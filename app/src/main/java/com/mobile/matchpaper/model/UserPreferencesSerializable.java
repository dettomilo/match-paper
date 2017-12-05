package com.mobile.matchpaper.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Serializable version of UserPreferences
 */

public class UserPreferencesSerializable implements Serializable {

    private static final long serialVersionUID = 777L;

    private static ArrayList<ImageContainer> likedImages = new ArrayList<>();
    private static Map<String, Integer> likedTags = new HashMap<>();

    public UserPreferencesSerializable(ArrayList<ImageContainer> likedImages, Map<String, Integer> likedTags) {
        this.likedImages = likedImages;
        this.likedTags = likedTags;
    }

    public static ArrayList<ImageContainer> getLikedImages() {
        return likedImages;
    }

    public static Map<String, Integer> getLikedTags() {
        return likedTags;
    }
}
