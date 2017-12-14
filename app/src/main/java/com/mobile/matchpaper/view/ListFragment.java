package com.mobile.matchpaper.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.ImageVisualizer;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.model.ImageContainer;
import com.mobile.matchpaper.model.JSONSearchResult;
import com.mobile.matchpaper.model.MatchPaperApp;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by emilio on 12/4/17.
 */

public class ListFragment extends Fragment{

    private static final int RESULTS_PER_PAGE = 24;
    private static final int NEW_REQUEST_THRESHOLD = 20;

    private static final String DOWNLOAD_FINISHED_EVENT_NAME = "list_image_download_finished";

    private static List<Drawable> drawableImages = new LinkedList<>();
    private static List<ImageContainer> imageContainers = new LinkedList<>();

    private static Integer maxImagesFound = 0;
    private static ContentAdapter adapter;

    private static Integer currentPage = 1;
    private static Integer lastRequestAtPosition = 0;

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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(getActivity(),"Bottom reached",Toast.LENGTH_LONG).show();
                }
            }
        });

        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView picture;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            picture = itemView.findViewById(R.id.tile_picture);
        }

        @Override
        public void onClick(View view) {
            Log.d("Image tapped","Image tapped");
            Toast.makeText(view.getContext(),"Image tapped",Toast.LENGTH_LONG).show();
            Snackbar.make(view, "Action is pressed", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        public ContentAdapter(Context context) {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Log.d("Current position", "Position requested is: " + position + " total images: " + getItemCount() + " page: " + currentPage);

            Drawable img = drawableImages.get(position);
            holder.picture.setImageDrawable(img);

            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ClickEvent", "Clicked image with ID: " + imageContainers.get(position).getImageID());
                    showFullScreenImage(v);
                }
            });

            final Integer currentMaxPosition = (drawableImages.size() - 1 - NEW_REQUEST_THRESHOLD);

            if (position ==  currentMaxPosition && getItemCount() <= maxImagesFound && lastRequestAtPosition != position){
                //This is to avoid duplicate requests on the same page position:
                lastRequestAtPosition = position;

                currentPage++;
                RequestMaker.searchRandomImages(currentPage, RESULTS_PER_PAGE);
            }
        }

        @Override
        public int getItemCount() {
            return drawableImages.size();
        }
    }

    public void showFullScreenImage(View view) {
        Intent intent = new Intent(this.getActivity(), DisplayImage.class);

        startActivity(intent);
    }

    public static void searchResultsReceived(JSONSearchResult searchResult) {
        maxImagesFound = searchResult.getNumberOfImagesFound();
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
