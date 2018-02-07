package com.mobile.matchpaper.model;

import android.graphics.drawable.Drawable;

import com.mobile.matchpaper.controller.ImageVisualizer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class contains the info about an image.
 */

public class ImageContainer implements Serializable {

    private String imageID = "";
    private ArrayList<String> tagList;
    private String previewURL = "";
    private String midResURL = "";
    private String fullHDURL = "";
    private String fullResURL = "";

    private Drawable previewDrawable = null;
    private Drawable midResDrawable = null;
    private Drawable fullHDDrawable = null;
    private Drawable fullResDrawable = null;

    public ImageContainer(String imageID, ArrayList<String> tagList, String previewURL, String midResURL, String fullHDURL, String fullResURL) {
        this.imageID = imageID;
        this.tagList = tagList;
        this.previewURL = previewURL;
        this.midResURL = midResURL;
        this.fullHDURL = fullHDURL;
        this.fullResURL = fullResURL;
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

    public String getFullHDURL() {
        return fullHDURL;
    }
    public String getFullResURL() {
        return fullResURL;
    }
    public Drawable getFullHDDrawable() {
        return fullHDDrawable;
    }

    public void setFullHDDrawable(Drawable fullHDDrawable) {
        this.fullHDDrawable = fullHDDrawable;
    }

    public Drawable getFullResDrawable() {
        return fullResDrawable;
    }
    public void setFullResDrawable(Drawable fullResDrawable) {
        this.fullResDrawable = fullResDrawable;
    }
}
