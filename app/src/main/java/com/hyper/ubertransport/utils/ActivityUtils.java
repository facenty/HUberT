package com.hyper.ubertransport.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

abstract class ActivityUtils {

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}