package com.hyper.ubertransport.my_account.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.registration.usable.RegistrationFragmentInterface;
import com.hyper.ubertransport.utils.Checker;
import com.hyper.ubertransport.validators_patterns.PasswordValidator;
import com.hyper.ubertransport.validators_patterns.Validable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordChangeFragment extends Fragment implements Validable, RegistrationFragmentInterface {


    private Button submitButton;

    private PasswordValidator newPasswordValidator;
    private PasswordValidator oldPasswordValidator;
    private TextInputLayout oldPasswordLayout;
    private TextInputLayout newPasswordLayout;
    private TextInputLayout repeatPasswordLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);


        submitButton = view.findViewById(R.id.button_submit_change_password);
        TextView headerView = view.findViewById(R.id.header);
        TextView subheaderView = view.findViewById(R.id.subheader);
        headerView.setText(getString(R.string.update_account));
        subheaderView.setText(getString(R.string.password_change_layout));
        oldPasswordLayout = view.findViewById(R.id.textinput_layout);
        oldPasswordLayout.getEditText().setHint(R.string.old_password);
        newPasswordLayout = view.findViewById(R.id.password_textinput_layout);
        repeatPasswordLayout = view.findViewById(R.id.repeatpassword_textinput_layout);

        createValidationPatterns();
        setUpButtonListeners();

        return view;
    }


    @Override
    public void setUpButtonListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Checker.checkInternetConnection(getContext(), getFragmentManager())) {
                    if (validate()) {
                        try {
                            setInformation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }

    @Override
    public void createValidationPatterns() {
        newPasswordValidator = new PasswordValidator(newPasswordLayout, getString(R.string.error_password), getString(R.string.error_blank));
        oldPasswordValidator = new PasswordValidator(newPasswordLayout, getString(R.string.error_password), getString(R.string.error_blank));
    }


    @Override
    public boolean validate() {

        oldPasswordLayout.setError(null);
        newPasswordLayout.setError(null);
        repeatPasswordLayout.setError(null);

        boolean passwordMatches = false;
        String password = newPasswordLayout.getEditText().getText().toString();
        String repeatPassword = repeatPasswordLayout.getEditText().getText().toString();

        passwordMatches = password.equals(repeatPassword);

        if (!passwordMatches) {
            repeatPasswordLayout.setError(getString(R.string.error_repeat_password));
        }

        return oldPasswordValidator.validate() & newPasswordValidator.validate() & passwordMatches;
    }

    @Override
    public void setInformation() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldPasswordLayout.getEditText().getText().toString());

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPasswordLayout.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity().getBaseContext(), "Updated Successfully",
                                                Toast.LENGTH_LONG).show();
                                        closeFragment();
                                    } else {
                                        Toast.makeText(getActivity().getBaseContext(), "Password not updated",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Auth failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }


    private void closeFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();


    }
}
