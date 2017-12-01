/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobile.matchpaper.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    private static final String SEARCH_BASE_URL = "https://pixabay.com/api/?key=7224233-e95ab7aee14015f4bc4fede93";

    public enum ResultsOrder {
        POPULAR ("popular"),
        LATEST ("latest");

        private final String order;

        ResultsOrder (String order){
            this.order = order;
        }

        public String toString(){
                return this.order;
        }
    }

    final static String PARAM_SEARCH_QUERY = "q";
    final static String PARAM_SEARCH_ID = "id";
    final static String PARAM_ORDER = "order";
    final static String PARAM_PAGE_NUM = "page";
    final static String PARAM_RESULTS_PER_PAGE = "per_page";

    /**
     * Builds the URL used to query PixaBay.
     *
     * @param searchQuery The search query [Leave empty if not used].
     * @param searchID The ID of the image to be searched [Leave empty if not used].
     * @param searchOrder The searching order of the images [Default: popular].
     * @param pageNum The page number to be searched [Leave empty to use Default: 1].
     * @param resultsPerPage The number of results per page [Leave empty to use Default: 20].
     * @return The URL to use to query the PixaBay.
     */
    public static URL buildSearchURL(String searchQuery, String searchID, ResultsOrder searchOrder, String pageNum, String resultsPerPage) {

        Uri.Builder uriBuilderTemp = Uri.parse(SEARCH_BASE_URL).buildUpon();

        if (searchQuery.length() > 0) {
            uriBuilderTemp.appendQueryParameter(PARAM_SEARCH_QUERY, searchQuery);
        }
        if (searchID.length() > 0) {
            uriBuilderTemp.appendQueryParameter(PARAM_SEARCH_ID, searchID);
        }

        uriBuilderTemp.appendQueryParameter(PARAM_ORDER, searchOrder.toString());

        if (pageNum.length() > 0) {
            uriBuilderTemp.appendQueryParameter(PARAM_PAGE_NUM, pageNum);
        }
        if (resultsPerPage.length() > 0) {
            uriBuilderTemp.appendQueryParameter(PARAM_RESULTS_PER_PAGE, resultsPerPage);
        }
        uriBuilderTemp.appendQueryParameter("image_type", "photo");

        Uri finalUrl = uriBuilderTemp.build();

        URL url = null;
        try {
            url = new URL(finalUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}