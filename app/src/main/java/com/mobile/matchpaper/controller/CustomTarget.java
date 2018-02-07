package com.mobile.matchpaper.controller;

import android.graphics.drawable.Drawable;

import com.squareup.picasso.Target;

public interface CustomTarget extends Target {
    void onDownloadCompleted(Drawable d);
}
