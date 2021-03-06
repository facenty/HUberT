package com.hyper.ubertransport.login;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.maps.MapsActivity;
import com.hyper.ubertransport.registration.RegistrationActivity;
import com.hyper.ubertransport.utils.Checker;
import com.hyper.ubertransport.utils.Editable;
import com.hyper.ubertransport.validators_patterns.EmailValidator;
import com.hyper.ubertransport.validators_patterns.PasswordValidator;
import com.hyper.ubertransport.validators_patterns.Validable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements Validable, Editable {

    private static final String TAG = "logowanie_email";
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;

    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;

    private Button regButton;
    private Button subButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        subButton = findViewById(R.id.submit);
        regButton = findViewById(R.id.register);
        emailInputLayout = findViewById(R.id.email_login_layout);
        passwordInputLayout = findViewById(R.id.password_login_layout);

        createValidationPatterns();
        setUpButtonListeners();
    }

    private void signin() {

        String email = emailInputLayout.getEditText().getText().toString();
        String password = passwordInputLayout.getEditText().getText().toString();

        //firebase check
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.v(TAG, "Zalogowano");

                            android.content.Intent myIntent = new android.content.Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(myIntent);
                            finish();

                        } else {
                            Log.v(TAG, "Błąd logowania", task.getException());
                            Toast.makeText(LoginActivity.this, "Błąd logowania.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public boolean validate() {
        return emailValidator.validate() & passwordValidator.validate();
    }

    @Override
    public void createValidationPatterns() {
        emailValidator = new EmailValidator(emailInputLayout, getString(R.string.error_email), getString(R.string.error_blank));
        passwordValidator = new PasswordValidator(passwordInputLayout, getString(R.string.error_password), getString(R.string.error_blank));
    }

    @Override
    public void setUpButtonListeners() {

        regButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.content.Intent myIntent = new android.content.Intent(v.getContext(), RegistrationActivity.class);
                startActivity(myIntent);
            }
        });

        subButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Checker.checkInternetConnection(getApplicationContext(), getSupportFragmentManager())) {
                    if (validate())
                        signin();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
