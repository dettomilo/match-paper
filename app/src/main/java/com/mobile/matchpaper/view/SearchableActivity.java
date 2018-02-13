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

    private static final String DOWNLOAD_FINISHED_EVENT_NAME = "search_image_download_finished";
    private static List<ImageContainer> imageContainers = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).registerReceiver(searchImageDownloadFinished,
                new IntentFilter(DOWNLOAD_FINISHED_EVENT_NAME));

        editTextSearch = findViewById(R.id.et_search);
        imageButtonSearch = findViewById(R.id.ib_search);
        textViewSearchResults = findViewById(R.id.tv_search_results);
        gridView = findViewById(R.id.gv_search);
        gridView.setAdapter(new ImageAdapter(this));

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                // TODO Da mettere dentro all'if sotto quando funzionerà? lol boh
                RequestMaker.searchImagesByQuery(textView.getText().toString(), 1, 24, new SearchResultReceivedListener() {
                    @Override
                    public void callListenerEvent(JSONSearchResult results) {
                        searchResultsReceived(results);
                    }
                });

                if (i == EditorInfo.IME_ACTION_DONE) {
                    textViewSearchResults.setVisibility(View.VISIBLE);
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(textView.getWindowToken(), 0);

                    // TODO y u no work?!?

                    return true;
                }
                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(SearchableActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchResultsReceived(JSONSearchResult searchResult) {
        final List<ImageContainer> results = searchResult.getImageList(true);
        imageContainers.addAll(results);

        // Starts all the downloads for the drawableImages:
        for (ImageContainer imageToDownload:results){
            ImageVisualizer.downloadImageAndNotifyView(DOWNLOAD_FINISHED_EVENT_NAME, imageToDownload, ImageVisualizer.ResolutionQuality.PREVIEW);
        }
    }

    private BroadcastReceiver searchImageDownloadFinished = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO qua si notifica che l'immagine con id IMAGE ID (Dentro a imageContainers) ha finito il download quindi tocca dire alla view che ora può mostrarla

            // Get extra data included in the Intent
            String imageID = intent.getStringExtra("loadedImageID");
            // TODO per prendere l'immagine da mostrare tocca che fai un for per cercare dentro imageContainers quale ha lo stesso ID cercato (mi pare) :)
            Log.d("SEARCHDOWNLOAD", "Download completed for image: " + imageID);
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
            return mThumbIds.length;
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
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp
        };
    }
}
