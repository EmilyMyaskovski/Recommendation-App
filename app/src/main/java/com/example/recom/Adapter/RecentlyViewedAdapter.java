package com.example.recom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recom.Models.Recom;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.example.recom.R;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.recom.Utilities.RecentlyViewedItemsManager;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.ViewHolder>{

    private ArrayList<Recom> recentlyViewedItems;
    private Context context;
    private RecentlyViewedItemsManager recentlyViewedItemsManager;

    public RecentlyViewedAdapter(ArrayList<Recom> items, Context context, RecentlyViewedItemsManager manager) {
        this.recentlyViewedItems = items;
        this.context = context;
        this.recentlyViewedItemsManager = manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recom_skeleton, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyViewedAdapter.ViewHolder holder, int position) {
        Recom viewedItems = recentlyViewedItems.get(position);

        holder.title.setText(viewedItems.getTitle());
        holder.username.setText(viewedItems.getUsername());
        holder.rating.setRating(viewedItems.getRating());
        holder.description.setText(viewedItems.getOverview());
        holder.category.setText(String.join(", ", viewedItems.getCategory()));

        if (viewedItems.getPostedOn() != null) {
            holder.posted_on.setText(viewedItems.getPostedOn());
        } else {
            holder.posted_on.setText("Unknown Date");
        }


        saveDateToSharedPreferences(viewedItems.getPostedOn());

        if (viewedItems.getImage() != null) {
            Glide.with(context)
                    .load(viewedItems.getImage())
                    .placeholder(R.drawable.logo_icon)
                    .into(holder.picture);
        } else {
            holder.picture.setImageResource(R.drawable.logo_icon);
        }

        holder.link.setText(viewedItems.getLink());
        holder.link.setOnClickListener(v -> {
            String url = viewedItems.getLink();
            if (url != null) {
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show();
            }
        });


        holder.favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = !viewedItems.isFavorite();
                viewedItems.setFavorite(isFavorite);
                recentlyViewedItemsManager.updateItemFavoriteStatus(viewedItems.getTitle(), isFavorite);
                updateFavoriteIcon(holder.favorite_button, isFavorite);
                recentlyViewedItemsManager.updateItemFavoriteStatus(viewedItems.getTitle(),isFavorite);
            }
        });

    }

    private void saveDateToSharedPreferences(String date) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LAST_VIEWED_DATE", date);
        editor.apply();
    }
    private void updateFavoriteIcon(ImageView favoriteIcon, boolean isFavorite) {
        if (isFavorite) {
            favoriteIcon.setImageResource(R.drawable.full_heart);
        } else {
            favoriteIcon.setImageResource(R.drawable.empty_heart);
        }


    }

    @Override
    public int getItemCount() {
        return recentlyViewedItems.size();
    }

    public void updateItems(ArrayList<Recom> newItems) {
        this.recentlyViewedItems = newItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView picture;
        private final ShapeableImageView favorite_button;
        private final MaterialTextView title;
        private final MaterialTextView posted_on;
        private final MaterialTextView description;
        private final MaterialTextView link;
        private final MaterialTextView category;
        private final MaterialTextView username;
        private final AppCompatRatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            favorite_button = itemView.findViewById(R.id.favorite_button);
            picture = itemView.findViewById(R.id.picture);
            posted_on = itemView.findViewById(R.id.posted_on);
            category = itemView.findViewById(R.id.category);
            username = itemView.findViewById(R.id.username);
            description = itemView.findViewById(R.id.description);
            rating = itemView.findViewById(R.id.rating);
            link = itemView.findViewById(R.id.link);
        }

    }
}
