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
import com.hyper.ubertransport.validators_patterns.NameValidator;

import java.util.Objects;

public class NameChangeFragment extends DataFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setNameFirestore(UsernameFirestore.name);
        setValidator(new NameValidator(getTextInputLayout(), getString(R.string.error_name), getString(R.string.error_blank)));
        setSubheaderText(getString(R.string.name_change_layout));
        Objects.requireNonNull(getTextInputLayout().getEditText()).setHint(getString(R.string.name));
        getTextInputLayout().getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        return view;
    }
}
