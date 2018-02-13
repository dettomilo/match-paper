package com.mobile.matchpaper.view;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.ImageVisualizer;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.controller.SearchResultReceivedListener;
import com.mobile.matchpaper.model.ImageContainer;
import com.mobile.matchpaper.model.JSONSearchResult;
import com.mobile.matchpaper.model.MatchPaperApp;
import com.mobile.matchpaper.model.UserPreferences;

import java.io.IOException;

public class DisplayImageActivity extends AppCompatActivity {

    private ImageView fullScreenImg;
    private ImageButton favoritesButton;
    private ImageButton downloadButton;
    private ImageButton setWallpaperButton;
    private ImageContainer tmpImage;
    private ProgressBar progressBar;
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
        progressBar = findViewById(R.id.progressbar_image);

        Intent intent = getIntent();
        String imageID = intent.getStringExtra(ListFragment.INTENT_STRING_CONTENT);

        tmpImage = ListFragment.getImageFromHomeByID(imageID);

        if (tmpImage != null) {
            ImageVisualizer.downloadImageAndNotifyView(DOWNLOAD_FINISHED_EVENT_NAME, tmpImage, ImageVisualizer.ResolutionQuality.MID);
        }

        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Image added to favorites!", Toast.LENGTH_SHORT).show();
                UserPreferences.AddImageToFavourites(tmpImage);
            }
        });

        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WallpaperManager myWallpaperManager = WallpaperManager
                        .getInstance(getApplicationContext());

                try {
                    myWallpaperManager.setBitmap(((BitmapDrawable)fullScreenImg.getDrawable()).getBitmap());
                    Toast.makeText(getBaseContext(), "Wallpaper set!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        ((BitmapDrawable)fullScreenImg.getDrawable()).getBitmap(),
                        tmpImage.getImageID(),
                        "Downloaded from Matchpaper");
            }
        });
    }

    private BroadcastReceiver fullscreenImageDownloadFinished = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String imageID = intent.getStringExtra("loadedImageID");

            fullScreenImg.setImageDrawable(tmpImage.getMidResDrawable());
            //The following line doesn't seem to be necessary
            fullScreenImg.postInvalidate();

            progressBar.setVisibility(View.GONE);
            fullScreenImg.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).unregisterReceiver(fullscreenImageDownloadFinished);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            UserPreferences.GetInstance().SavePreferences();
            Log.d("FILESAVE", "on exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
