package com.example.recom.Models;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class Recom {


    private String overview;
    private String title;
    private String category;
    private String username;
    private String postedOn;
    private String link;
    private float rating;
    private String image;
    private boolean isFavorite;

    public Recom() {
    }

    public Recom(float rating, String overview, String category, String link, String image, String postedOn) {
        this.rating = rating;
        this.overview = overview;
        this.category = category;
        this.link = link;
        this.image = image;
        this.username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        this.postedOn = postedOn;
    }


    public boolean isFavorite() {
        return isFavorite;
    }

    public Recom setFavorite(boolean favorite) {
        isFavorite = favorite;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public Recom setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Recom setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Recom setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Recom setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPostedOn() {
        return postedOn;

    }

    public Recom setPostedOn(String postedOn) {
        this.postedOn = postedOn;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Recom setLink(String link) {
        this.link = link;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public Recom setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Recom setImage(String image) {
        this.image = image;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recom recom = (Recom) o;
        return title.equals(recom.title) &&
                username.equals(recom.username) &&
                postedOn.equals(recom.postedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, username, postedOn);
    }
}
