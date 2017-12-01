package com.mobile.matchpaper.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.mobile.matchpaper.model.JSONParser;
import com.mobile.matchpaper.utilities.NetworkUtils;
import com.mobile.matchpaper.view.MainActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class makes HTTP requests from PixaBay
 */

public class RequestMaker {

    public static void searchImagesByQuery(String simpleQuery) throws MalformedURLException {
        String requestURL = NetworkUtils.buildSearchURL(simpleQuery, "", NetworkUtils.ResultsOrder.POPULAR, "1", "20").toString();
        Log.d("Request URL", requestURL);
        URL searchUrl = new URL(requestURL);
        new QueryTaskAsync().execute(searchUrl);
    }

    private static class QueryTaskAsync extends AsyncTask<URL, Void, String> {

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

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                MainActivity.searchResultsReceived(JSONParser.parseJSONSearchResult(searchResults));
            }
        }
    }
}
