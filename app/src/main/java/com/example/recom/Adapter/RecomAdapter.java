package com.example.recom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recom.Interfaces.OnLikeClickListener;
import com.bumptech.glide.Glide;
import com.example.recom.ItemView;
import com.example.recom.Models.Recom;
import com.example.recom.R;
import com.example.recom.Utilities.RecentlyViewedItemsManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecomAdapter extends RecyclerView.Adapter<RecomAdapter.RecomViewHolder> {

    private ArrayList<Recom> recoms;
    private ArrayList<Recom> filteredRecoms;
    private Context context;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private RecentlyViewedItemsManager recentlyViewed;
    private OnLikeClickListener onLikeClickListener;

    public RecomAdapter(ArrayList<Recom> recoms, Context context, RecentlyViewedItemsManager recentlyViewed, SharedPreferences sharedPreferences, Gson gson) {
        this.recoms = recoms;
        this.context = context;
        this.recentlyViewed = recentlyViewed;
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    public RecomAdapter(ArrayList<Recom> recoms, OnLikeClickListener onLikeClickListener) {
        this.recoms = recoms;
        this.onLikeClickListener = onLikeClickListener;
    }

    public void setRecoms(ArrayList<Recom> recoms) {
        this.recoms = recoms;
        this.filteredRecoms = new ArrayList<>(recoms);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recom_skeleton, parent, false);
        return new RecomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecomViewHolder holder, int position) {
        if (filteredRecoms.isEmpty()) {
            return;
        }
        Recom recom = getItem(position);
        holder.title.setText(recom.getTitle());
        if (recom.getImage() != null) {
            Uri imageUri = Uri.parse(recom.getImage());
            Glide.with(holder.itemView.getContext()).load(imageUri).into(holder.picture);
        } else {
            holder.picture.setImageResource(R.drawable.logo_icon);  // Set a placeholder if no image is available
        }
        if (recom.getPostedOn() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            holder.posted_on.setText(recom.getPostedOn());
        } else {
            holder.posted_on.setText("Unknown Date");
        }
        holder.description.setText(recom.getOverview());
        holder.rating.setRating(recom.getRating());
        holder.category.setText(String.join(", ", recom.getCategory()));
        holder.username.setText(recom.getUsername());

        holder.link.setText(recom.getLink());

        holder.link.setOnClickListener(v -> {
            String url = recom.getLink();
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

        holder.favorite_button.setOnClickListener(v -> {
            boolean newLikeStatus = !recom.isFavorite();
            recom.setFavorite(newLikeStatus);

            if (newLikeStatus) {
                holder.favorite_button.setImageResource(R.drawable.full_heart);
            } else {
                holder.favorite_button.setImageResource(R.drawable.empty_heart);
            }

        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemView.class);
            intent.putExtra("TITLE", recom.getTitle());
            intent.putExtra("USERNAME", recom.getUsername());
            intent.putExtra("DESCRIPTION", recom.getOverview());
            intent.putExtra("IMAGE_URL", recom.getImage());
            intent.putExtra("POSTED_ON", recom.getPostedOn().toString());
            intent.putExtra("CATEGORY", String.join(", ", recom.getCategory()));
            intent.putExtra("RATING", recom.getRating());
            intent.putExtra("LINK", recom.getLink());
            context.startActivity(intent);

        });
    }


    @Override
    public int getItemCount() {
        return recoms == null ? 0 : recoms.size();
    }

    public Recom getItem(int position) {
        return recoms.get(position);
    }

    public static class RecomViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView picture;
        private ShapeableImageView favorite_button;
        private MaterialTextView title;
        private MaterialTextView posted_on;
        private MaterialTextView description;
        private MaterialTextView link;
        private MaterialTextView category;
        private MaterialTextView username;
        private AppCompatRatingBar rating;

        public RecomViewHolder(@NonNull View itemView) {
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
