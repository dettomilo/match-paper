package com.mobile.matchpaper.controller;

import com.mobile.matchpaper.model.JSONSearchResult;

/**
 * Created by Alberto on 07/02/2018.
 */

public interface SearchResultReceivedListener {
    void callListenerEvent(JSONSearchResult results);
}
