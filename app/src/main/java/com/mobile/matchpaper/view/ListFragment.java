package com.mobile.matchpaper.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.ImageVisualizer;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.controller.SearchResultReceivedListener;
import com.mobile.matchpaper.model.ImageContainer;
import com.mobile.matchpaper.model.JSONSearchResult;
import com.mobile.matchpaper.model.MatchPaperApp;
import com.mobile.matchpaper.model.UserPreferences;

import java.util.LinkedList;
import java.util.List;

public class ListFragment extends Fragment{

    private static final int RESULTS_PER_PAGE = 5;
    private static final int NEW_REQUEST_THRESHOLD = 5;

    private static final String DOWNLOAD_FINISHED_EVENT_NAME = "list_image_download_finished";
    protected static final String INTENT_STRING_CONTENT = "image_id";

    private static List<ImageContainer> imageContainers = new LinkedList<>();

    private static Integer maxImagesFound = 0;
    private static ContentAdapter adapter;
    private static Integer loadedImagesNumber = 0;

    private static Integer currentPage = 1;
    private static Integer lastRequestAtPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(homeImageDownloadFinished,
                new IntentFilter(DOWNLOAD_FINISHED_EVENT_NAME));

        QueryForMostLikedTags();

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view,
                container,
                false);

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
                    Toast.makeText(getActivity(),"Loading new images...",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return recyclerView;
    }

    private void QueryForMostLikedTags(){

        int maxTags = 15;
        int currTags = 0;

        if (!UserPreferences.GetInstance().GetMostLikedTags().keySet().isEmpty()) {
            Log.d("NOTEMPTY", "SADASDASDAS");

            for (String tag : UserPreferences.GetInstance().GetMostLikedTags().keySet()) {
                if (currTags < maxTags){
                    // Mixing things up
                    RequestMaker.searchImagesByQuery(tag, currentPage, 4, new SearchResultReceivedListener() {
                        @Override
                        public void callListenerEvent(JSONSearchResult results) {
                            searchResultsReceived(results);
                        }
                    });
                    currTags++;
                } else {
                    break;
                }
            }
        } else {
            RequestMaker.searchRandomImages(currentPage, 24, new SearchResultReceivedListener() {
                @Override
                public void callListenerEvent(JSONSearchResult results) {
                    searchResultsReceived(results);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            picture = itemView.findViewById(R.id.tile_picture);
        }
    }

    /**
     * Adapter to display recycler view.
     */
     private class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        public ContentAdapter(Context context) {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            //Log.d("Current position", "Position requested is: " + position + " total images: " + getItemCount() + " page: " + currentPage);

            Drawable img = imageContainers.get(position).getPreviewDrawable();
            holder.picture.setImageDrawable(img);

            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = imageContainers.get(position).getImageID();
                    //String msg = "Clicked image with ID: ";
                    Log.d("ClickEvent", "Position: " + position + " " + imageContainers.get(position).getMidResURL());
                    showFullScreenImage(v, id, imageContainers.get(position));
                }
            });

            final Integer currentMaxPosition = (imageContainers.size() - 1 - NEW_REQUEST_THRESHOLD);

            if (position ==  currentMaxPosition && getItemCount() <= maxImagesFound && lastRequestAtPosition != position){
                //This is to avoid duplicate requests on the same page position:
                lastRequestAtPosition = position;

                currentPage++;

                QueryForMostLikedTags();
            }
        }

        @Override
        public int getItemCount() {
            return loadedImagesNumber;
        }
    }

    private void showFullScreenImage(View view, String text, ImageContainer imageInHome) {
        Intent intent = new Intent(this.getActivity(), DisplayImageActivity.class);
        intent.putExtra(INTENT_STRING_CONTENT, text);
        startActivity(intent);
    }

    public void searchResultsReceived(JSONSearchResult searchResult) {
        Log.d("ResultReceived", "ListFragment Result Received");

        maxImagesFound = searchResult.getNumberOfImagesFound();
        final List<ImageContainer> results = searchResult.getImageList(true);
        imageContainers.addAll(results);

        Log.d("ResultReceived", "ListFragment results: " + results.size());

        // Starts all the downloads for the drawableImages:
        for (ImageContainer imageToDownload:results){
            ImageVisualizer.downloadImageAndNotifyView(DOWNLOAD_FINISHED_EVENT_NAME, imageToDownload, ImageVisualizer.ResolutionQuality.PREVIEW);
        }

        adapter.notifyDataSetChanged();
    }

    public void notifyViewOfNewLoadedImage() {
        adapter.notifyDataSetChanged();
        loadedImagesNumber++;
    }

    public static ImageContainer getImageFromHomeByID(String imageID){

         for (ImageContainer img: imageContainers){
             if (img.getImageID().equals(imageID)){
                 return img;
             }
         }
         return null;
    }

    private BroadcastReceiver homeImageDownloadFinished = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            notifyViewOfNewLoadedImage();
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(MatchPaperApp.getContext()).unregisterReceiver(homeImageDownloadFinished);
        super.onDestroy();
    }
}
