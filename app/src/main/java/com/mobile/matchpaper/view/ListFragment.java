package com.mobile.matchpaper.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.ImageVisualizer;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.model.ImageContainer;
import com.mobile.matchpaper.model.JSONSearchResult;
import com.mobile.matchpaper.model.MatchPaperApp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by emilio on 12/4/17.
 */

public class ListFragment extends Fragment{

    private static final int RESULTS_PER_PAGE = 6;
    private static final int NEW_REQUEST_THRESHOLD = 3;
    private static final String DOWNLOAD_FINISHED_EVENT_NAME = "list_image_download_finished";

    private static List<Drawable> drawableImages = new LinkedList<>();
    private static List<ImageContainer> imageContainers = new LinkedList<>();

    //private static Integer numOfImagesFound;
    private static ContentAdapter adapter;

    private static Integer currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(DOWNLOAD_FINISHED_EVENT_NAME));


        RequestMaker.searchRandomImages(currentPage, RESULTS_PER_PAGE);

        LinearLayout ll = (LinearLayout) inflater.inflate(
                R.layout.recycler_view,
                container,
                false);

        RecyclerView recyclerView = ll.findViewById(R.id.my_recycler_view);
        if(recyclerView.getParent() != null)
            ((ViewGroup)recyclerView.getParent()).removeView(recyclerView);

        /*ViewPager viewPager = (ViewPager) container;
        Integer i = ((ViewPager) container).getCurrentItem();
        Toast.makeText(getActivity(), i.toString(),
                Toast.LENGTH_LONG).show();*/

        adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        // Set padding for Tiles
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        //public TextView name;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
            //name = (TextView) itemView.findViewById(R.id.tile_title);
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        public ContentAdapter(Context context) {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.d("Current position", "Position requested is: " + position + " total images: " + getItemCount() + " page: " + currentPage);

            final Integer currentMaxPosition = (drawableImages.size() - 1 - NEW_REQUEST_THRESHOLD);

            if (position ==  currentMaxPosition){
                currentPage++;
                RequestMaker.searchRandomImages(currentPage, RESULTS_PER_PAGE);
            }

            Drawable img = drawableImages.get(position);
            holder.picture.setImageDrawable(img);
        }

        @Override
        public int getItemCount() {
            return drawableImages.size();
        }
    }

    public static void searchResultsReceived(JSONSearchResult searchResult) {
        //numOfImagesFound += searchResult.getNumberOfImagesFound();
        final List<ImageContainer> results = searchResult.getImageList(true);
        imageContainers.addAll(results);

        // Starts all the downloads for the drawableImages:
        for (ImageContainer imageToDownload:results){
            ImageVisualizer.downloadImageAndNotifyView(DOWNLOAD_FINISHED_EVENT_NAME, imageToDownload, ImageVisualizer.ResolutionQuality.PREVIEW);
        }

        adapter.notifyDataSetChanged();
    }

    public void addDrawablePreviewToList(String imageID) {
        for (ImageContainer img:imageContainers) {
            if (img.getImageID().equals(imageID) && !drawableImages.contains(img.getPreviewDrawable())) {
                //Log.d("FOUND", "ImageID: " + img.getImageID());
                drawableImages.add(img.getPreviewDrawable());
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("loadedImageID");
            Log.d("DownloadCompleted", "ImageID: " + message);
            addDrawablePreviewToList(message);
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
