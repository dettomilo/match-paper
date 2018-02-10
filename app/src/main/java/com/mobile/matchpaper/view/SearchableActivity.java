package com.mobile.matchpaper.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
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

public class SearchableActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private ImageButton imageButtonSearch;
    private TextView textViewSearchResults;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        editTextSearch = findViewById(R.id.et_search);
        imageButtonSearch = findViewById(R.id.ib_search);
        textViewSearchResults = findViewById(R.id.tv_search_results);
        gridView = findViewById(R.id.gv_search);
        gridView.setAdapter(new ImageAdapter(this));

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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(SearchableActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
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
