package com.udacity.movietip.data.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.udacity.movietip.R;
import com.udacity.movietip.data.model.Trailers;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private static final String TRAILER_INTENT_TITLE = "Choose Player";
    private static final String SHARE_BUTTON_INTENT_TITLE = "Share video to...";
    private final List<Trailers> mTrailersList;


    class TrailerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView trailerImageView;
        private final TextView trailerTextView;
        private final ImageButton shareImageButton;

        TrailerViewHolder(View view){
            super(view);

            trailerTextView = view.findViewById(R.id.movie_trailer_title);
            shareImageButton = view.findViewById(R.id.movie_trailer_share_button);
            trailerImageView = view.findViewById(R.id.movie_trailer_imageView);
        }
    }


    public TrailersAdapter(List<Trailers> trailersList){
        this.mTrailersList = trailersList;
    }


    @NonNull
    @Override
    public TrailersAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trailer_item, parent, false);

        // Reference: https://stackoverflow.com/a/50498245
        final DisplayMetrics displayMetrics = parent.getContext().getResources().getDisplayMetrics();
        Integer width = displayMetrics.widthPixels;
        int calculatedWidth = (int) Math.round(width/1.2);

        view.setLayoutParams(new RecyclerView.LayoutParams(calculatedWidth, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new TrailerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

        holder.trailerTextView.setText(mTrailersList.get(position).getName());

        /* Reference: ic_broken_image made by https://www.flaticon.com/authors/those-icons and is licensed by http://creativecommons.org/licenses/by/3.0/
           Reference: ic_image_loading icon made by https://www.flaticon.com/authors/dave-gandy and is licensed by http://creativecommons.org/licenses/by/3.0/
         */

        Glide.with(holder.itemView.getContext())
                .load(mTrailersList.get(position).getTrailerThumbnailUrl())
                .apply(new RequestOptions()
                    .placeholder(R.drawable.ic_image_loading)
                    .error(R.drawable.ic_broken_image)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(holder.trailerImageView);

        holder.trailerImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Trailers trailer = mTrailersList.get(position);

                // Reference: https://developer.android.com/guide/components/intents-common#Music
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                trailerIntent.setData(Uri.parse(mTrailersList.get(position).getTrailerUrl()));

                // Reference: https://developer.android.com/training/basics/intents/sending#AppChooser
                Intent chooser = Intent.createChooser(trailerIntent, TRAILER_INTENT_TITLE);

                if (trailerIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(chooser);
                }
            }
        });

        holder.shareImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Trailers trailer = mTrailersList.get(position);

                // Reference: https://developer.android.com/training/sharing/
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, trailer.getTrailerUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Intent chooser = Intent.createChooser(intent, SHARE_BUTTON_INTENT_TITLE);

                if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(chooser);
                }
            }
        });
    }

    // Return the size of the list as invoked by the layout manager
    @Override
    public int getItemCount() {
        return mTrailersList != null ? mTrailersList.size() : 0;
    }

    // Reference: https://stackoverflow.com/a/48959184
    public void setTrailersList(List<Trailers> trailersList){
        mTrailersList.clear();
        mTrailersList.addAll(trailersList);
        notifyDataSetChanged();
    }
}
