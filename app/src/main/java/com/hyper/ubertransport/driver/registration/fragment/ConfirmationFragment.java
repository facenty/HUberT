package com.hyper.ubertransport.driver.registration.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.maps.MapsActivity;
import com.hyper.ubertransport.utils.Checker;
import com.hyper.ubertransport.utils.UsernameFirestore;
import com.hyper.ubertransport.validators_patterns.Validable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmationFragment extends Fragment implements Validable {

    private Button applyButton;
    private CheckBox checkBox;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_registration_confirm, container, false);

        applyButton = view.findViewById(R.id.driver_apply_button);
        checkBox = view.findViewById(R.id.driver_terms_checkbox);
        linearLayout = view.findViewById(R.id.checkbox_layout);
        progressBar = view.findViewById(R.id.driver_registration_progressbar);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (Checker.checkInternetConnection(getContext(), getFragmentManager())) {
                        setDriverAccount();
                        changeVisibilityDuringRegistration();
                    }
                }
            }
        });

        return view;
    }

    private void setDriverAccount() {
        DocumentReference driverAccount = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        driverAccount.update(UsernameFirestore.driverAccount.name(), true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Zostałeś kierowcą!",
                                Toast.LENGTH_LONG).show();

                        android.content.Intent myIntent = new android.content.Intent(getView().getRootView().getContext(), MapsActivity.class);
                        startActivity(myIntent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        changeVisibilityOnRegistrationFail();
                        Toast.makeText(getContext(), "Błąd aktualizacji danych",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public boolean validate() {
        checkBox.setError(null);
        if (checkBox.isChecked())
            return true;
        else {
            checkBox.setError(getString(R.string.error_terms_checkbox));
            return false;
        }
    }

    private void changeVisibilityDuringRegistration() {
        linearLayout.setVisibility(View.GONE);
        applyButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void changeVisibilityOnRegistrationFail() {
        linearLayout.setVisibility(View.VISIBLE);
        applyButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}