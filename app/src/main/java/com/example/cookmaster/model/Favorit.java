package com.example.cookmaster.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Favorit {
    private String articleId;
    private String userId;

    public Favorit() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Favorit(String articleId, String userId) {
        this.articleId = articleId;
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
