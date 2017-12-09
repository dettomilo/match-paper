package com.mobile.matchpaper.model;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * This class contains the info about an image.
 */

public class ImageContainer {

    private String imageID = "";
    private ArrayList<String> tagList;
    private String previewURL = "";
    private String midResURL = "";
    private Drawable midResDrawable = null;

    public ImageContainer(String imageID, ArrayList<String> tagList, String previewURL, String midResURL) {
        this.imageID = imageID;
        this.tagList = tagList;
        this.previewURL = previewURL;
        this.midResURL = midResURL;
    }

    public String getImageID() {
        return imageID;
    }

    public ArrayList<String> getTagList() {
        return tagList;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public String getMidResURL() {
        return midResURL;
    }

    public Drawable getDrawablePreview() {
        return ImageVisualizer.getDrawableImageFromURL(previewURL);
    }

    public Drawable getDrawableMidRes() {

        if (midResDrawable == null){
            midResDrawable = ImageVisualizer.getDrawableImageFromURL(midResURL);
        }

        return midResDrawable;
    }
}
