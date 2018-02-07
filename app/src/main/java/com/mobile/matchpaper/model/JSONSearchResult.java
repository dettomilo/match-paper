package com.mobile.matchpaper.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * A single object of this class is created from a search result representing the searched data
 */

public class JSONSearchResult {

    private int totalImagesFound = 0;
    private ArrayList<ImageContainer> imageList;

    public JSONSearchResult (Integer totalImagesFound, JSONArray imagesArray) {
        this.totalImagesFound = totalImagesFound;
        this.imageList = parseJSONImages(imagesArray);
    }

    private ArrayList<ImageContainer> parseJSONImages(JSONArray imagesArray){
        ArrayList<ImageContainer> tmpList = new ArrayList<>();

        for (int i = 0; i < imagesArray.length(); i++) {
            JSONObject currImg = null;

            try {
                currImg = imagesArray.getJSONObject(i);

                tmpList.add(new ImageContainer(
                            currImg.getString("id"),
                            tagParser(currImg.getString("tags")),
                            currImg.getString("previewURL"),
                            currImg.getString("webformatURL"),
                            currImg.getString("fullHDURL"),
                            currImg.getString("imageURL"))
                );

            } catch (JSONException e) {
                Log.e("Parsing Error", "Error parsing results array.");
                e.printStackTrace();
            }
        }

        return tmpList;
    }

    /**
        This method returns a list of images, the amount of images returned is always
        less or equal to the amount of total images cointaned in the search result.
        @param amountOfImages The amount of images to be returned.
        @param shuffleArray Should the resulting array be randomized?
        @return A list of ImageContainer
     */
    public ArrayList<ImageContainer> getImageList(Integer amountOfImages, Boolean shuffleArray) {
        if (amountOfImages > imageList.size()) {
            amountOfImages = imageList.size();
        }

        ArrayList<ImageContainer> result = new ArrayList<>(imageList.subList(0, amountOfImages));

        if (shuffleArray) {
            Collections.shuffle(result);
        }

        return new ArrayList<>(result);
    }

    /**
        This method returns the searched list of images.
        @param shuffleArray Should the resulting array be randomized?
        @return A list of ImageContainer
     */
    public ArrayList<ImageContainer> getImageList(Boolean shuffleArray) {
        ArrayList<ImageContainer> result = new ArrayList<>(imageList);

        if (shuffleArray) {
            Collections.shuffle(result);
        }

        return new ArrayList<>(result);
    }

    private ArrayList<String> tagParser(String tags){

        // Splits tags by comma
        String [] tmpTags = tags.split(",");

        for (int i = 0; i < tmpTags.length; i++){

            // Removes first blank space if it's present
            if (tmpTags[i].charAt(0) == ' ' && tmpTags[i].length() >= 2) {
                tmpTags[i] = tmpTags[i].substring(1);
            }

            // Normalizes tags to lowercase
            tmpTags[i] = tmpTags[i].toLowerCase();
        }

        return new ArrayList<>(Arrays.asList(tmpTags));
    }

    /**
     * Get the number of found images in total (Max 500)
     * @return The number of found images.
     */
    public int getNumberOfImagesFound() {
        return totalImagesFound;
    }
}
