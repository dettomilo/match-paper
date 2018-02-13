package com.mobile.matchpaper.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.mobile.matchpaper.view.FavoritesFragment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The object of this class contains the user data as a SINGLETON object.
 * As a singleton this class is automatically instantiated only one time.
 */

public class UserPreferences {

    private static UserPreferences Instance = null;
    private static final String SAVE_FILENAME = "MatchPaperSave";

    private static ArrayList<ImageContainer> likedImages = new ArrayList<>();
    private static Map<String, Integer> likedTags = new HashMap<>();

    private UserPreferences (){
        // To avoid instantiation :)
    }

    /**
     * Get the SINGLETON of UserPreferences
     * @return The user preference object.
     */
    public static UserPreferences GetInstance() {
        if (Instance == null) {
            Instance = new UserPreferences();
        }

        return Instance;
    }

    /**
     * Saves user prefs to disk.
     * @throws IOException
     */
    public static void SavePreferences() throws IOException {
        Context contextPath = MatchPaperApp.getContext();

        FileOutputStream outFile = contextPath.openFileOutput(SAVE_FILENAME, Context.MODE_PRIVATE);
        ObjectOutputStream outputStream = new ObjectOutputStream(outFile);

        ArrayList<String> likedIDs = new ArrayList<>();

        for (ImageContainer img : likedImages) {
            likedIDs.add(img.getImageID());
        }

        UserPreferencesSerializable prefs = new UserPreferencesSerializable(likedIDs, new HashMap<>(likedTags));
        outputStream.writeObject(prefs);

        Log.d("FILESAVE", "SAVED " + prefs.getLikedImages().size() + " images and: " + prefs.getLikedTags().size() + " tags.");

        outputStream.flush();
        outputStream.close();
    }

    /**
     * Loads user prefs from disk.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static ArrayList<String> LoadPreferences() throws IOException, ClassNotFoundException {
        Context contextPath = MatchPaperApp.getContext();

        ObjectInputStream inputStream = new ObjectInputStream(contextPath.openFileInput(SAVE_FILENAME));
        UserPreferencesSerializable loadedUserPrefs = (UserPreferencesSerializable)inputStream.readObject();

        ArrayList<String> likedIDs = new ArrayList<>(loadedUserPrefs.getLikedImages());
        likedTags = new HashMap<>(loadedUserPrefs.getLikedTags());

        Log.d("FILESAVE", "LOADED " + likedIDs.size() + " images and: " + likedTags.size() + " tags.");

        inputStream.close();

        return likedIDs;
    }

    public static void SetLikedImages(ArrayList<ImageContainer> downloadedList){
        likedImages = new ArrayList<>(downloadedList);
        FavoritesFragment.notifyViewForDatasetChange();
    }

    /**
     * Memorizes the liked image tags.
     * @param likedImage The image to like.
     */
    public static void LikeImage(ImageContainer likedImage) {

        // Adds +1 to every tag of the image
        for (String tag:likedImage.getTagList()) {

            String lowerTag = tag.toLowerCase();
            Integer previousTagLikes = 0;

            if (likedTags.containsKey(lowerTag)) {
                previousTagLikes = likedTags.get(lowerTag).intValue();
            }

            likedTags.put(lowerTag, previousTagLikes + 1);
        }

        SaveStatusToDisk();
    }

    /**
     * Dislike image tags.
     * @param imageID The image to like.
     */
    public static void DislikeImage(String imageID) {

        ImageContainer foundImg = null;

        for (ImageContainer img: likedImages) {
            if (imageID.equals(img.getImageID())) {
                foundImg = img;
                break;
            }
        }

        if (foundImg != null) {

            // Remove -1 to every tag of the image
            for (String tag:foundImg.getTagList()) {

                String lowerTag = tag.toLowerCase();
                // There should be already at least 1 like per tag since the image was found in the liked ones.
                Integer previousTagLikes  = likedTags.get(lowerTag).intValue();

                likedTags.put(lowerTag, previousTagLikes - 1);

                if(likedTags.get(lowerTag).intValue() == 0) {
                    likedTags.remove(lowerTag);
                }
            }
        } else {
            Log.e("Image ID not found!", "Can't unlike image with ID: " + imageID);
        }

        SaveStatusToDisk();
    }

    /**
     * Memorizes the liked image and updateds liked tags.
     * @param likedImage The image to like.
     */
    public static void AddImageToFavourites(ImageContainer likedImage) {
        if (!likedImages.contains(likedImage)) {
            likedImages.add(likedImage);

            // Adds +1 to every tag of the image
            for (String tag:likedImage.getTagList()) {

                String lowerTag = tag.toLowerCase();
                Integer previousTagLikes = 0;

                if (likedTags.containsKey(lowerTag)) {
                    previousTagLikes = likedTags.get(lowerTag).intValue();
                }

                likedTags.put(lowerTag, previousTagLikes + 1);
            }

            FavoritesFragment.notifyViewForDatasetChange();

            SaveStatusToDisk();

            Log.d("UserPreferences", "Favourite images count: " + likedImages.size());
        }
    }

    /**
     * This removes an image from the liked list, and removes the corresponding liked tags.
     * @param imageID The ID of the image to remove.
     */
    public static void RemoveImageFromFavourites(String imageID) {

        ImageContainer foundImg = null;

        for (ImageContainer img: likedImages) {
            if (imageID.equals(img.getImageID())) {
                foundImg = img;
                break;
            }
        }

        if (foundImg != null) {
            likedImages.remove(foundImg);

            // Remove -1 to every tag of the image
            for (String tag:foundImg.getTagList()) {

                String lowerTag = tag.toLowerCase();
                // There should be already at least 1 like per tag since the image was found in the liked ones.
                Integer previousTagLikes  = likedTags.get(lowerTag).intValue();

                likedTags.put(lowerTag, previousTagLikes - 1);

                if(likedTags.get(lowerTag).intValue() == 0) {
                    likedTags.remove(lowerTag);
                }
            }
        } else {
            Log.e("Image ID not found!", "Can't unlike image with ID: " + imageID);
        }

        FavoritesFragment.notifyViewForDatasetChange();

        SaveStatusToDisk();
    }

    public static void SaveStatusToDisk() {
        try {
            if (!likedTags.isEmpty()) {
                SavePreferences();
            }
        } catch (IOException e) {
            Log.d("FILESAVE", "ERROR SAVING! :(");
            e.printStackTrace();
        }
    }

    public static Boolean getIfImageIsInFavourites(String imgID) {
        for (ImageContainer img : likedImages) {
            if (imgID.equals(img.getImageID())){
                return true;
            }
        }

        return false;
    }

    /**
     * Get all the liked images.
     * @return The liked images arraylist.
     */
    public static ArrayList<ImageContainer> GetLikedImages() {
        return new ArrayList<>(likedImages);
    }

    /**
     * Gets the map TAG - LIKES ordered descending.
     * @return The ordered map.
     */
    public static Map<String, Integer> GetMostLikedTags() {

        return sortByValue(likedTags);
    }

    /**
     * Map ordering by value ♥ Stack Overflow ♥
     */
    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
