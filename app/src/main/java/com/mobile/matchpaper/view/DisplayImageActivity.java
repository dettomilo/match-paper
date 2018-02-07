package com.mobile.matchpaper.view;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.ImageVisualizer;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.controller.SearchResultReceivedListener;
import com.mobile.matchpaper.model.ImageContainer;
import com.mobile.matchpaper.model.JSONSearchResult;
import com.mobile.matchpaper.model.MatchPaperApp;

import java.io.IOException;

public class DisplayImageActivity extends AppCompatActivity {

    private ImageView fullScreenImg;
    private ImageButton favoritesButton;
    private ImageButton downloadButton;
    private ImageButton setWallpaperButton;
    private ImageContainer tmpImage;
    public static final String DOWNLOAD_FINISHED_EVENT_NAME = "display_image_download_finished";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).registerReceiver(fullscreenImageDownloadFinished,
                new IntentFilter(DOWNLOAD_FINISHED_EVENT_NAME));

        setContentView(R.layout.activity_display_image);

        fullScreenImg = findViewById(R.id.full_screen_img);
        favoritesButton = findViewById(R.id.btn_img_favorites);
        downloadButton = findViewById(R.id.btn_img_download);
        setWallpaperButton = findViewById(R.id.btn_img_set_wallpaper);

        Intent intent = getIntent();
        String imageID = intent.getStringExtra(ListFragment.INTENT_STRING_CONTENT);

        tmpImage = ListFragment.getImageFromHomeByID(imageID);

        if (tmpImage != null) {
            ImageVisualizer.downloadImageAndNotifyView(DOWNLOAD_FINISHED_EVENT_NAME, tmpImage, ImageVisualizer.ResolutionQuality.MID);
        }

        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WallpaperManager myWallpaperManager = WallpaperManager
                        .getInstance(getApplicationContext());

                //TODO replace ... with img
                /*try {
                    myWallpaperManager.setResource(R.drawable. ... );
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }

    private BroadcastReceiver fullscreenImageDownloadFinished = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String imageID = intent.getStringExtra("loadedImageID");

            ImageView view = new ImageView(context);
            view.setImageDrawable(tmpImage.getMidResDrawable());
            setContentView(view);
        }
    };
}
