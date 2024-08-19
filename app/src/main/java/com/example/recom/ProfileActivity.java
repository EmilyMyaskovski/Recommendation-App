package com.example.recom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.recom.Interfaces.OnLikeClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recom.Adapter.RecomAdapter;
import com.example.recom.Models.Post;
import com.example.recom.Adapter.PostAdapter;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements OnLikeClickListener{
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CREATE_POST_REQUEST_CODE = 1001;

    private ImageView search_button;
    private ImageView create_button;
    private ImageView liked_button;
    private ImageButton back_button;
    private ImageView home_button;
    private MaterialTextView username;
    private MaterialTextView full_name;
    private ImageButton profile_image;
    private Uri imageUri;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private PostAdapter postAdapter;
    private RecyclerView recommendations_section;
    private TextView recommend;
    private TextView likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LogInActivity.class));
            finish();
            return;
        }

        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");

        findViews();
        initRecyclerView();
        initViews();

        loadPosts();
        loadProfileFromSharedPreferences();

    }

    private void loadProfileFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String usernameValue = sharedPreferences.getString("USERNAME", null);
        String fullNameValue = sharedPreferences.getString("FULL_NAME", null);
        String imageUrl = sharedPreferences.getString("PROFILE_IMAGE_URL", null);
        int recommendCount = sharedPreferences.getInt("RECOMMEND_COUNT", 0);
        int likesCount = sharedPreferences.getInt("LIKES_COUNT", 0);

        if (usernameValue != null) {
            username.setText(usernameValue);
        }

        if (fullNameValue != null) {
            full_name.setText(fullNameValue);
        }

        if (imageUrl != null) {
            imageUri = Uri.parse(imageUrl);
            loadImageFromUri(imageUri);
        }
        recommend.setText(recommendCount + "\nRecommend");
        likes.setText(likesCount + "\nLikes");

    }


    private void loadPosts() {
        // Clear existing posts in the adapter before loading new posts
        postAdapter.clearPosts();

        // Check if there are any saved posts in SharedPreferences
        List<Post> savedPosts = getPostsFromSharedPreferences();
        if (savedPosts != null && !savedPosts.isEmpty()) {
            postAdapter.setPosts(savedPosts);
        }
    }

    private void loadImageFromUri(Uri uri) {
        try {
            if (uri != null) {
                profile_image.setImageURI(uri);
            }
        } catch (SecurityException e) {
            Toast.makeText(ProfileActivity.this, "Unable to access the selected image. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveProfileToSharedPreferences();
        FirebaseDatabase.getInstance().getReference("Users").keepSynced(true);
        saveProfileToDatabase();
    }

    private void saveProfileToSharedPreferences() {
        String currentUsername = username.getText().toString();
        String currentFullName = full_name.getText().toString();
        String currentImageUrl = imageUri != null ? imageUri.toString() : getSavedImageUrlFromSharedPreferences();
        int currentLikesCount = getLikesCountFromSharedPreferences();

        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("USERNAME", currentUsername);
        editor.putString("FULL_NAME", currentFullName);
        editor.putString("PROFILE_IMAGE_URL", currentImageUrl);
        editor.putInt("LIKES_COUNT", currentLikesCount);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveProfileToDatabase();
    }

    private void saveProfileToDatabase() {
        String currentUsername = username.getText().toString();
        String currentFullName = full_name.getText().toString();
        String currentImageUrl = imageUri != null ? imageUri.toString() : getSavedImageUrlFromSharedPreferences();
        int currentLikesCount = getLikesCountFromSharedPreferences();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            Map<String, Object> profileData = new HashMap<>();
            profileData.put("username", currentUsername);
            profileData.put("fullName", currentFullName);
            profileData.put("profileImageUrl", currentImageUrl);
            profileData.put("likeCount", currentLikesCount);

            databaseReference.setValue(profileData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ProfileActivity", "Profile saved to database");
                        } else {
                            Log.d("ProfileActivity", "Failed to save profile to database");
                        }
                    });
        }
    }

    private int getLikesCountFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        return sharedPreferences.getInt("LIKES_COUNT", 0); // Default to 0 if not found
    }

    private void saveImageUrlToSharedPreferences(String imageUrl) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PROFILE_IMAGE_URL", imageUrl);
        editor.apply();
    }

    private String getSavedImageUrlFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        return sharedPreferences.getString("PROFILE_IMAGE_URL", null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            loadImageFromUri(imageUri);
            saveImageToFirebase();
        }

        if (requestCode == CREATE_POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Log all the data received
            Log.d("ProfileActivity", "Received post data: " +
                    "Rating: " + data.getFloatExtra("POST_RATING", 0) +
                    ", Overview: " + data.getStringExtra("POST_OVERVIEW") +
                    ", Category: " + data.getStringExtra("POST_CATEGORY") +
                    ", Link: " + data.getStringExtra("POST_LINK") +
                    ", ImageUri: " + data.getStringExtra("POST_IMAGE_URI") +
                    ", Username: " + data.getStringExtra("USERNAME") +
                    ", Date: " + data.getStringExtra("POST_DATE"));

            // Extract post details from the Intent
            float postRating = data.getFloatExtra("POST_RATING", 0);
            String postOverview = data.getStringExtra("POST_OVERVIEW");
            String postCategory = data.getStringExtra("POST_CATEGORY");
            String postLink = data.getStringExtra("POST_LINK");
            String postImageUri = data.getStringExtra("POST_IMAGE_URI");
            String postUsername = data.getStringExtra("USERNAME");
            String postDateString = data.getStringExtra("POST_DATE");
            String postTitleString = data.getStringExtra("POST_TITLE");
            String postId = FirebaseDatabase.getInstance().getReference("posts").push().getKey();

            // Ensure that all fields have valid data
            if (postOverview == null || postCategory == null || postLink == null || postUsername == null || postDateString == null) {
                Log.e("ProfileActivity", "Some post data is missing!");
                return;
            }

            // Create a new Post
            Post newPost = new Post(
                    postId,
                    postRating,
                    postOverview,
                    postCategory,
                    postLink,
                    postImageUri,
                    postTitleString,
                    postUsername,
                    postDateString
            );


            postAdapter.addNewPost(newPost);
            recommendations_section.scrollToPosition(postAdapter.getItemCount() - 1);
            incrementRecommendationCount();
            savePostsToSharedPreferences(postAdapter.getPosts());
            savePostToFirebase(newPost);
        }
    }

    private void saveImageToFirebase() {
        if (imageUri != null && currentUser != null) {
            StorageReference fileReference = storageReference.child(currentUser.getUid() + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveImageUrlToSharedPreferences(imageUrl);
                        saveProfileToDatabase(); // Update the profile in the database with the new image URL
                        Log.d("ProfileActivity", "Image saved to Firebase");
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        Log.e("ProfileActivity", "Failed to upload image to Firebase", e);
                    });
        }
    }

    private void incrementRecommendationCount() {
        String currentText = recommend.getText().toString();
        int currentCount = Integer.parseInt(currentText.split("\n")[0]);
        int newCount = currentCount + 1;
        recommend.setText(newCount + "\nRecommend");
        saveRecommendationCount(newCount);
    }
    private void saveRecommendationCount(int count) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("RECOMMEND_COUNT", count);
        editor.apply();
    }

    private void incrementLikesCount() {
        String currentText = likes.getText().toString();
        int currentCount = Integer.parseInt(currentText.split("\n")[0]); // Extract the current count
        int newCount = currentCount + 1;
        Log.d("ProfileActivity", "newCount " + newCount);
        likes.setText(newCount + "\nlikes");

        saveLikesCount(newCount);
    }


    private void saveLikesCount(int count) {
        // Save to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("LIKE_COUNT", count);
        editor.apply();

        // Save to Firebase
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            Map<String, Object> likeData = new HashMap<>();
            likeData.put("likeCount", count);

            databaseReference.updateChildren(likeData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ProfileActivity", "Like count saved to database");
                        } else {
                            Log.d("ProfileActivity", "Failed to save like count to database");
                        }
                    });
        }
    }


    private void savePostToFirebase(Post post) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("recommendations");

            dbRef.child(post.getPostId()).setValue(post).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("ProfileActivity", "Post saved to Firebase under User ID: " + userId);
                } else {
                    Log.e("ProfileActivity", "Failed to save post to Firebase", task.getException());
                }
            });
        } else {
            Log.e("ProfileActivity", "User is not authenticated, cannot save post.");
        }
    }

    private void savePostsToSharedPreferences(List<Post> posts) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPosts", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(posts);
        editor.putString("posts", json);
        editor.apply();
    }

    private List<Post> getPostsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPosts", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("posts", null);
        Type type = new TypeToken<ArrayList<Post>>() {}.getType();

        List<Post> posts = gson.fromJson(json, type);

        // If the retrieved posts are null, initialize them as an empty list
        if (posts == null) {
            posts = new ArrayList<>();
        }

        return posts;
    }


    private void loadPostsFromFirebase() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("recommendations");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Post> posts = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Post post = snapshot.getValue(Post.class);
                        if (post != null) {
                            String postDateString = snapshot.child("postedOn").getValue(String.class);
                            if (postDateString != null) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                                LocalDate postDate = LocalDate.parse(postDateString, formatter);
                                post.setPostedOn(String.valueOf(postDate));
                            }
                            posts.add(post);
                        }
                    }

                    // Set the posts in the adapter and update UI
                    postAdapter.setPosts(posts);
                    savePostsToSharedPreferences(posts); // Also update SharedPreferences
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ProfileActivity", "Failed to load posts from Firebase", databaseError.toException());
                }
            });
        }
    }


    private void initViews() {
        search_button.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        create_button.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, CreateActivity.class);
            startActivityForResult(intent, CREATE_POST_REQUEST_CODE);
        });

        liked_button.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LikedActivity.class);
            startActivity(intent);
        });

        back_button.setOnClickListener(v -> finish());

        home_button.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });

        profile_image.setOnClickListener(v -> openFileChooser());
        likes.setOnClickListener(v -> {
            // Assuming that this is a post created by the current user
            incrementLikesCount();
        });
    }

    private void initRecyclerView() {
        recommendations_section.setLayoutManager(new LinearLayoutManager(this));
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        postAdapter = new PostAdapter(this, currentUser, this);
        recommendations_section.setAdapter(postAdapter);
        RecomAdapter recomAdapter = new RecomAdapter(new ArrayList<>(), this);
    }



    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    private void findViews() {
        back_button = findViewById(R.id.back_button);
        search_button = findViewById(R.id.search_button);
        create_button = findViewById(R.id.create_button);
        liked_button = findViewById(R.id.liked_button);
        home_button = findViewById(R.id.home_button);
        username = findViewById(R.id.username);
        full_name = findViewById(R.id.full_name);
        profile_image = findViewById(R.id.profile_image);
        recommendations_section = findViewById(R.id.recommendations_section);
        recommend = findViewById(R.id.recommend);
        likes = findViewById(R.id.likes);

    }

    @Override
    public void onLikeClicked(int likeChange) {
        // Increment or decrement the like count based on the received value
        int currentCount = getLikesCountFromSharedPreferences();
        int newCount = currentCount + likeChange;

        likes.setText(newCount + "\nLikes");

        saveLikesCount(newCount);
    }
}
