package com.mobile.matchpaper.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.matchpaper.R;

public class DisplayImageActivity extends AppCompatActivity {

    private ImageView fullScreenImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        fullScreenImg = findViewById(R.id.full_screen_img);

        Intent intent = getIntent();
        String imageID = intent.getStringExtra(ListFragment.INTENT_STRING_CONTENT);

        //TODO request image with said ID
    }
}
