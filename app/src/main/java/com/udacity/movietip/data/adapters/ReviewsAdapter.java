package com.udacity.movietip.data.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.udacity.movietip.R;
import com.udacity.movietip.data.adapters.ReviewsAdapter.ReviewViewHolder;
import com.udacity.movietip.data.model.Reviews;
import com.udacity.movietip.data.model.Trailers;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    final private ItemClickListener mOnClickListener;

    private final List<Reviews> mReviewList;
    private final Context mContext;

    // Reference: Udacity Android Developer Nanodegree Program > Developing Android Apps > Lesson 4: RecyclerView > Part 20. Responding to Clicks
    public interface ItemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    // Provide a reference to the views for each trailer item
    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView reviewAuthorTextView;
        private final TextView reviewContentsTextView;

        ReviewViewHolder(View view){
            super(view);

            reviewAuthorTextView = view.findViewById(R.id.movie_review_author);
            reviewContentsTextView = view.findViewById(R.id.movie_review_contents_textview);
            reviewContentsTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onItemClick(getAdapterPosition());
        }
    }

    // Provide a constructor
    public ReviewsAdapter(Context context, List<Reviews> reviewsList, ItemClickListener listener){
        this.mContext = context;
        this.mOnClickListener = listener;
        this.mReviewList = reviewsList;
    }

    // Create new views as invoke by the layout manager
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review_item, parent, false);

        // dynamically calculate and set the width of the view
        // Reference: https://stackoverflow.com/a/50498245
        final DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        Integer width = displayMetrics.widthPixels;
        int calculatedWidth = (int) Math.round(width/1.5);

        view.setLayoutParams(new RecyclerView.LayoutParams(calculatedWidth, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new ReviewViewHolder(view);
    }

    // Replace the contents of a view as invoked by the layout manager
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        final Reviews currentReview = mReviewList.get(position);

        holder.reviewAuthorTextView.setText(currentReview.getAuthor());
        holder.reviewContentsTextView.setText(currentReview.getContent());

    }

    // Return the size of the list as invoked by the layout manager
    @Override
    public int getItemCount() {
        return mReviewList != null ? mReviewList.size() : 0;
    }

    // Reference: https://stackoverflow.com/a/48959184
    public void setReviewsList(List<Reviews> reviewsList){
        // clear the old list
        mReviewList.clear();
        // collecttion.addall in place of foreeach or for loop
        mReviewList.addAll(reviewsList);
        // notify the adapter
        notifyDataSetChanged();
    }
}
