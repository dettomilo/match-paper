package com.mobile.matchpaper.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.mobile.matchpaper.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class contains methods to visualize images from URL.
 */

public class ImageVisualizer {

    /**
     * Returns a Drawable from an URL
     * @param URL The URL of the Image.
     * @return The Drawable representing the image.
     */
    public static Drawable getDrawableImageFromURL(final String URL) {
        ImageView view = new ImageView(MatchPaperApp.getContext());

        Picasso.with(MatchPaperApp.getContext())
                .load(URL)
                .resize(300,300)
                .centerCrop()
                .into(view);

        return view.getDrawable();
    }
}
