package com.hyper.ubertransport.registration.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.registration.RegistrationActivity;
import com.hyper.ubertransport.registration.usable.RegistrationFragmentInterface;
import com.hyper.ubertransport.registration.usable.UserInfo;
import com.hyper.ubertransport.validators_patterns.EmailValidator;
import com.hyper.ubertransport.validators_patterns.PasswordValidator;
import com.hyper.ubertransport.validators_patterns.Validable;

public class UsernamePasswordFragment extends Fragment implements Validable, RegistrationFragmentInterface {

    private final int NEXT_PAGE = 4;
    private Button nextButton;

    private PasswordValidator passwordValidator;
    private EmailValidator emailValidator;

    private TextInputLayout passwordLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout repeatPasswordLayout;

    private UserInfo userInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_username_password, container, false);

        userInfo = (UserInfo) getArguments().getSerializable("user_info");

        nextButton = view.findViewById(R.id.button_next_detail3);
        passwordLayout = view.findViewById(R.id.password_textinput_layout);
        emailLayout = view.findViewById(R.id.email_textinput_layout);
        repeatPasswordLayout = view.findViewById(R.id.repeatpassword_textinput_layout);

        createValidationPatterns();
        setUpButtonListeners();

        return view;
    }


    @Override
    public void setUpButtonListeners() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    try {
                        setInformation();
                        ((RegistrationActivity) getActivity()).changeFragment(NEXT_PAGE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void createValidationPatterns() {
        passwordValidator = new PasswordValidator(passwordLayout, getString(R.string.error_password), getString(R.string.error_blank));
        emailValidator = new EmailValidator(emailLayout, getString(R.string.error_password), getString(R.string.error_blank));
    }


    @Override
    public boolean validate() {

        passwordLayout.setError(null);
        repeatPasswordLayout.setError(null);
        emailLayout.setError(null);

        boolean passwordMatches = false;
        String password = passwordLayout.getEditText().getText().toString();
        String repeatPassword = repeatPasswordLayout.getEditText().getText().toString();

        passwordMatches = password.equals(repeatPassword);

        if (!passwordMatches) {
            repeatPasswordLayout.setError(getString(R.string.error_repeat_password));
        }

        return emailValidator.validate() & passwordValidator.validate() & passwordMatches;
    }

    @Override
    public void setInformation() {
        if (userInfo == null) return;

        userInfo.setPassword(passwordLayout.getEditText().getText().toString());
        userInfo.setEmail(emailLayout.getEditText().getText().toString());


    }
}
