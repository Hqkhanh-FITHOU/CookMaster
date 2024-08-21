package com.example.cookmaster.model;

public class Slider {
    private Integer resId;
    private String title;

    private String message;

    public Slider(Integer resId, String title, String message) {
        this.resId = resId;
        this.title = title;
        this.message = message;
    }

    public Slider() {
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
