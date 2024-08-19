package com.example.recom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.recom.Interfaces.OnLikeClickListener;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recom.ItemView;
import com.example.recom.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.recom.Models.Post;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;
    private Context context;
    private FirebaseUser currentUser;
    private OnLikeClickListener onLikeClickListener;


    public PostAdapter(Context context, FirebaseUser currentUser, OnLikeClickListener onLikeClickListener) {
        this.context = context;
        this.posts = new ArrayList<>();
        this.currentUser = currentUser;
        this.onLikeClickListener = onLikeClickListener;
    }

    public void addNewPost(Post newPost) {
        posts.add(newPost);
        notifyItemInserted(posts.size() + 1);
    }

    public void clearPosts() {
        this.posts.clear();
        notifyDataSetChanged();
    }


    public void setPosts(List<Post> posts) {
        this.posts = new ArrayList<>(posts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recom_skeleton, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        // Bind the data to the views
        holder.rating.setRating(post.getRating());
        holder.description.setText(post.getOverview());
        holder.category.setText(post.getCategory());
        holder.link.setText(post.getLink());
        holder.username.setText(post.getUsername());
        holder.title.setText(post.getTitle());

        // Check if the date is null
        if (post.getPostedOn() != null) {
            holder.postedOn.setText(post.getPostedOn());
        } else {
            holder.postedOn.setText("Unknown Date");
        }

        if (post.getImage() != null) {
            Glide.with(context)
                    .load(post.getImage())
                    .placeholder(R.drawable.logo_icon)
                    .into(holder.picture);
        } else {
            holder.picture.setImageResource(R.drawable.logo_icon);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemView.class);
            intent.putExtra("TITLE", post.getTitle());
            intent.putExtra("USERNAME", post.getUsername());
            intent.putExtra("DESCRIPTION", post.getOverview());
            intent.putExtra("IMAGE_URL", post.getImage());
            intent.putExtra("POSTED_ON", post.getPostedOn() != null ? post.getPostedOn() : "Unknown Date");
            intent.putExtra("CATEGORY", post.getCategory());
            intent.putExtra("RATING", post.getRating());
            intent.putExtra("LINK", post.getLink());
            context.startActivity(intent);
        });

        holder.link.setOnClickListener(v -> {
            String url = post.getLink();
            if (url != null && !url.isEmpty()) {
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No URL provided", Toast.LENGTH_SHORT).show();
            }
        });

        AtomicBoolean isFavorite = new AtomicBoolean(loadLikeStateFromSharedPreferences(post));
        AtomicInteger likeCount = new AtomicInteger(loadLikeCountFromSharedPreferences(post));
        updateFavoriteIcon(holder.favorite_button, isFavorite.get());

        holder.favorite_button.setOnClickListener(v -> {
            boolean newLikeStatus = !isFavorite.get();
            post.setFavorite(newLikeStatus);

            if (newLikeStatus) {
                likeCount.incrementAndGet();
            } else {
                likeCount.decrementAndGet();
            }

            updateFavoriteIcon(holder.favorite_button, newLikeStatus);
            saveLikeStateToSharedPreferences(post);
            saveLikeCountToSharedPreferences(post, likeCount.get());
            saveLikeCountToDatabase(post, likeCount.get());

            if (onLikeClickListener != null) {
                onLikeClickListener.onLikeClicked(newLikeStatus ? 1 : -1);
            }

            isFavorite.set(newLikeStatus);
        });
    }

    private void saveLikeCountToDatabase(Post post, int count) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("posts").child(post.getPostId());

            Map<String, Object> updates = new HashMap<>();
            updates.put("likeCount", count);

            dbRef.updateChildren(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("PostAdapter", "Like count updated in database for post ID: " + post.getPostId());
                } else {
                    Log.e("PostAdapter", "Failed to update like count in database", task.getException());
                }
            });
        }

    }

    private boolean loadLikeStateFromSharedPreferences(Post post) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PostLikes", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(post.getPostId() + "_liked", false);
    }

    private int loadLikeCountFromSharedPreferences(Post post) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PostLikes", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(post.getPostId() + "_like_count", 0);
    }

    private void saveLikeStateToSharedPreferences(Post post) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PostLikes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(post.getPostId() + "_liked", post.isFavorite());
        editor.apply();
    }

    private void saveLikeCountToSharedPreferences(Post post, int count) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PostLikes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(post.getPostId() + "_like_count", count);
        editor.apply();
    }

    private void updateFavoriteIcon(ShapeableImageView favoriteButton, boolean isFavorite) {
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.full_heart);
        } else {
            favoriteButton.setImageResource(R.drawable.empty_heart);
        }
    }

    private void saveLikeStatus(Post post) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("likes");
            String postId = post.getPostId();

            dbRef.child(postId).setValue(post.isFavorite()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (onLikeClickListener != null) {
                        saveLikeStateToSharedPreferences(post);
                    }
                    Log.d("PostAdapter", "Like status saved to Firebase for post ID: " + postId);
                } else {
                    Log.e("PostAdapter", "Failed to save like status", task.getException());
                }
            });
        } else {
            Log.e("PostAdapter", "User is not authenticated, cannot save like status.");
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public List<Post> getPosts() {
        return new ArrayList<>(posts);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView picture;
        private MaterialTextView title;
        private MaterialTextView postedOn;
        private MaterialTextView description;
        private MaterialTextView link;
        private MaterialTextView category;
        private MaterialTextView username;
        private AppCompatRatingBar rating;
        private ShapeableImageView favorite_button;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            picture = itemView.findViewById(R.id.picture);
            postedOn = itemView.findViewById(R.id.posted_on);
            category = itemView.findViewById(R.id.category);
            username = itemView.findViewById(R.id.username);
            description = itemView.findViewById(R.id.description);
            rating = itemView.findViewById(R.id.rating);
            link = itemView.findViewById(R.id.link);
            favorite_button = itemView.findViewById(R.id.favorite_button);
        }
    }
}
