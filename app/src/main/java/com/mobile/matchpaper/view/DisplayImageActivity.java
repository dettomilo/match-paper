package com.mobile.matchpaper.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mobile.matchpaper.R;

public class DisplayImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        Intent intent = getIntent();
        String msg = intent.getStringExtra(ListFragment.INTENT_STRING_CONTENT);

        TextView textView = findViewById(R.id.textView);
        textView.setText(msg);
    }
}
