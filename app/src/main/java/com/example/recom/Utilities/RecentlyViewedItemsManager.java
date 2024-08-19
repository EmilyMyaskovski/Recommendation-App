package com.example.recom.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.recom.Models.Recom;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecentlyViewedItemsManager {

    private static final String PREF_NAME = "recently_viewed_items";
    private static final String KEY_RECENT_ITEMS = "recent_items";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public RecentlyViewedItemsManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public ArrayList<Recom> getItems() {
        String json = sharedPreferences.getString(KEY_RECENT_ITEMS, null);
        Type type = new TypeToken<ArrayList<Recom>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    public void saveItems(ArrayList<Recom> items) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(items);
        editor.putString(KEY_RECENT_ITEMS, json);
        editor.apply();
    }


    public void updateItemFavoriteStatus(String title, boolean isFavorite) {
        ArrayList<Recom> items = getItems();
        for (Recom item : items) {
            if (item.getTitle().equals(title)) {
                item.setFavorite(isFavorite);
                break;
            }
        }
        saveItems(items);
    }
}
