package com.mobile.matchpaper.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
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
    private static ArrayList<CustomTarget> targetsQueue = new ArrayList<>();

    /**
     * Returns a Drawable from an URL
     * @return The Drawable representing the image.
     */
    public static void downloadImageAndNotifyView(final String downloadFinishedEventName, final ImageContainer originalImage, final ResolutionQuality quality) {
        ImageView view = new ImageView(MatchPaperApp.getContext());

        String URL = "";

        CustomTarget finalTarget = new CustomTarget() {
            private Bitmap img;

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                img = bitmap;

                onDownloadCompleted(getDrawable());
            }

            @Override
            public void onDownloadCompleted(Drawable d){
                Intent intent = new Intent(downloadFinishedEventName);
                intent.putExtra("loadedImageID", originalImage.getImageID());
                switch (quality){
                    case PREVIEW:
                        originalImage.setPreviewDrawable(d);
                        break;
                    case MID:
                        originalImage.setMidResDrawable(d);
                        break;
                    case HIGH:
                        originalImage.setFullHDDrawable(d);
                        break;
                    case FULL:
                        originalImage.setFullResDrawable(d);
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
                if (originalImage.getPreviewDrawable() == null) {
                    URL = originalImage.getPreviewURL();
                    Picasso.with(MatchPaperApp.getContext())
                            .load(URL)
                            .resize(350,350)
                            .centerCrop()
                            .into(finalTarget);
                } else {
                    finalTarget.onDownloadCompleted(originalImage.getPreviewDrawable());
                }
                break;
            case MID:
                if (originalImage.getMidResDrawable() == null) {
                    URL = originalImage.getMidResURL();
                    Picasso.with(MatchPaperApp.getContext())
                            .load(URL)
                            .into(finalTarget);
                } else {
                    finalTarget.onDownloadCompleted(originalImage.getMidResDrawable());
                }
                break;
            case HIGH:
                if (originalImage.getFullHDDrawable() == null) {
                    URL = originalImage.getFullHDURL();
                    Picasso.with(MatchPaperApp.getContext())
                            .load(URL)
                            .into(finalTarget);
                } else {
                    finalTarget.onDownloadCompleted(originalImage.getFullHDDrawable());
                }
                break;
            case FULL:
                if (originalImage.getFullResDrawable() == null) {
                    URL = originalImage.getFullResURL();
                    Picasso.with(MatchPaperApp.getContext())
                            .load(URL)
                            .into(finalTarget);
                } else {
                    finalTarget.onDownloadCompleted(originalImage.getFullResDrawable());
                }
                break;
        }

        targetsQueue.add(finalTarget);
    }

    public enum ResolutionQuality {
        FULL,
        HIGH,
        MID,
        PREVIEW;
    }
}
