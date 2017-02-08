package com.example.android.favouritemovieprojectonemichael;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.favouritemovieprojectonemichael.Data.MovieData;
import com.squareup.picasso.Picasso;

/**
 * Created by LondonAegis on 06/02/2017.
 */

public class MyMovieAdapter extends RecyclerView.Adapter<MyMovieAdapter.MyMovieAdapterViewHolder> {
    private static final String LOG_TAG = MyMovieAdapter.class.getSimpleName();

    private MovieData[] mMovieData;
    private ImageView mLeftImage;
    private ImageView mRightImage;
    View mView;

    public class MyMovieAdapterViewHolder extends RecyclerView.ViewHolder {


        public MyMovieAdapterViewHolder(View view) {
            super(view);
            mView = view;
            mLeftImage = (ImageView) view.findViewById(R.id.left_image);
            mLeftImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    
                }
            });
            mRightImage = (ImageView) view.findViewById(R.id.right_image);
        }
    }

    @Override
    public MyMovieAdapter.MyMovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MyMovieAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }

    @Override
    public void onBindViewHolder(MyMovieAdapter.MyMovieAdapterViewHolder holder, int position) {
        MovieData selectedMovieData = mMovieData[position];
        // holder.mLeftImage.setImageURI(Uri.parse(selectedMovieData.getPosterPath()));
        Picasso.with(mView.getContext())
                .load(selectedMovieData.getPosterPath())
                .into(mLeftImage);
    }

    public void setMovieData(MovieData[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
