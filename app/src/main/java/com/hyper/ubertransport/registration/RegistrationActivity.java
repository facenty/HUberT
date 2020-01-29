package com.hyper.ubertransport.registration;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.registration.usable.CustomRegistrationAdapter;
import com.hyper.ubertransport.registration.usable.CustomViewPager;
import com.hyper.ubertransport.registration.usable.UserInfo;

public class RegistrationActivity extends AppCompatActivity {

    private CustomViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        UserInfo userInfo = new UserInfo();

        viewPager=findViewById(R.id.registration_viewpager);
        CustomRegistrationAdapter customRegistrationAdapter=new CustomRegistrationAdapter(getSupportFragmentManager(), userInfo);
        viewPager.setAdapter(customRegistrationAdapter);
    }




    public void changeFragment(int position){
        viewPager.setCurrentItem(position);
    }


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }
        else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

}
