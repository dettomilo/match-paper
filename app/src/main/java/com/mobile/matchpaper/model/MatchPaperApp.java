package com.mobile.matchpaper.model;

import android.content.Context;

public class MatchPaperApp extends android.app.Application {

    private static android.app.Application sApplication;

    public static android.app.Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}