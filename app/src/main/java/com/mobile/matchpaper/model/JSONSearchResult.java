package com.mobile.matchpaper.model;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A single object of this class is created from a search result representing the searched data
 */

public class JSONSearchResult {

    private int totalImagesFound = 0;

    public JSONSearchResult (int totalImagesFound, JSONArray imagesArray) {
        this.totalImagesFound = totalImagesFound;
    }

    /**
        This method returns a list of images, the amount of images returned is always
        less or equal to the amount of total images cointaned in the search result.
        @param amountOfImages The amount of images to be returned, -1 to return all the images.
        @return A list of ImageContainer
     */
    public ArrayList<ImageContainer> requestImageList(int amountOfImages) {
        return null;
    }

    public int getTotalImagesFound() {
        return totalImagesFound;
    }
}
