package com.hyper.ubertransport.my_account.usable;

import androidx.fragment.app.Fragment;

public class UserData {

    private String description;
    private String value;
    private Fragment fragment;


    public UserData(String description, String value) {
        this.description = description;
        this.value = value;
    }


    public UserData(String description, String value, Fragment fragment) {
        this.description = description;
        this.value = value;
        this.fragment = fragment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
