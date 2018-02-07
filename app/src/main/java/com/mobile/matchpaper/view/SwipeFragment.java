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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.model.ImageContainer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by emilio on 12/4/17.
 */

public class SwipeFragment extends Fragment{

    private Button likeButton;
    private Button dislikeButton;
    private ImageView currentPhoto;

    private static List<ImageContainer> imageContainers = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(DOWNLOAD_FINISHED_EVENT_NAME));

        RequestMaker.searchRandomImages(currentPage, RESULTS_PER_PAGE);*/

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

    /*public void notifyViewOfNewLoadedImage(String imageID) {
        adapter.notifyDataSetChanged();
        loadedImagesNumber++;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("loadedImageID");
            notifyViewOfNewLoadedImage(message);
        }
    };*/
}
