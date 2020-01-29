package com.hyper.ubertransport.registration.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.registration.RegistrationActivity;
import com.hyper.ubertransport.registration.usable.RegistrationFragmentInterface;
import com.hyper.ubertransport.registration.usable.UserInfo;
import com.hyper.ubertransport.validators_patterns.CCVValidator;
import com.hyper.ubertransport.validators_patterns.CreditCardNumberValidator;
import com.hyper.ubertransport.validators_patterns.Validable;

public class CreditCardInformationFragment extends Fragment implements Validable, RegistrationFragmentInterface {


    private final int NEXTPAGE=3;
    private Button nextButton;

    private CreditCardNumberValidator creditCardNumberValidator;
    private CCVValidator ccvValidator;

    private TextInputLayout creditCardLayout;
    private TextInputLayout ccvLayout;
    private UserInfo userInfo;
    private TextView headerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_creditcard_info, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {

            userInfo = (UserInfo) bundle.getSerializable("user_info");
        }


        nextButton=view.findViewById(R.id.button_next_detail2);
        creditCardLayout=view.findViewById(R.id.cardnumber_textinput_layout);
        ccvLayout=view.findViewById(R.id.ccv_textinput_layout);
        headerView = view.findViewById(R.id.createAcoount3);


        createValidationPatterns();
        setUpButtonListeners();

        return view;
    }



    @Override
    public void setUpButtonListeners(){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    try {
                        setInformation();
                        ((RegistrationActivity) getActivity()).changeFragment(NEXTPAGE);
                    } catch (Exception e) {
                        Log.i("CreditCardFragment", "exception");
                    }
                }
            }
        });
    }

    @Override
    public void createValidationPatterns(){
        creditCardNumberValidator=new CreditCardNumberValidator(creditCardLayout, getString(R.string.error_card_number), getString(R.string.error_blank));
        ccvValidator=new CCVValidator(ccvLayout, getString(R.string.error_ccv), getString(R.string.error_blank));
    }

    @Override
    public boolean validate() {


        creditCardLayout.setError(null);
        ccvLayout.setError(null);

        return ccvValidator.validate()&creditCardNumberValidator.validate();
    }


    @Override
    public void setInformation() {
        if (userInfo == null) return;

        userInfo.getCard().setCreditCardNumber(creditCardLayout.getEditText().getText().toString());
        userInfo.getCard().setCcv(ccvLayout.getEditText().getText().toString());

    }


    protected Button getNextButton() {
        return nextButton;
    }

    protected TextInputLayout getCreditCardLayout() {
        return creditCardLayout;
    }

    protected TextInputLayout getCcvLayout() {
        return ccvLayout;
    }

    protected TextView getHeaderView() {
        return headerView;
    }
}
