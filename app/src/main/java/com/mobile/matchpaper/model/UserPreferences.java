package com.mobile.matchpaper.model;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The object of this class contains the user data as a SINGLETON object.
 * As a singleton this class is automatically instantiated only one time.
 */

public class UserPreferences implements Serializable {

    private static UserPreferences Instance = null;
    private static final long serialVersionUID = 777L;
    private static final String SAVE_FILENAME = "MatchPaperSave";

    protected UserPreferences (){
        // To avoid instantiation :)
    }

    /**
     * Get the SINGLETON of UserPreference
     * @return The user preference object.
     */
    public static UserPreferences GetInstance() {
        if (Instance == null) {
            Instance = new UserPreferences();
        }

        return Instance;
    }

    public static void SavePreferences(Context contextPath) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(contextPath.openFileOutput(SAVE_FILENAME, Context.MODE_PRIVATE));
        outputStream.writeObject(GetInstance());

        outputStream.flush();
        outputStream.close();
    }

    public static void LoadPreferences(Context contextPath) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(contextPath.openFileInput(SAVE_FILENAME));
        Instance = (UserPreferences)inputStream.readObject();
    }
}
