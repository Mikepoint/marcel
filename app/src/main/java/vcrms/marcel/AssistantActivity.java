package vcrms.marcel;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public class AssistantActivity extends AppCompatActivity {

    private ImageButton speakBtn;
    private ScrollView speakScrlView;
    private LinearLayout linLayToSpeakScrlView;
    private TextToSpeech toSpeech;
    private String text;
    private int result;
    private LinkedList<TextView> textViews;
    private AssistantActivity self = this;

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

                    TextView newText = new TextView(this);
                    newText.setText(text + "\n");
                    newText.setTextColor(Color.BLUE);
                    newText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    linLayToSpeakScrlView.addView(newText);

                    speakScrlView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

                    linLayToSpeakScrlView.postDelayed(new Runnable() {
                        public void run() {
                            speakScrlView.fullScroll(HorizontalScrollView.FOCUS_DOWN);
                        }
                    }, 100L);

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
                                    newResponse.setText(text + "\n");
                                    linLayToSpeakScrlView.addView(newResponse);
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
}
