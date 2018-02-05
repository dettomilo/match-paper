package com.mobile.matchpaper.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.mobile.matchpaper.R;

public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        SearchView searchView = findViewById(R.id.search_view);
        //searchView.setQuery("Test", true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        //showSoftKeyboard(searchView);
        searchView.requestFocus();
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(
                        InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
