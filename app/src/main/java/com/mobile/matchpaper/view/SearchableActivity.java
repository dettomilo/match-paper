package com.mobile.matchpaper.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.matchpaper.R;

public class SearchableActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private ImageButton imageButtonSearch;
    private TextView textViewSearchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        editTextSearch = findViewById(R.id.et_search);
        imageButtonSearch = findViewById(R.id.ib_search);
        textViewSearchResults = findViewById(R.id.tv_search_results);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    textViewSearchResults.setVisibility(View.VISIBLE);
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    //TODO implement grid of images
}
