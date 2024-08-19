package com.example.recom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recom.Adapter.RecomAdapter;
import com.example.recom.Models.Recom;
import com.example.recom.Utilities.DataManager;
import com.example.recom.Utilities.RecentlyViewedItemsManager;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    private ImageView home_button;
    private ImageView liked_button;
    private ImageView create_button;
    private ImageView profile_button;
    private ImageButton back_button;
    private TextView discover_title;
    private ImageView search_button;
    private SearchView searchView;
    private DataManager dataManager;
    private RecyclerView recycler_view;
    private RecomAdapter recomAdapter;
    private RecentlyViewedItemsManager viewedItemsManager;
    private ArrayList<Recom> recoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        findViews();
        initViews();

        searchView.setQueryHint("Search items");

        int searchEditTextId = androidx.appcompat.R.id.search_src_text;
        EditText searchEditText = searchView.findViewById(searchEditTextId);
        searchEditText.setText("search for anything");

        if (searchEditText != null) {
            searchEditText.setTextColor(Color.BLACK);
            searchEditText.setHintTextColor(Color.GRAY);
            searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    searchEditText.setText("");
                }
            });
        }

        recoms = DataManager.getRecoms();
        if (recoms == null) {
            recoms = new ArrayList<>();
        }
        displayItems(recoms);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterRecoms(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRecoms(newText);
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void filterRecoms(String query) {
        Locale locale = Locale.getDefault();
        String lowerCaseQuery = query.toLowerCase(locale);

        ArrayList<Recom> filteredRecoms = (ArrayList<Recom>) recoms.stream()
                .filter(recom -> recom.getTitle().toLowerCase(locale).contains(lowerCaseQuery)
                        || recom.getOverview().toLowerCase(locale).contains(lowerCaseQuery))
                .collect(Collectors.toList());
        displayItems(filteredRecoms);
    }

    private void displayItems(ArrayList<Recom> recoms) {
        this.recoms = recoms;
        if (recomAdapter != null) {
            recomAdapter.setRecoms(recoms);
        }
    }

    private void initViews() {
        viewedItemsManager = new RecentlyViewedItemsManager(this);
        SharedPreferences sharedPreferences = getSharedPreferences("YourPreferenceName", MODE_PRIVATE);
        Gson gson = new Gson();

        recomAdapter = new RecomAdapter(new ArrayList<>(), this, viewedItemsManager, sharedPreferences, gson);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setAdapter(recomAdapter);

        profile_button.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        home_button.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(intent);
            //  finish();
        });

        create_button.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, CreateActivity.class);
            startActivity(intent);
        });

        liked_button.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, LikedActivity.class);
            startActivity(intent);
        });

        back_button.setOnClickListener(v -> finish());
    }

    private void findViews() {
        home_button = findViewById(R.id.home_button);
        liked_button = findViewById(R.id.liked_button);
        create_button = findViewById(R.id.create_button);
        profile_button = findViewById(R.id.profile_button);
        back_button = findViewById(R.id.back_button);
        discover_title = findViewById(R.id.discover_title);
        searchView = findViewById(R.id.searchView);
        recycler_view = findViewById(R.id.recycler_view);
        search_button = findViewById(R.id.search_button);
    }
}