package com.mobile.matchpaper.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

/**
 * Created by emilio on 12/4/17.
 */

public class SwipeFragment extends Fragment{

    private Button likeButton;
    private Button dislikeButton;
    private ImageView currentPhoto;
    private ProgressBar progressBar;
    private int currentPhotoIndex;

    private static final int RESULTS_PER_PAGE = 5;
    private static final int NEW_REQUEST_THRESHOLD = 5;

    private static final String DOWNLOAD_FINISHED_EVENT_NAME = "swipe_image_download_finished";
    protected static final String INTENT_STRING_CONTENT = "image_id";

    private static List<ImageContainer> imageContainers = new LinkedList<>();

    private static Integer maxImagesFound = 0;
    private static Integer loadedImagesNumber = 0;

    private static Integer currentPage = 1;
    private static Integer lastRequestAtPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(swipeImageDownloadFinished,
                new IntentFilter(DOWNLOAD_FINISHED_EVENT_NAME));

        currentPhotoIndex = 0;

        RequestMaker.searchRandomImages(currentPage, 24, new SearchResultReceivedListener() {
            @Override
            public void callListenerEvent(JSONSearchResult results) {
                searchResultsReceived(results);
            }
        });

        LinearLayout ll = (LinearLayout) inflater.inflate(
                R.layout.item_swipe, container, false);

        return ll;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        likeButton = getView().findViewById(R.id.like_btn);
        dislikeButton = getView().findViewById(R.id.dislike_btn);
        currentPhoto = getView().findViewById(R.id.iv_current_photo);
        progressBar = getView().findViewById(R.id.progressbar_swipe);

        updateCurrentPhoto(currentPhoto);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO like photo
            }
        });

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO dislike photo
            }
        });
    }

    private void updateCurrentPhoto(ImageView iv) {
        if (!imageContainers.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            currentPhoto.setVisibility(View.VISIBLE);
            Log.d("ResultReceived", "SwipeFragment curr pos. " + currentPhotoIndex);
            Log.d("ResultReceived", "SwipeFragment img id " + imageContainers.get(currentPhotoIndex).getImageID());
            //iv.setImageDrawable(imageContainers.get(currentPhotoIndex).getMidResDrawable());
            iv.setImageResource(R.drawable.ic_android_black_24dp);
            //currentPhoto.invalidate();
        }
    }

    public void searchResultsReceived(JSONSearchResult searchResult) {
        Log.d("ResultReceived", "SwipeFragment Result Received");

        maxImagesFound = searchResult.getNumberOfImagesFound();
        final List<ImageContainer> results = searchResult.getImageList(true);
        imageContainers.addAll(results);

        Log.d("ResultReceived", "SwipeFragment results: " + results.size());

        // Starts all the downloads for the drawableImages:
        for (ImageContainer imageToDownload:results){
            ImageVisualizer.downloadImageAndNotifyView(DOWNLOAD_FINISHED_EVENT_NAME, imageToDownload, ImageVisualizer.ResolutionQuality.PREVIEW);
        }

        updateCurrentPhoto(currentPhoto);
    }

    private void notifyViewOfNewLoadedImage() {
        updateCurrentPhoto(currentPhoto);
        loadedImagesNumber++;
    }

    private BroadcastReceiver swipeImageDownloadFinished = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            notifyViewOfNewLoadedImage();
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).unregisterReceiver(swipeImageDownloadFinished);
        super.onDestroy();

        try {
            UserPreferences.GetInstance().SavePreferences();
            Log.d("FILESAVE", "on exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            UserPreferences.GetInstance().SavePreferences();
            Log.d("FILESAVE", "on exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
