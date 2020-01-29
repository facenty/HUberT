package com.hyper.ubertransport.order.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.maps.MapsActivity;
import com.hyper.ubertransport.order.usable.DistanceLoader;
import com.hyper.ubertransport.order.usable.OrderInfo;
import com.hyper.ubertransport.utils.Checker;
import com.hyper.ubertransport.utils.Editable;
import com.hyper.ubertransport.validators_patterns.PhoneValidator;
import com.hyper.ubertransport.validators_patterns.Validable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class SummaryFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<Long>, Editable, Validable {

    private final int LOADER_ID = 1;
    private OrderInfo orderInfo;
    private TextInputLayout recipientData;
    private TextInputLayout recipientPhone;
    private PhoneValidator phoneValidator;
    private Button orderButton;
    private TextView priceTextView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle = getArguments();
        orderInfo = (OrderInfo) bundle.getSerializable("order_info");

        final View view = inflater.inflate(R.layout.fragment_summary, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_ordersummary);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
        recipientData = view.findViewById(R.id.textinput_receipient_name);
        recipientPhone = view.findViewById(R.id.textinput_receipient_phone_number);
        orderButton = view.findViewById(R.id.acceptance_button);
        TextView pickUpTextView = view.findViewById(R.id.pickup_textview);
        TextView destinationTextView = view.findViewById(R.id.destination_textview);
        priceTextView = view.findViewById(R.id.amount_price_textview);
        pickUpTextView.setText(orderInfo.getFromName());
        destinationTextView.setText(orderInfo.getDestinationName());
        createValidationPatterns();
        setUpButtonListeners();
        getLoader();

        return view;
    }

    @Override
    public void createValidationPatterns() {
        phoneValidator = new PhoneValidator(recipientPhone, getString(R.string.error_phone_number), getString(R.string.error_blank));
    }

    @Override
    public void setUpButtonListeners() {

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (Checker.checkInternetConnection(getContext(), getFragmentManager())) {
                        orderInfo.setRecipient(recipientData.getEditText().getText().toString());
                        orderInfo.setRecipientPhone(recipientPhone.getEditText().getText().toString());
                        orderInfo.setUserID(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        sendOrderInfo();
                        Toast toast = Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG);
                        toast.show();
                        Log.i("INFO", orderInfo.getOrderInfoMap().toString());
                        closeFragment();
                    }
                }
            }
        });
    }

    private void sendOrderInfo() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("packages").document()
                .set(orderInfo.getOrderInfoMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.w(TAG, "DocumentSnapshot succesfully added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public boolean validate() {
        recipientData.setError(null);
        boolean receipientEmpty = TextUtils.isEmpty(recipientData.getEditText().getText().toString());
        if (receipientEmpty) {
            recipientData.setError(getString(R.string.error_blank));
        }
        return phoneValidator.validate() & !receipientEmpty;
    }

    @NonNull
    @Override
    public Loader<Long> onCreateLoader(int i, @Nullable Bundle bundle) {

        Context context = getContext();
        if (context != null) {
            return new DistanceLoader(context, orderInfo.getFrom(), orderInfo.getDestination());
        }

        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Long> loader, Long aLong) {

        Log.i("Loader", "Finished working");
        orderInfo.setPrice(String.valueOf(roundTwoDecimals(10 + orderInfo.multiplier() * (double) (aLong / 1000))));
        Log.i("aLong", String.valueOf(aLong));
        Log.i("multi", String.valueOf(orderInfo.multiplier()));
        updateUI(orderInfo.getPrice());//PRICE
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Long> loader) {
        updateUI("*'*");
    }


    private void updateUI(String price) {
        if (priceTextView != null)
            priceTextView.setText(price);
    }

    private void getLoader() {
        if (getActivity().getSupportLoaderManager().getLoader(LOADER_ID) == null)
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        else
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    private void closeFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
//                        .setCustomAnimations(R.anim, R.anim.push_up_out)
                .remove(this)
                .commit();

        ((MapsActivity) getActivity()).showPlaceAutoCompletePickUpFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopLoader();
    }

    private void stopLoader() {
        getLoaderManager().destroyLoader(LOADER_ID);
    }

    private double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
}
