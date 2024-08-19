package com.example.recom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import com.bumptech.glide.Glide;
import com.example.recom.Models.Recom;
import com.example.recom.Utilities.RecentlyViewedItemsManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class ItemView extends AppCompatActivity {

    private ImageButton back_button;
    private ShapeableImageView favorite_button;
    private MaterialTextView title;
    private MaterialTextView posted_on;
    private MaterialTextView username;
    private MaterialTextView description;
    private AppCompatRatingBar rating;
    private MaterialTextView category;
    private MaterialTextView link;
    private ImageView picture;
    private Recom recom;
    private RecentlyViewedItemsManager recentlyViewedItems;
    private Gson gson;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_view_activity);

        sharedPreferences = getSharedPreferences("com.example.recom.PREFERENCES", Context.MODE_PRIVATE);
        gson = new Gson();

        findViews();
        initViews();

        recentlyViewedItems = new RecentlyViewedItemsManager(this);

        // Retrieve and display the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String titleText = intent.getStringExtra("TITLE");
            String usernameText = intent.getStringExtra("USERNAME");
            String descriptionText = intent.getStringExtra("DESCRIPTION");
            String imageUrl = intent.getStringExtra("IMAGE_URL");
            String postedOnText = intent.getStringExtra("POSTED_ON");
            String categoryText = intent.getStringExtra("CATEGORY");
            float ratingValue = intent.getFloatExtra("RATING", 0);
            String linkValue = intent.getStringExtra("LINK");

            title.setText(titleText);
            description.setText(descriptionText);
            posted_on.setText(postedOnText);
            Log.d("ItemView", "Posted on: " + postedOnText);
            category.setText(categoryText);
            rating.setRating(ratingValue);
            username.setText(usernameText);
            Log.d("ItemView", "Username: " + usernameText);
            link.setText(linkValue);

            Glide.with(this).load(imageUrl).into(picture);

            Recom item = new Recom();
            item.setTitle(titleText);
            item.setUsername(usernameText);
            item.setOverview(descriptionText);
            item.setPostedOn(postedOnText);
            item.setCategory(categoryText);
            item.setRating(ratingValue);
            item.setLink(linkValue);
            item.setImage(imageUrl);

            addRecentlyViewedItem(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void addRecentlyViewedItem(Recom recom) {
        if (recom != null) {
            ArrayList<Recom> viewedItems = recentlyViewedItems.getItems();

            // Check if the item is already in the list and remove it
            for (int i = 0; i < viewedItems.size(); i++) {
                if (viewedItems.get(i).getTitle().equals(recom.getTitle())) {
                    viewedItems.remove(i);
                    break;
                }
            }
            // Add the item to the top of the list
            viewedItems.add(0, recom);
            Log.e("ItemView", "Item added");

            recentlyViewedItems.saveItems(viewedItems);
        } else {
            Log.e("ItemView", "Recom is null");
        }

    }

    private void initViews() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void findViews() {
        back_button = findViewById(R.id.back_button);
        title = findViewById(R.id.title);
        posted_on = findViewById(R.id.posted_on);
        username = findViewById(R.id.username);
        category = findViewById(R.id.category);
        description = findViewById(R.id.description);
        rating = findViewById(R.id.rating);
        picture = findViewById(R.id.picture);
        link = findViewById(R.id.link);
    }
}
