package com.hyper.ubertransport.driver.registration.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.driver.registration.DriverRegistrationActivity;
import com.hyper.ubertransport.utils.Editable;

public class IntroFragment extends Fragment implements Editable {

    private final Integer NEXT_PAGE = 1;
    private Button nextButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_registration_intro, container, false);

        nextButton = view.findViewById(R.id.driver_registration_next_button);

        setUpButtonListeners();

        return view;
    }

    @Override
    public void createValidationPatterns() {

    }

    @Override
    public void setUpButtonListeners() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((DriverRegistrationActivity) getActivity()).changeFragment(NEXT_PAGE);
                } catch (Exception e) {
                    Log.i("Intro", e.getMessage());
                }
            }
        });
    }
}