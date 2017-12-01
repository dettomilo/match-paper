package com.mobile.matchpaper.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to parse JSON
 */

public class JSONParser {
    /**
     * This method parses a JSON search result and returns a JSONSearchResult object that contains
     * information about the search result.
     *
     * @param jsonResult The json string content.
     * @return The object containing info on the search results.
     */
    static public JSONSearchResult parseJSONSearchResult(String jsonResult){

        try {
            JSONObject jObject = new JSONObject(jsonResult.replaceAll("\n", "\\n"));

            return new JSONSearchResult(jObject.getInt("totalHits"), jObject.getJSONArray("hits"));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Json parse error", "Error parsing JSON");
        }

        return new JSONSearchResult(0, new JSONArray());
    }
}
