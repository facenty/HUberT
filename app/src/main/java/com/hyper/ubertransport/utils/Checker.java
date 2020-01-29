package com.hyper.ubertransport.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.fragment.app.FragmentManager;
import android.widget.Toast;

import com.hyper.ubertransport.registration.fragment.NoInternetConnectionDialogFragment;

import java.util.Objects;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Checker {

    public static boolean checkInternetConnection(Context context, FragmentManager fragmentManager){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        boolean result = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!result) {

            openNoConnectionFragment(fragmentManager);
            Toast.makeText(context, "No internet connection.",
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    private static void openNoConnectionFragment(FragmentManager fragmentManager) {
        NoInternetConnectionDialogFragment dialogFragment = new NoInternetConnectionDialogFragment();
        dialogFragment.show(fragmentManager, "No internet connection");
    }
}
