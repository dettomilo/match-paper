package com.mobile.matchpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.matchpaper.utilities.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_search_results_json);
    }

    private void makeSearchQuery() throws MalformedURLException {
        String query = mSearchBoxEditText.getText().toString();
        /* TODO the following code needs some polish
            1) remove the actual url and make it into a constant
            2) right now, if in the search bar you put two words separated by a space, the url won't
            return any result. Fix that putting a + between the words (I highly recommend you use the
            NetworkUtils class)
         */
        URL searchUrl = new URL("https://pixabay.com/api/?key=7232093-5c2e905e26143573763e287dc&q=" + query + "&image_type=photo&pretty=true");
        mUrlDisplayTextView.setText(searchUrl.toString());
        // COMPLETED (4) Create a new queryTask and call its execute method, passing in the url to query
        new queryTask().execute(searchUrl);
    }

    public class queryTask extends AsyncTask<URL, Void, String> {

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
                mSearchResultsTextView.setText(searchResults);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            try {
                makeSearchQuery();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* TODO
        of course, the result for now are raw json. Parsing!
     */
}
