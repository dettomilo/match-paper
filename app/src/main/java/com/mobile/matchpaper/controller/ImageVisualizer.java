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
     * @return The Drawable representing the image.
     */
    public static void downloadImageAndNotifyView(final String downloadFinishedEventName, final ImageContainer originalImage, final ResolutionQuality quality) {
        ImageView view = new ImageView(MatchPaperApp.getContext());

        String URL = "";

        Target finalTarget = new Target() {
            private Bitmap img;

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                img = bitmap;

                Intent intent = new Intent(downloadFinishedEventName);
                intent.putExtra("loadedImageID", originalImage.getImageID());
                switch (quality){
                    case PREVIEW:
                        originalImage.setPreviewDrawable(getDrawable());
                        break;
                    case MID:
                        originalImage.setMidResDrawable(getDrawable());
                        break;
                    case HIGH:
                        /**
                         * TODO Add HQ download.
                         */
                        originalImage.setMidResDrawable(getDrawable());
                        break;
                }

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

        switch (quality) {
            case PREVIEW:
                URL = originalImage.getPreviewURL();
                Picasso.with(MatchPaperApp.getContext())
                        .load(URL)
                        .resize(350,350)
                        .centerCrop()
                        .into(finalTarget);
                break;
            case MID:
                URL = originalImage.getMidResURL();
                Picasso.with(MatchPaperApp.getContext())
                        .load(URL)
                        .into(finalTarget);
                break;
            case HIGH:
                /**
                 * TODO Add HQ download.
                 */
                URL = originalImage.getMidResURL();
                Picasso.with(MatchPaperApp.getContext())
                        .load(URL)
                        .into(finalTarget);
                break;
        }

        targetsQueue.add(finalTarget);
    }

    public enum ResolutionQuality {
        HIGH,
        MID,
        PREVIEW;
    }
}
