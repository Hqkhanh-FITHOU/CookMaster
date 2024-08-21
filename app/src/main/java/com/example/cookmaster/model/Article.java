package com.example.cookmaster.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class Article implements Serializable {
    private String id;
    private String title;
    private String image;
    private String imageAlt;
    private String introduction;
    private String instruction;
    private boolean published;
    private double star;
    private int view;
    private String createdUserId;
    private Date publishedAt;
    private String category;

    public Article() {
    }

    public Article(String id, String title, String image, String imageAlt, String introduction, String instruction, boolean published, double star, int view, String createdUserId, Date publishedAt, String category) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.imageAlt = imageAlt;
        this.introduction = introduction;
        this.instruction = instruction;
        this.published = published;
        this.star = star;
        this.view = view;
        this.createdUserId = createdUserId;
        this.publishedAt = publishedAt;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public void setImageAlt(String imageAlt) {
        this.imageAlt = imageAlt;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }


    public static List<String> getListOfCategories(){
        List<String> categories = new ArrayList<>();
        categories.add("Món gỏi");
        categories.add("Món nấu");
        categories.add("Món hấp");
        categories.add("Món nướng");
        categories.add("Món chiên");
        categories.add("Món kho");
        return categories;
    }

    public static String[] getArrayOfCategories(){
        String[] categories = {"Món gỏi","Món nấu","Món hấp","Món nướng","Món chiên","Món kho"};
        return categories;
    }
}
