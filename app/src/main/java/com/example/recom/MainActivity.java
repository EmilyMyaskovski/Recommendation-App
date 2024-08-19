package com.example.recom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import com.example.recom.Adapter.RecentlyViewedAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recom.Adapter.RecomAdapter;
import com.example.recom.Models.Recom;
import com.example.recom.Utilities.RecentlyViewedItemsManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recentlyViewedRecyclerView;
    private RecomAdapter recomAdapter;
    private RecentlyViewedItemsManager recentlyViewedItemsManager;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private ImageView profile_button;
    private ImageView search_button;
    private ImageView create_button;
    private ImageView liked_button;
    private ImageView home_buttom;
    private MaterialTextView link;
    private ImageView logo;
    private ArrayList<Recom> recentlyViewedItems;
    private RecentlyViewedAdapter recentlyViewedAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();

        sharedPreferences = getSharedPreferences("YourPreferenceName", MODE_PRIVATE);
        gson = new Gson();
        recentlyViewedItemsManager = new RecentlyViewedItemsManager(this);

        ArrayList<Recom> recentlyViewedItems = recentlyViewedItemsManager.getItems();

        // Initialize the RecyclerView and set the LayoutManager
        recentlyViewedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recomAdapter = new RecomAdapter(recentlyViewedItems, this, recentlyViewedItemsManager, sharedPreferences, gson);
        recentlyViewedRecyclerView.setAdapter(recomAdapter);

        loadRecentlyViewedItems();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecentlyViewedItems();
    }


    private void loadRecentlyViewedItems() {
        recentlyViewedItems = recentlyViewedItemsManager.getItems();

        if (recentlyViewedItems != null && !recentlyViewedItems.isEmpty()) {
            updateRecentlyViewedItemsView(recentlyViewedItems);
        } else {
            Log.d("MainActivity", "No recently viewed items to display.");
        }
    }

    private void updateRecentlyViewedItemsView(ArrayList<Recom> items) {
        if (recentlyViewedAdapter == null) {
            recentlyViewedAdapter = new RecentlyViewedAdapter(items, this, recentlyViewedItemsManager);
            recentlyViewedRecyclerView.setAdapter(recentlyViewedAdapter);
            recentlyViewedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recentlyViewedAdapter.updateItems(items);
        }
    }



    private void findViews() {
        recentlyViewedRecyclerView = findViewById(R.id.recyclerViewRecoms);
        link = findViewById(R.id.link);
        logo = findViewById(R.id.logo);
        profile_button = findViewById(R.id.profile_button);
        search_button = findViewById(R.id.search_button);
        create_button = findViewById(R.id.create_button);
        liked_button = findViewById(R.id.liked_button);
        home_buttom = findViewById(R.id.home_buttom);
    }

    private void initViews() {

        profile_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        search_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        create_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateActivity.class);
            startActivity(intent);
        });

        liked_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LikedActivity.class);
            startActivity(intent);
        });
    }
}
