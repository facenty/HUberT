package com.hyper.ubertransport.registration.usable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hyper.ubertransport.registration.fragment.BasicInformationFragment;
import com.hyper.ubertransport.registration.fragment.ConfirmationFragment;
import com.hyper.ubertransport.registration.fragment.CreditCardInformationFragment;
import com.hyper.ubertransport.registration.fragment.PostalAdressFragment;
import com.hyper.ubertransport.registration.fragment.UsernamePasswordFragment;

import javax.annotation.Nonnull;

public class CustomRegistrationAdapter extends FragmentPagerAdapter {

    private final Bundle bundle;

    public CustomRegistrationAdapter(FragmentManager fm, UserInfo userInfo) {
        super(fm);
        this.bundle = new Bundle();
        bundle.putSerializable("user_info", userInfo);
    }

    @Override
    @Nonnull
    public Fragment getItem(int i) {

        Fragment fragment;

        switch (i) {
            case 0:
                fragment = new BasicInformationFragment();
                break;
            case 1:
                fragment = new PostalAdressFragment();
                break;
            case 2:
                fragment = new CreditCardInformationFragment();
                break;
            case 3:
                fragment = new UsernamePasswordFragment();
                break;
            case 4:
                fragment = new ConfirmationFragment();
                break;
            default:
                fragment = null;
                break;
        }

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
