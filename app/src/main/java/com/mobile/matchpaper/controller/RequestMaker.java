package com.mobile.matchpaper.controller;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.util.Log;

import com.mobile.matchpaper.model.JSONParser;
import com.mobile.matchpaper.model.JSONSearchResult;
import com.mobile.matchpaper.view.ListFragment;
import com.mobile.matchpaper.view.MainActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
    public static void searchImagesByQuery(String simpleQuery, Integer pageNumber, Integer resultsPerPage) {
        String requestURL = buildSearchURL(
                simpleQuery,
                "",
                ResultsOrder.POPULAR,
                pageNumber.toString(),
                resultsPerPage.toString()
        ).toString();

        makeRequest(requestURL, QueryType.QUERY_SEARCH);
    }

    /**
     *  Searches for images by image IDs
     * @param imagesIDs Image IDs (If multiple, separate them by comma. Ex: ID1,ID2,...)
     * @param pageNumber The result page number.
     * @param resultsPerPage The results per page.
     */
    public static void searchImagesByID(String imagesIDs, Integer pageNumber, Integer resultsPerPage) {
        String requestURL = buildSearchURL(
                "",
                imagesIDs,
                ResultsOrder.POPULAR,
                pageNumber.toString(),
                resultsPerPage.toString()
        ).toString();

        makeRequest(requestURL, QueryType.ID_SEARCH);
    }

    /**
     * Gets a list of random images ordered by the LATEST uploaded.
     * @param pageNumber
     * @param resultsPerPage
     */
    public static void searchRandomImages(Integer pageNumber, Integer resultsPerPage) {
        String requestURL = buildSearchURL(
                "",
                "",
                ResultsOrder.LATEST,
                pageNumber.toString(),
                resultsPerPage.toString()
        ).toString();

        makeRequest(requestURL, QueryType.RANDOM_IMAGE);
    }

    /**
     * Makes a request using a request URL.
     * @param requestURL The request URL.
     * @param type The kind of request.
     */
    private static void makeRequest(String requestURL, QueryType type) {

        URL searchUrl = null;
        try {
            searchUrl = new URL(requestURL);
            new QueryTaskAsync().execute(new QueryContainer(searchUrl, type));
        } catch (MalformedURLException e) {
            Log.e("Malformed URL", "Request of type: " + type.toString() + " not correct with URL: " + requestURL);
            e.printStackTrace();
        }
    }

    private static class QueryTaskAsync extends AsyncTask<QueryContainer, Void, String> {
        private QueryType type;

        @Override
        protected String doInBackground(QueryContainer ...params) {

            URL searchUrl = params[0].getQueryUrl();
            type = params[0].getType();

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

                switch (type){
                    case QUERY_SEARCH:
                        MainActivity.searchResultsReceived(searchResultsContainer);
                        break;
                    case ID_SEARCH:
                        // TODO Call appropriate view method for ID_SEARCH (Like visualize a previously liked image stored by ID)
                        break;
                    case HQ_ID_SEARCH:
                        // TODO Call appropriate view method for HQ_ID_SEARCH (Like to download an image in HQ)
                        break;
                    case RANDOM_IMAGE:
                        // TODO Call appropriate view method for RANDOM_IMAGE (Like to fill the random image Like/Dislike page)
                        ListFragment.searchResultsReceived(searchResultsContainer);
                        break;
                }

            }
        }
    }

    private enum QueryType {
        QUERY_SEARCH,
        RANDOM_IMAGE,
        ID_SEARCH,
        HQ_ID_SEARCH;
    }

    private static class QueryContainer {

        private QueryType type;
        private URL queryUrl;

        public QueryContainer(URL queryUrl, QueryType type) {
            this.type = type;
            this.queryUrl = queryUrl;
        }

        public QueryType getType() {
            return type;
        }

        public URL getQueryUrl() {
            return queryUrl;
        }
    }
}
