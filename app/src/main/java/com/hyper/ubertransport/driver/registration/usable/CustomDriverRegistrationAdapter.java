package com.hyper.ubertransport.driver.registration.usable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hyper.ubertransport.driver.registration.fragment.ConfirmationFragment;
import com.hyper.ubertransport.driver.registration.fragment.IntroFragment;
import com.hyper.ubertransport.driver.registration.fragment.LicenseFragment;

public class CustomDriverRegistrationAdapter extends FragmentPagerAdapter {

    private static final Integer NUM_ITEMS = 3;
    private final Bundle bundle;

    public CustomDriverRegistrationAdapter(FragmentManager fm) {
        super(fm);
        this.bundle = new Bundle();
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment;

        switch (i) {
            case 0:
                fragment = new IntroFragment();
                break;
            case 1:
                fragment = new LicenseFragment();
                break;
            case 2:
                fragment = new ConfirmationFragment();
                break;
            default:
                fragment = null;
                break;
        }

        if (fragment != null) {
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}