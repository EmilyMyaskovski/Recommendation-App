package com.example.recom.Models;

import java.util.Objects;

public class Post {
    private String postId;
    private String overview;
    private String title;
    private String category;
    private String username;
    private String postedOn;
    private String link;
    private float rating;
    private String image;
    private boolean isFavorite;
    private String postedOnString;

    public Post() {

    }

    public Post(String postId, float rating, String overview, String category, String link, String imageUri, String title, String username, String postedOn) {
        this.postId = postId;
        this.rating = rating;
        this.overview = overview;
        this.category = category;
        this.link = link;
        this.image = imageUri;
        this.title = title;
        this.username = username;
        this.postedOn = postedOn;
        this.isFavorite = false;
    }

    public String getPostId() {
        return postId;
    }
    public boolean isFavorite() {
        return isFavorite;
    }

    public Post setFavorite(boolean favorite) {
        isFavorite = favorite;
        return this;
    }

    public String getOverview() {
        return overview;
    }


    public String getTitle() {
        return title;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Post setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Post setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public String setPostedOn(String postedOn) {
        this.postedOn = postedOn;
        return postedOn;
    }

    public String getLink() {
        return link;
    }

    public Post setLink(String link) {
        this.link = link;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public Post setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public String getImage() {
        return image;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;

        return Objects.equals(title, post.title) &&
                Objects.equals(username, post.username) &&
                Objects.equals(postedOn, post.postedOn);
    }


    @Override
    public int hashCode() {
        return Objects.hash(title, username, postedOn);
    }
}
