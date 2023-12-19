package com.zareinaflameniano.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SliderScreen extends Activity {
    Button backButton, nextButton, skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_screen);

        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderScreen.this, SliderScreen2.class);
                startActivity(intent);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SliderScreen.this, GetStarted.class);
                startActivity(i);
            }
        });
    }
}

