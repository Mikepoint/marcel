package vcrms.marcel;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity {

    private ImageButton speakBtn;
    private ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        speakBtn = (ImageButton) findViewById(R.id.speakBtn);
        productImage = (ImageView) findViewById(R.id.productImage);

        Intent i = getIntent();
        String product = i.getStringExtra("product");

        try {
            Field f = R.drawable.class.getField(product);
            productImage.setImageResource(f.getInt(null));
        } catch (Exception e) {

        }

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
