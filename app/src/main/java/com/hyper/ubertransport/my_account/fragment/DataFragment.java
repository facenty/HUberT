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
import com.hyper.ubertransport.utils.UsernameFirestore;
import com.hyper.ubertransport.validators_patterns.AbstractValidator;
import com.hyper.ubertransport.validators_patterns.Validable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class DataFragment extends Fragment implements Validable, RegistrationFragmentInterface {

    private TextView subheaderView;
    private TextInputLayout textInputLayout;
    private UsernameFirestore nameFirestore;
    private AbstractValidator validator;
    private Button submitButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_data_change, container, false);

        TextView headerView = view.findViewById(R.id.header);
        subheaderView = view.findViewById(R.id.subheader);
        textInputLayout = view.findViewById(R.id.textinput_layout);
        submitButton = view.findViewById(R.id.button_data_fragment);
        headerView.setText(getString(R.string.update_account));

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

    }

    @Override
    public void setInformation() {

        DocumentReference document = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        document.update(nameFirestore.name(), textInputLayout.getEditText().getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity().getBaseContext(), "Updated Successfully",
                                Toast.LENGTH_LONG).show();
                        closeFragment();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error updating",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void closeFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public boolean validate() {
        return validator.validate();
    }


    void setSubheaderText(String subheaderText) {
        subheaderView.setText(subheaderText);
    }

    public void setTextInputLayoutHint(String textInputLayoutHint) {
        textInputLayout.getEditText().setHint(textInputLayoutHint);
    }

    public UsernameFirestore getNameFirestore() {
        return nameFirestore;
    }

    void setNameFirestore(UsernameFirestore nameFirestore) {
        this.nameFirestore = nameFirestore;
    }

    public AbstractValidator getValidator() {
        return validator;
    }

    void setValidator(AbstractValidator validator) {
        this.validator = validator;
    }

    TextInputLayout getTextInputLayout() {
        return textInputLayout;
    }

    public void setTextInputLayout(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }
}
