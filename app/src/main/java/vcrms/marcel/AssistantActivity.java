package vcrms.marcel;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

public class AssistantActivity extends AppCompatActivity {

    private ImageButton speakBtn;
    private ScrollView speakScrlView;
    private LinearLayout linLayToSpeakScrlView;
    private TextToSpeech toSpeech;
    private String text;
    private String state = "";
    private int result;
    private LinkedList<TextView> textViews;
    private AssistantActivity self = this;
    private GradientDrawable gradientDrawable = new GradientDrawable();
    private ArrayList<String> questions = new ArrayList<String>(Arrays.asList("what", "how", "where", "when", "which"));
    MessagesDatabase msgDb = new MessagesDatabase();
    ArrayList<String> possibleMatches = new ArrayList<String>();
    private ArrayList<String> jokes = new ArrayList<String>(Arrays.asList(
            "Two tabels sit in a bar. A query walks in and asks them: Can I join you?",
            "What does a programmer shout when he's sinking? ... F1! F1!",
            "What's the name of Irina Login's sister? ... Irina Logout."
    ));

    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        speakBtn = (ImageButton) findViewById(R.id.speakBtn);
        speakScrlView = (ScrollView) findViewById(R.id.speakScrlView);
        linLayToSpeakScrlView = (LinearLayout) findViewById(R.id.linLayToSpeakScrlView);
        msgDb.initialize();
        speakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnToOpenMic();
            }
        });

        Intent i = getIntent();
        if(i.getBooleanExtra("openMic", false)) {
            btnToOpenMic();
        }
    }

    private void btnToOpenMic() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Emma is listening...");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);
        } catch (ActivityNotFoundException tim) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_OUTPUT:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> voiceInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text = voiceInText.get(0);

                    AnswerProcessor ap = new AnswerProcessor();
                    AnswerKeywords ak = ap.generateAnswerKeywords(text.toLowerCase());

                    for (String question : questions) {
                        if (text.contains(question)) {
                            text += "?";
                        }
                    }
                    text = "      " + text.substring(0,1).toUpperCase() + text.substring(1) + "\n";

                    TextView newText = new TextView(this);
                    newText.setText(text);
                    newText.setTextColor(Color.WHITE);
                    gradientDrawable.setCornerRadius(20);
                    gradientDrawable.setColor(Color.BLUE);
                    newText.setBackground(gradientDrawable);
                    newText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    linLayToSpeakScrlView.addView(newText);
                    linLayToSpeakScrlView.addView(new TextView(this));

                    speakScrlView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

                    linLayToSpeakScrlView.postDelayed(new Runnable() {
                        public void run() {
                            speakScrlView.fullScroll(HorizontalScrollView.FOCUS_DOWN);
                        }
                    }, 100L);

                    if (possibleMatches == null) {
                        possibleMatches = new ArrayList<String>();
                    }
                    possibleMatches = generateResponse(ak, possibleMatches);

                    if (possibleMatches != null) {
                        Log.e("DUMP",""+possibleMatches.size());
                        if (state == null || state.equals("")) {
                            state = "brand";
                        } else if (state.equals("brand")) {
                            Log.e("meeeem","Switch to mem");
                            state = "memory";
                        } else if (state.equals("memory")) {
                            state = "";
                        }
                    }
                   // speak();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toSpeech != null)
        {
            toSpeech.stop();
            toSpeech.shutdown();
        }
    }

    public ArrayList<String> generateResponse(AnswerKeywords keywords, ArrayList<String> inheritedMatches) {
        Log.e("DUMP inh",""+inheritedMatches.size());

        if (keywords.isGreeting()) {
            text = "Hi";
            speak();
            return null;
        }
        if (keywords.isHelp()) {
            text = "I am here to help you find products you need. You can ask me about a certain product or we can find the best choice together.";
            speak();
            return null;
        }
        if (keywords.getProduct() != null) {
            Log.e("Produuuct", keywords.getProduct());
            text = keywords.getProduct();
            speak();
            Intent homeIntent = new Intent(AssistantActivity.this, ProductActivity.class);
            homeIntent.putExtra("product", text.toLowerCase().replace(' ', '_'));
            startActivity(homeIntent);
            return null;
        }
        if (state.equals("") && keywords.getOther() != null) {
            if (keywords.getOther().contains("baby") || keywords.getOther().contains("call")) {
                text = "Seriously? This is how you're trying to be funny?";
                speak();
                return null;
            } else if (keywords.getOther().contains("joke")) {
                Random rand = new Random();
                text = jokes.get(rand.nextInt(jokes.size()));
                speak();
                return null;
            } else if (keywords.getOther().contains("buy") && keywords.getOther().contains("emag")) {
                text = "I'm still trying to figure it out. Anyone here was so nice to me since i've been booted.";
                speak();
                return null;
            } else if (keywords.getOther().contains("time")) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
                text = sdf.format(new Date());
                speak();
                return null;
            }
        }
        if (state.equals("brand")) {
            if (keywords.isNegation()) {
                text = "How much memory do you want?";
                speak();
                return  inheritedMatches;
            }
            ArrayList<String> possibleMatchesList = msgDb.eliminateProducts(inheritedMatches, "brand", keywords.getOther());
            if (possibleMatchesList.size() == 1) {
                text = possibleMatchesList.get(0);
                speak();
                state = "";
                Intent homeIntent = new Intent(AssistantActivity.this, ProductActivity.class);
                homeIntent.putExtra("product", text.toLowerCase().replace(' ', '_'));
                startActivity(homeIntent);
                return null;
            } else {
                text = "How much memory do you want?";
                speak();
                return possibleMatchesList;
            }
        } else if (state.equals("memory")) {
            if (keywords.isNegation() && !inheritedMatches.isEmpty()) {
                text = inheritedMatches.get(0);
                speak();
                state = "";
                Intent homeIntent = new Intent(AssistantActivity.this, ProductActivity.class);
                homeIntent.putExtra("product", text.toLowerCase().replace(' ', '_'));
                startActivity(homeIntent);
                return  null;
            }
            ArrayList<String> possibleMatchesList = msgDb.eliminateProducts(inheritedMatches, "memory", keywords.getOther());
            if (possibleMatchesList.size() == 1) {
                text = possibleMatchesList.get(0);
                speak();
                state = "";
                Intent homeIntent = new Intent(AssistantActivity.this, ProductActivity.class);
                homeIntent.putExtra("product", text.toLowerCase().replace(' ', '_'));
                startActivity(homeIntent);
                return null;
            } else if (possibleMatchesList.size() > 0) {
                text =  possibleMatchesList.get(0);
                speak();
                Intent homeIntent = new Intent(AssistantActivity.this, ProductActivity.class);
                homeIntent.putExtra("product", text.toLowerCase().replace(' ', '_'));
                startActivity(homeIntent);
                return null;
            } else {
                text =  "Could not find a product that matches the description";
                state = "";
                speak();
                return null;
            }
        }
        if (keywords.getCategory() != null && !keywords.getCategory().isEmpty()) {
            ArrayList<String> possibleMatchesList = new ArrayList<String>();
            possibleMatchesList.addAll(msgDb.productsOfCategory(keywords.getCategory().toLowerCase()));
            if (possibleMatchesList.isEmpty()) {
                text = "There is no such category. Please try again.";
                state = "";
                speak();
                return null;
            } else if (possibleMatchesList.size() == 1) {
                text = possibleMatchesList.get(0);
                speak();
                Intent homeIntent = new Intent(AssistantActivity.this, ProductActivity.class);
                homeIntent.putExtra("product", text.toLowerCase().replace(' ', '_'));
                startActivity(homeIntent);
                return null;
            }
            text = "What brand do you want?";
            speak();
            return possibleMatchesList;
        }
        text = "I don't get it.";
        speak();
        return null;
    }

    private void speak() {
        toSpeech = new TextToSpeech(AssistantActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = toSpeech.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(getApplicationContext(), "Internal Error", Toast.LENGTH_SHORT).show();
                    } else {
                        toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);

                        TextView newResponse = new TextView(self);
                        newResponse.setTextColor(Color.RED);
                        newResponse.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                        newResponse.setText("      " + text.substring(0,1).toUpperCase() + text.substring(1) + "\n");
                        linLayToSpeakScrlView.addView(newResponse);
                        linLayToSpeakScrlView.addView(new TextView(self));
                        linLayToSpeakScrlView.postDelayed(new Runnable() {
                            public void run() {
                                speakScrlView.fullScroll(HorizontalScrollView.FOCUS_DOWN);
                            }
                        }, 100L);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Internal Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
