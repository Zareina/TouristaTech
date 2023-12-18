package com.zareinaflameniano.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Retrieve data from intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String accName = intent.getStringExtra("accName");
            String accLocation = intent.getStringExtra("accLocation");
            String accImage = intent.getStringExtra("accImage");


            ImageView tvImage = findViewById(R.id.tvImage);
            TextView tvName = findViewById(R.id.tvName);
            TextView tvLocation = findViewById(R.id.tvLocation);
            TextView tvDescription= findViewById(R.id.tvDescription);


            int imageResourceId = getResources().getIdentifier(accImage, "drawable", getPackageName());
            if (imageResourceId != 0) {
                tvImage.setImageResource(imageResourceId);
            } else {
                tvImage.setImageResource(R.drawable.depthpool);
            }
            tvName.setText(accName);
            tvLocation.setText(accLocation);
            tvDescription.setText(accImage);
        }
    }
}