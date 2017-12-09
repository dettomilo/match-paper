package com.mobile.matchpaper.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ImageView;

import com.mobile.matchpaper.model.ImageContainer;
import com.mobile.matchpaper.model.MatchPaperApp;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * This class contains methods to visualize images from URL.
 */

public class ImageVisualizer {
    private static ArrayList<Target> targetsQueue = new ArrayList<>();

    /**
     * Returns a Drawable from an URL
     * @param URL The URL of the Image.
     * @return The Drawable representing the image.
     */
    public static void downloadImageAndNotifyView(final String URL, final String downloadFinishedEventName, final ImageContainer originalImage) {
        ImageView view = new ImageView(MatchPaperApp.getContext());

        Target finalTarget = new Target() {
            private Bitmap img;

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                img = bitmap;

                Intent intent = new Intent(downloadFinishedEventName);
                intent.putExtra("loadedImageID", originalImage.getImageID());
                originalImage.setMidResDrawable(getDrawable());
                LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).sendBroadcast(intent);

                targetsQueue.remove(this);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

            public Drawable getDrawable(){
                return new BitmapDrawable(MatchPaperApp.getContext().getResources(), img);
            }
        };



        Picasso.with(MatchPaperApp.getContext())
                .load(URL)
                .resize(350,350)
                .centerCrop()
                .into(finalTarget);

        targetsQueue.add(finalTarget);
    }
}
