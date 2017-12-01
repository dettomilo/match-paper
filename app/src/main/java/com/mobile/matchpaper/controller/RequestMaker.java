package com.mobile.matchpaper.controller;

import android.os.AsyncTask;

import com.mobile.matchpaper.utilities.NetworkUtils;
import com.mobile.matchpaper.view.MainActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class makes HTTP requests from PixaBay
 */

public class RequestMaker {

    private final String SEARCH_URL = "https://pixabay.com/api/?key=7232093-5c2e905e26143573763e287dc&q=QUERY_VALUES&image_type=photo&pretty=true";

    public static void searchImages(String simpleQuery) throws MalformedURLException {


        URL searchUrl = new URL(getFormattedURL(simpleQuery));
        // COMPLETED (4) Create a new queryTask and call its execute method, passing in the url to query
        new queryTask().execute(searchUrl);
    }

    /*
        TODO format query to be URL ready use NETWORK UTILS maybeeeeeeee :)
    */

    private static String getFormattedURL(String searchQuery){
        return searchQuery;
    }

    private static class queryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        /*
            TODO Elaborate the search results.
         */
        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                MainActivity.searchResultsReceived();
            }
        }
    }
}