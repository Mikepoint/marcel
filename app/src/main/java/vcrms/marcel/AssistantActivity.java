package vcrms.marcel;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class AssistantActivity extends AppCompatActivity {

    private ImageButton speakBtn;
    private ScrollView speakScrlView;
    private LinearLayout linLayToSpeakScrlView;
    private TextToSpeech toSpeech;
    private String text;
    private int result;
    private AssistantActivity self = this;
    private GradientDrawable gradientDrawable = new GradientDrawable();
    private ArrayList<String> questions = new ArrayList<String>(Arrays.asList("what", "how", "where", "when", "which"));
    private QueryBuilder qb = new QueryBuilder();

    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        speakBtn = (ImageButton) findViewById(R.id.speakBtn);
        speakScrlView = (ScrollView) findViewById(R.id.speakScrlView);
        linLayToSpeakScrlView = (LinearLayout) findViewById(R.id.linLayToSpeakScrlView);
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
                    String bk = voiceInText.get(0);

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

                    String str =Custom.match(bk);
                    if (str != null) {
                        text = str;
                        speak();
                        break;
                    }
                    if (bk.toLowerCase().contains("cancel")) {
                        qb = new QueryBuilder();
                        text = "How can I help you?";
                        speak();
                        break;
                    }
                    if (!qb.isReady()) {
                        str = QueryBuilder.checkName(bk);
                        if (str != null) {
                            text = str;
                            speak();
                            Intent homeIntent = new Intent(AssistantActivity.this, ProductActivity.class);
                            homeIntent.putExtra("product", text.toLowerCase().replace(' ', '_'));
                            startActivity(homeIntent);
                            break;
                        } else {
                            str = QueryBuilder.factorize(bk);
                            if (str != null) {
                                qb.setCategory(str);
                                text = qb.validators.get(0).askQuestion();
                                speak();
                                break;
                            }
                        }
                    } else {
                        if (qb.nextSpec(bk)) {
                            if (qb.isCompleted()) {
                                text = qb.find();
                                if (text == null) {
                                    text = "Sorry, no matches found";
                                    speak();
                                    qb = new QueryBuilder();
                                } else {
                                    speak();
                                    qb = new QueryBuilder();
                                    Intent homeIntent = new Intent(AssistantActivity.this, ProductActivity.class);
                                    homeIntent.putExtra("product", text.toLowerCase().replace(' ', '_'));
                                    startActivity(homeIntent);
                                }
                                break;
                            }
                        }
                        text = qb.validators.get(qb.currentValidator).askQuestion();
                        speak();
                        break;
                    }
                }

                text = "Sorry I didn't get it.";
                speak();
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
