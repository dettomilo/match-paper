package com.mobile.matchpaper.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobile.matchpaper.R;
import com.mobile.matchpaper.controller.RequestMaker;
import com.mobile.matchpaper.model.ImageContainer;
import com.mobile.matchpaper.model.JSONSearchResult;

import java.util.ArrayList;

/**
 * Created by emilio on 12/4/17.
 */

public class ListFragment extends Fragment{

    static ArrayList<ImageContainer> images = new ArrayList<>();
    static Integer numOfImagesFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RequestMaker.searchRandomImages(1, 20);
        images = new ArrayList<>();
        numOfImagesFound = 0;

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

        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        // Set padding for Tiles
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
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

        private Drawable[] drawablePreviews;
        static ArrayList<Drawable> drawableArrayList = new ArrayList<>();
        //private final Drawable[] mPlacePictures;
        public ContentAdapter(Context context) {

            for (ImageContainer i : images) {
                drawableArrayList.add(i.getDrawablePreview());
            }

            drawablePreviews = drawableArrayList.toArray(new Drawable[drawableArrayList.size()]);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.d("Current position", "Position requested is: " + position);
            ImageContainer img = images.get(position);
            holder.picture.setImageDrawable(img.getDrawablePreview());
        }

        @Override
        public int getItemCount() {
            return images.size();
        }
    }

    private ArrayList<ImageContainer> getPhotos() {
        return images;
    }

    public static void searchResultsReceived(JSONSearchResult searchResult) {
        numOfImagesFound = searchResult.getNumberOfImagesFound();
        images.addAll(searchResult.getImageList(true));
    }
}
