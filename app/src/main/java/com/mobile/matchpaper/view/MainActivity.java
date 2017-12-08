package com.mobile.matchpaper.view;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.model.JSONSearchResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //TODO Delete useless views
    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabs;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Snackbar.make(v, "Hello Snackbar!",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        /**
         * TODO Remove this line after testing:
         */
        RequestMaker.searchRandomImages(1, 5);
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

    /**
        This method is called from the controller when the search results are completed.
        TODO complete function to update the view with the results
     */
    public static void searchResultsReceived(JSONSearchResult searchResult){
        /**
         * TODO Remove this content when not needed (It's just for demo)
         */
        Log.d("Results received","Total found images: " + searchResult.getTotalImagesFound());

        // Just for debug :)
        // searchResult.getImageList() is also available to return ALL images
        // use .getDrawable---() to return a DRAWABLE that can be displayed.
        Log.d("Debug image", "" + searchResult.getImageList(1, false).get(0).getMidResURL());

        //UserPreferences.GetInstance().LikeImage(searchResult.getImageList(false).get(0));
        //UserPreferences.GetInstance().UnlikeImage(searchResult.getImageList(false).get(0).getImageID());
        /*
        try {
            UserPreferences.GetInstance().SavePreferences();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserPreferences.GetInstance().LikeImage(searchResult.getImageList().get(1));

        Log.d("Liked Images1", "Count: " + UserPreferences.GetInstance().GetLikedImages().size());
        Log.d("Liked Tags1", "" + UserPreferences.GetInstance().GetMostLikedTags().toString());

        try {
            UserPreferences.GetInstance().LoadPreferences();

            Log.d("Liked Images after load", "Count: " + UserPreferences.GetInstance().GetLikedImages().size());
            Log.d("Liked Tags after load", "" + UserPreferences.GetInstance().GetMostLikedTags().toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */
    }
}
