package com.udacity.movietip.data.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.udacity.movietip.R;
import com.udacity.movietip.data.model.Movie;

import java.util.List;

// Reference: https://developer.android.com/guide/topics/ui/layout/recyclerview
public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.MovieViewHolder>{

    private static final String TAG = "MasterListAdapter";

    final private GridItemClickListener mOnClickListener;

    private List<Movie> mMoviesList;
    private Context mContext;

    // Reference: Udacity Android Developer Nanodegree Program > Developing Android Apps > Lesson 4: RecyclerView > Part 20. Responding to Clicks
    public interface GridItemClickListener {
        void onGridItemClick(View v, int clickedItemIndex);
    }

    // Provide a reference to the views for each image item
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView posterImage;
        MovieViewHolder(View view){
            super(view);

            posterImage = (ImageView) view.findViewById(R.id.movie_poster_imageView);
            posterImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onGridItemClick(v, getAdapterPosition());
        }
    }

    // Provide a constructor
    public MasterListAdapter(Context context, List<Movie> moviesList, GridItemClickListener listener){
        this.mContext = context;
        this.mOnClickListener = listener;
        this.mMoviesList = moviesList;
    }

    // Create new views as invoked by the layout manager
    @Override
    public MasterListAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent);
        return new MovieViewHolder(view);
    }

    // Replace the contents of a view as invoked by the layout manager
    @Override
    public void onBindViewHolder(MasterListAdapter.MovieViewHolder holder, int position) {

        // Grab the movie from the movie list at the passed in position
        Movie movie = mMoviesList.get(position);

        /* Use Glide to load the images from the internet
           Reference: https://github.com/bumptech/glide
           Reference: https://stackoverflow.com/questions/45481756/glide-4-0-0-missing-placeholder-error-glideapp-and-does-not-resolve-its-method
           Reference: ic_broken_image made by https://www.flaticon.com/authors/those-icons and is licensed by http://creativecommons.org/licenses/by/3.0/
           Reference: ic_image_loading icon made by https://www.flaticon.com/authors/dave-gandy and is licensed by http://creativecommons.org/licenses/by/3.0/
         */
        Glide.with(mContext)
                .load(movie.getFullPosterPath())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_loading)
                        .error(R.drawable.ic_broken_image))
                .into(holder.posterImage);
    }

    // Return the size of the list as invoked by the layout manager
    @Override
    public int getItemCount() {
        return mMoviesList != null ? mMoviesList.size() : 0;
    }

    // Reference: https://stackoverflow.com/a/48959184
    public void setMoviesList(List<Movie> moviesList){
        // clear the old list
        mMoviesList.clear();
        // collection.addAll in place of foreach or for loop
        mMoviesList.addAll(moviesList);
        // notify the adapter
        notifyDataSetChanged();
    }
}
