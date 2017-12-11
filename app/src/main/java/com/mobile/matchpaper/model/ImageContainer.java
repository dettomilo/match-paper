package com.mobile.matchpaper.model;

import android.graphics.drawable.Drawable;

import com.mobile.matchpaper.controller.ImageVisualizer;

import java.util.ArrayList;

/**
 * This class contains the info about an image.
 */

public class ImageContainer {

    private String imageID = "";
    private ArrayList<String> tagList;
    private String previewURL = "";
    private String midResURL = "";

    private Drawable previewDrawable = null;
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

    public Drawable getMidResDrawable() {
        return midResDrawable;
    }

    public void setMidResDrawable(Drawable midResDrawable) {
        this.midResDrawable = midResDrawable;
    }

    public Drawable getPreviewDrawable() {
        return previewDrawable;
    }

    public void setPreviewDrawable(Drawable previewDrawable) {
        this.previewDrawable = previewDrawable;
    }
}
