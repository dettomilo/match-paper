package com.mobile.matchpaper.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.ImageVisualizer;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.controller.SearchResultReceivedListener;
import com.mobile.matchpaper.model.ImageContainer;
import com.mobile.matchpaper.model.JSONSearchResult;
import com.mobile.matchpaper.model.MatchPaperApp;
import com.mobile.matchpaper.model.UserPreferences;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private ImageButton imageButtonSearch;
    private TextView textViewSearchResults;
    private GridView gridView;
    private ProgressBar progressBar;
    private int imageWidth;

    private static final String DOWNLOAD_FINISHED_EVENT_NAME = "search_image_download_finished";
    protected static final String INTENT_STRING_CONTENT = "image_id";
    private static List<ImageContainer> imageContainers = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).registerReceiver(searchImageDownloadFinished,
                new IntentFilter(DOWNLOAD_FINISHED_EVENT_NAME));


        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        imageWidth = ((screenWidth / 2) / 100) * 95;


        editTextSearch = findViewById(R.id.et_search);
        imageButtonSearch = findViewById(R.id.ib_search);
        textViewSearchResults = findViewById(R.id.tv_search_results);
        gridView = findViewById(R.id.gv_search);
        ImageAdapter adapter = new ImageAdapter(this);
        gridView.setAdapter(adapter);
        progressBar = findViewById(R.id.pb_search);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE) {

                    textViewSearchResults.setVisibility(View.VISIBLE);
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(textView.getWindowToken(), 0);

                    progressBar.setVisibility(View.VISIBLE);
                    textViewSearchResults.setVisibility(View.GONE);

                    RequestMaker.searchImagesByQuery(textView.getText().toString(), 1, 24, new SearchResultReceivedListener() {
                        @Override
                        public void callListenerEvent(JSONSearchResult results) {
                            searchResultsReceived(results);
                        }
                    });

                    return true;
                }
                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String id = imageContainers.get(position).getImageID();

                showFullScreenImage(id);
            }
        });
    }

    private void showFullScreenImage(String text) {
        Intent intent = new Intent(this, DisplayImageActivity.class);
        intent.putExtra(INTENT_STRING_CONTENT, text);
        startActivity(intent);
    }

    public void searchResultsReceived(JSONSearchResult searchResult) {
        final List<ImageContainer> results = searchResult.getImageList(true);
        imageContainers.clear();
        imageContainers.addAll(results);

        if(results.isEmpty()) {
            textViewSearchResults.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        // Starts all the downloads for the drawableImages:
        for (ImageContainer imageToDownload:results){
            ImageVisualizer.downloadImageAndNotifyView(DOWNLOAD_FINISHED_EVENT_NAME, imageToDownload, ImageVisualizer.ResolutionQuality.MID);
        }
    }

    private BroadcastReceiver searchImageDownloadFinished = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            gridView.setAdapter(new ImageAdapter(getBaseContext()));
            gridView.invalidate();

            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).unregisterReceiver(searchImageDownloadFinished);
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imageContainers.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            if(imageContainers.size() > position) {
                imageView.setImageDrawable(imageContainers.get(position).getMidResDrawable());
            }

            return imageView;
        }
    }
}
