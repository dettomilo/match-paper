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

    private ArrayList<String> likedImages = new ArrayList<>();
    private Map<String, Integer> likedTags = new HashMap<>();

    public UserPreferencesSerializable(final ArrayList<String> likedImages,final Map<String, Integer> likedTags) {
        this.likedImages = likedImages;
        this.likedTags = likedTags;
    }

    public ArrayList<String> getLikedImages() {
        return likedImages;
    }

    public Map<String, Integer> getLikedTags() {
        return likedTags;
    }
}
