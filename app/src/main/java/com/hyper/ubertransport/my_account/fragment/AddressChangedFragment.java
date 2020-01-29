package com.hyper.ubertransport.my_account.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.Toast;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.registration.fragment.PostalAdressFragment;
import com.hyper.ubertransport.registration.usable.AddressInfo;
import com.hyper.ubertransport.utils.Checker;
import com.hyper.ubertransport.utils.UsernameFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddressChangedFragment extends PostalAdressFragment {

    @Override
    public void setUpButtonListeners() {

        getHeaderView().setText(getString(R.string.update_account));
        getNextButton().setOnClickListener(new View.OnClickListener() {
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
    public void setInformation() {
        AddressInfo adressInfo = new AddressInfo();
        adressInfo.setCity(getCityInputLayout().getEditText().getText().toString());
        adressInfo.setStreet(getStreetInputLayout().getEditText().getText().toString());
        adressInfo.setFlat(getFlatInputLayout().getEditText().getText().toString());

        DocumentReference adress = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        adress.update(UsernameFirestore.adress.name(), adressInfo.getAdressMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Updated Successfully",
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
}
