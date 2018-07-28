package com.udacity.movietip.data.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.movietip.R;
import com.udacity.movietip.data.adapters.ReviewsAdapter.ReviewViewHolder;
import com.udacity.movietip.data.model.Reviews;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private static final String REVIEW_INTENT_TITLE = "Choose Browser";
    private final List<Reviews> mReviewList;


    class ReviewViewHolder extends RecyclerView.ViewHolder{

        private final TextView reviewAuthorTextView;
        private final TextView reviewContentsTextView;

        ReviewViewHolder(View view){
            super(view);

            reviewAuthorTextView = view.findViewById(R.id.movie_review_author);
            reviewContentsTextView = view.findViewById(R.id.movie_review_contents_textview);
        }
    }

    public ReviewsAdapter(List<Reviews> reviewsList){
        this.mReviewList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review_item, parent, false);

        // dynamically calculate and set the width of the view
        // Reference: https://stackoverflow.com/a/50498245
        final DisplayMetrics displayMetrics = parent.getContext().getResources().getDisplayMetrics();
        Integer width = displayMetrics.widthPixels;
        int calculatedWidth = (int) Math.round(width/1.5);

        view.setLayoutParams(new RecyclerView.LayoutParams(calculatedWidth, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new ReviewViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        final Reviews currentReview = mReviewList.get(position);

        holder.reviewAuthorTextView.setText(currentReview.getAuthor());
        holder.reviewContentsTextView.setText(currentReview.getContent());

        holder.reviewContentsTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Intent reviewIntent = new Intent(Intent.ACTION_VIEW);
                reviewIntent.setData(Uri.parse(currentReview.getUrl()));

                Intent chooser = Intent.createChooser(reviewIntent, REVIEW_INTENT_TITLE);

                if (reviewIntent.resolveActivity(v.getContext().getPackageManager()) != null){
                    v.getContext().startActivity(chooser);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mReviewList != null ? mReviewList.size() : 0;
    }


    // Reference: https://stackoverflow.com/a/48959184
    public void setReviewsList(List<Reviews> reviewsList){
        mReviewList.clear();
        mReviewList.addAll(reviewsList);
        notifyDataSetChanged();
    }
}
