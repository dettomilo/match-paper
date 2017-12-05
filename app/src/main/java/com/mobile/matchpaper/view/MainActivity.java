package com.mobile.matchpaper.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.model.MatchPaperApp;
import com.mobile.matchpaper.model.JSONSearchResult;
import com.mobile.matchpaper.model.UserPreferences;

import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        /**
         * TODO Remove this line after testing:
         */
        RequestMaker.searchRandomImages(1, 5);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ListContentFragment(), "List");
        adapter.addFragment(new TileContentFragment(), "Tile");
        adapter.addFragment(new CardContentFragment(), "Card");
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
        Log.d("Debug image", "" + searchResult.getImageList(1).get(0).getMidResURL());

        //UserPreferences.GetInstance().LikeImage(searchResult.getImageList().get(0));
        //UserPreferences.GetInstance().UnlikeImage(searchResult.getImageList().get(0).getImageID());
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
