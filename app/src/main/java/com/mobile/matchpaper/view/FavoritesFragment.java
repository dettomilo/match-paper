package com.mobile.matchpaper.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mobile.matchpaper.R;
import com.mobile.matchpaper.model.ImageContainer;
import com.mobile.matchpaper.model.UserPreferences;

import java.util.LinkedList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    protected static final String INTENT_STRING_CONTENT = "image_id";

    private static List<ImageContainer> imageContainers = new LinkedList<>();
    private static ContentAdapter adapter;
    private static Integer currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        imageContainers = UserPreferences.GetInstance().GetLikedImages();

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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return recyclerView;
    }

    private String GetConcatenatedLikedIDs(){
        String concatIDs = "";

        for (ImageContainer img : UserPreferences.GetInstance().GetLikedImages()){
            concatIDs += img.getImageID() + ',';
        }

        if (concatIDs == "") {
            concatIDs = "0";
        }

        return concatIDs;
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
            Log.d("Current position", "Position requested is: " + position + " total images: " + getItemCount() + " page: " + currentPage);

            if (!imageContainers.isEmpty()) {

                Drawable img = imageContainers.get(position).getPreviewDrawable();
                holder.picture.setImageDrawable(img);

                holder.picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = imageContainers.get(position).getImageID();
                        //String msg = "Clicked image with ID: ";
                        Log.d("ClickEvent", "Position: " + position + " " + imageContainers.get(position).getMidResURL());
                        showFullScreenImage(v, id);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return imageContainers.size();
        }
    }

    private void showFullScreenImage(View view, String text) {
        Intent intent = new Intent(this.getActivity(), DisplayImageActivity.class);
        intent.putExtra(INTENT_STRING_CONTENT, text);
        startActivity(intent);
    }

    public static void notifyViewForDatasetChange() {
        imageContainers = UserPreferences.GetInstance().GetLikedImages();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        UserPreferences.GetInstance().SaveStatusToDisk();
    }
}
