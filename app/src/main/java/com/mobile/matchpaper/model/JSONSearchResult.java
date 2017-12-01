package com.mobile.matchpaper.model;

import org.json.JSONArray;

/**
 * A single object of this class is created from a search result representing the searched data
 */

public class JSONSearchResult {

    private int totalImagesFound = 0;

    public JSONSearchResult (int totalImagesFound, JSONArray imagesArray) {
        this.totalImagesFound = totalImagesFound;
    }

    public int getTotalImagesFound() {

        return totalImagesFound;
    }
}
