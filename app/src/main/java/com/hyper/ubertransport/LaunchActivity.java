package com.hyper.ubertransport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.hyper.ubertransport.login.LoginActivity;
import com.hyper.ubertransport.maps.MapsActivity;
import com.hyper.ubertransport.utils.Checker;
import com.google.firebase.auth.FirebaseAuth;

public class LaunchActivity extends AppCompatActivity {

    //show logo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Context context = getApplicationContext();
        if (Checker.checkInternetConnection(context, getSupportFragmentManager())) {
            int SPLASH_TIME_OUT = 500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        Intent homeIntent = new Intent(LaunchActivity.this, LoginActivity.class);
                        startActivity(homeIntent);
                        finish();
                    } else {
                        Intent homeIntent1 = new Intent(LaunchActivity.this, MapsActivity.class);
                        startActivity(homeIntent1);
                        finish();
                    }

                }
            }, SPLASH_TIME_OUT);
        } else {
            finish();
        }
    }
}
