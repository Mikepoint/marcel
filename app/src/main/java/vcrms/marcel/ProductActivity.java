package vcrms.marcel;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Locale;

public class ProductActivity extends AppCompatActivity {

    private ImageButton speakBtn;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        speakBtn = (ImageButton) findViewById(R.id.speakBtn);

        speakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assistentIntent = new Intent(ProductActivity.this, AssistantActivity.class);
                assistentIntent.putExtra("openMic", true);
                startActivity(assistentIntent);
            }
        });
    }
}
