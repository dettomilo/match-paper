package com.mobile.matchpaper.model;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
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
        try {
            InputStream is = (InputStream) new URL(URL).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
