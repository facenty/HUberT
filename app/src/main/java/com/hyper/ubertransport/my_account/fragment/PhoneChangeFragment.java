package com.hyper.ubertransport.my_account.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.utils.UsernameFirestore;
import com.hyper.ubertransport.validators_patterns.PhoneValidator;

public class PhoneChangeFragment extends DataFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setNameFirestore(UsernameFirestore.phone);
        setValidator(new PhoneValidator(getTextInputLayout(), getString(R.string.error_phone_number), getString(R.string.error_blank)));

        setSubheaderText(getString(R.string.phone_change_layout));
        getTextInputLayout().getEditText().setHint(getString(R.string.phone));
        getTextInputLayout().getEditText().setInputType(InputType.TYPE_CLASS_PHONE);

        return view;
    }
}
