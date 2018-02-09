package com.mobile.matchpaper.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.ImageVisualizer;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.controller.SearchResultReceivedListener;
import com.mobile.matchpaper.model.ImageContainer;
import com.mobile.matchpaper.model.JSONSearchResult;
import com.mobile.matchpaper.model.MatchPaperApp;
import com.mobile.matchpaper.model.UserPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;
    private FloatingActionButton fab;
    private ArrayList<ImageContainer> tempList = new ArrayList<>();
    public static final String DOWNLOAD_FINISHED_EVENT_NAME = "preferences_download_completed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).registerReceiver(preferencesDownloadCompleted,
                new IntentFilter(DOWNLOAD_FINISHED_EVENT_NAME));

        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        // This line set the open tab to be the second one
        tabs.getTabAt(1).select();
        fab = findViewById(R.id.fab);
        fab.animate().translationY(fab.getHeight());

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 1) {
                    fab.animate()
                            .alpha(1.0f)
                            .translationY(0);
                } else {
                    /*fab.animate()
                            .translationY(fab.getHeight())
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    fab.setVisibility(View.INVISIBLE);
                                }
                            });*/
                    fab.animate()
                            .translationY(fab.getHeight())
                            .alpha(0.0f);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Attach a listener to fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Snackbar.make(v, "Hello Snackbar!",
                        Snackbar.LENGTH_LONG).show();*/
                showSearchActivity(v);
            }
        });

        try {
            // Loads the preferences and then downloads the preferenced images
            ArrayList<String> imageIDs = UserPreferences.GetInstance().LoadPreferences();

            String concatIDs = "";

            for (String id : imageIDs){
                concatIDs += id + ',';
            }

            if (concatIDs == "") {
                concatIDs = "NO_ID";
            }

            RequestMaker.searchImagesByID(concatIDs, new SearchResultReceivedListener() {
                @Override
                public void callListenerEvent(JSONSearchResult results) {
                    preferencesResultsReceived(results);
                }
            });

            Log.d("FILESAVE", "on resume");
        } catch (IOException e) {
            Log.d("Error loading file", "No savefile");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showSearchActivity(View view) {
        Intent intent = new Intent(this, SearchableActivity.class);
        startActivity(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new SwipeFragment(), "Swipe");
        adapter.addFragment(new ListFragment(), "List");
        adapter.addFragment(new FavoritesFragment(), "Favorites");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            UserPreferences.GetInstance().SavePreferences();
            Log.d("FILESAVE", "on exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            // Loads the preferences and then downloads the preferenced images
            ArrayList<String> imageIDs = UserPreferences.GetInstance().LoadPreferences();

            String concatIDs = "";

            for (String id : imageIDs){
                concatIDs += id + ',';
            }

            if (concatIDs == "") {
                concatIDs = "NO_ID";
            }

            RequestMaker.searchImagesByID(concatIDs, new SearchResultReceivedListener() {
                @Override
                public void callListenerEvent(JSONSearchResult results) {
                    preferencesResultsReceived(results);
                }
            });

            Log.d("FILESAVE", "on resume");
        } catch (IOException e) {
            Log.d("Error loading file", "No savefile");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void preferencesResultsReceived(JSONSearchResult results){
        tempList = results.getImageList(false);

        for (ImageContainer imageToDownload : tempList){
            ImageVisualizer.downloadImageAndNotifyView(DOWNLOAD_FINISHED_EVENT_NAME, imageToDownload, ImageVisualizer.ResolutionQuality.PREVIEW);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).unregisterReceiver(preferencesDownloadCompleted);

        try {
            UserPreferences.GetInstance().SavePreferences();
            Log.d("FILESAVE", "on exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver preferencesDownloadCompleted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            //String imageID = intent.getStringExtra("loadedImageID");
            UserPreferences.GetInstance().SetLikedImages(tempList);
        }
    };
}
