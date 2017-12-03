package com.mobile.matchpaper.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.model.JSONSearchResult;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            RequestMaker.searchImagesByQuery(mSearchBoxEditText.getText().toString(), 1, 20);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
        This method is called from the controller when the search results are completed.
        TODO complete function to update the view with the results
     */
    public static void searchResultsReceived(JSONSearchResult searchResult){
        Log.d("Results received","Total found images: " + searchResult.getTotalImagesFound());

        // Just for debug :)
        // searchResult.getImageList() is also available to return ALL images
        // use .getDrawable---() to return a DRAWABLE that can be displayed.
        Log.d("Debug image", "" + searchResult.getImageList(1).get(0).getMidResURL());
    }
}
