package com.example.cookmaster.model;

import java.io.Serializable;

public class Option implements Serializable {
    private int optionIcon;
    private String optionName;


    public Option(int optionIcon, String optionName) {
        this.optionIcon = optionIcon;
        this.optionName = optionName;
    }

    public Option() {
    }



    public int getOptionIcon() {
        return optionIcon;
    }

    public void setOptionIcon(int optionIcon) {
        this.optionIcon = optionIcon;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
}
