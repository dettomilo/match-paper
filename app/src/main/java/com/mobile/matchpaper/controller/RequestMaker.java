package com.mobile.matchpaper.controller;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.util.Log;

import com.mobile.matchpaper.model.JSONParser;
import com.mobile.matchpaper.model.JSONSearchResult;
import com.mobile.matchpaper.view.FavoritesFragment;
import com.mobile.matchpaper.view.ListFragment;
import com.mobile.matchpaper.view.MainActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import static com.mobile.matchpaper.utilities.NetworkUtils.*;

/**
 * This class makes HTTP requests from PixaBay
 */

public class RequestMaker {

    /**
     * Searches image by a query.
     * @param simpleQuery The search box content.
     * @param pageNumber The result page number.
     * @param resultsPerPage The results per page.
     */
    public static void searchImagesByQuery(String simpleQuery, Integer pageNumber, Integer resultsPerPage, SearchResultReceivedListener downloadFinishedListener) {
        String requestURL = buildSearchURL(
                simpleQuery,
                "",
                ResultsOrder.POPULAR,
                pageNumber.toString(),
                resultsPerPage.toString()
        ).toString();

        makeRequest(requestURL, downloadFinishedListener);
    }

    /**
     *  Searches for images by image IDs
     * @param imagesIDs Image IDs (If multiple, separate them by comma. Ex: ID1,ID2,...)
     */
    public static void searchImagesByID(String imagesIDs, SearchResultReceivedListener downloadFinishedListener) {
        String requestURL = buildSearchURL(
                "",
                imagesIDs,
                ResultsOrder.POPULAR,
                "",
                ""
        ).toString();

        makeRequest(requestURL, downloadFinishedListener);
    }

    /**
     *  Searches for images by image IDs
     * @param imagesIDs Image IDs (If multiple, separate them by comma. Ex: ID1,ID2,...)
     */
    public static void searchHDImagesByID(String imagesIDs, SearchResultReceivedListener downloadFinishedListener) {
        String requestURL = buildHDSearchURL(
                "",
                imagesIDs,
                ResultsOrder.POPULAR,
                "",
                ""
        ).toString();

        makeRequest(requestURL, downloadFinishedListener);
    }

    /**
     * Gets a list of random images ordered by the LATEST uploaded.
     * @param pageNumber
     * @param resultsPerPage
     */
    public static void searchRandomImages(Integer pageNumber, Integer resultsPerPage, SearchResultReceivedListener downloadFinishedListener) {
        String requestURL = buildSearchURL(
                "",
                "",
                ResultsOrder.LATEST,
                pageNumber.toString(),
                resultsPerPage.toString()
        ).toString();

        makeRequest(requestURL, downloadFinishedListener);
    }

    /**
     * Makes a request using a request URL.
     * @param requestURL The request URL.
     * @param listener The listener of request.
     */
    private static void makeRequest(String requestURL, SearchResultReceivedListener listener) {

        URL searchUrl = null;
        try {
            searchUrl = new URL(requestURL);
            new QueryTaskAsync().execute(new QueryContainer(searchUrl, listener));
        } catch (MalformedURLException e) {
            Log.e("Malformed URL", "Request not correct with URL: " + requestURL);
            e.printStackTrace();
        }
    }

    private static class QueryTaskAsync extends AsyncTask<QueryContainer, Void, String> {
        private SearchResultReceivedListener listener;

        @Override
        protected String doInBackground(QueryContainer ...params) {

            URL searchUrl = params[0].getQueryUrl();
            listener = params[0].getListener();

            String searchResults = null;
            try {
                searchResults = getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {

                // When the query ends, check what kind of query it was, and call the appropriate method.
                JSONSearchResult searchResultsContainer = JSONParser.parseJSONSearchResult(searchResults);
                listener.callListenerEvent(searchResultsContainer);
            }
        }
    }

    private static class QueryContainer {

        private SearchResultReceivedListener listener;
        private URL queryUrl;

        public QueryContainer(URL queryUrl, SearchResultReceivedListener listener) {
            this.listener = listener;
            this.queryUrl = queryUrl;
        }

        public SearchResultReceivedListener getListener() {
            return listener;
        }

        public URL getQueryUrl() {
            return queryUrl;
        }
    }
}
