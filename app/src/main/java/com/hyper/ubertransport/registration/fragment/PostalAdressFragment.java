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
import com.hyper.ubertransport.validators_patterns.CityValidator;
import com.hyper.ubertransport.validators_patterns.FlatValidator;
import com.hyper.ubertransport.validators_patterns.StreetValidator;
import com.hyper.ubertransport.validators_patterns.Validable;

public class PostalAdressFragment extends Fragment implements Validable, RegistrationFragmentInterface {


    private final int NEXT_PAGE=2;
    private Button nextButton;

    private FlatValidator flatValidator;
    private StreetValidator streetValidator;
    private CityValidator cityValidator;

    private TextInputLayout flatInputLayout;
    private TextInputLayout streetInputLayout;
    private TextInputLayout cityInputLayout;
    private TextView headerView;

    private UserInfo userInfo;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_postaladress, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userInfo = (UserInfo) bundle.getSerializable("user_info");
        }

        flatInputLayout=view.findViewById(R.id.flat_textinput_layout);
        streetInputLayout=view.findViewById(R.id.street_textinput_layout);
        cityInputLayout=view.findViewById(R.id.city_textinput_layout);
        headerView = view.findViewById(R.id.createAcoount1);

        nextButton=view.findViewById(R.id.button_next_detail1);


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
                        ((RegistrationActivity) getActivity()).changeFragment(NEXT_PAGE);
                    } catch (Exception e) {
                        Log.i("PostalAdressFragment", "exception");
                    }

                }
            }
        });
    }

    @Override
    public void createValidationPatterns(){
        flatValidator=new FlatValidator(flatInputLayout, getString(R.string.error_flat), getString(R.string.error_blank));
        streetValidator=new StreetValidator(streetInputLayout, getString(R.string.error_street), getString(R.string.error_blank));
        cityValidator=new CityValidator(cityInputLayout, getString(R.string.error_city), getString(R.string.error_blank));
    }

    @Override
    public boolean validate() {


        flatInputLayout.setError(null);
        streetInputLayout.setError(null);
        cityInputLayout.setError(null);


       return flatValidator.validate()&streetValidator.validate()&cityValidator.validate();
    }

    @Override
    public void setInformation() {
        if (userInfo == null) return;

        userInfo.getAdress().setFlat(flatInputLayout.getEditText().getText().toString());
        userInfo.getAdress().setStreet(streetInputLayout.getEditText().getText().toString());
        userInfo.getAdress().setCity(cityInputLayout.getEditText().getText().toString());

    }

    protected Button getNextButton() {
        return nextButton;
    }


    protected TextInputLayout getFlatInputLayout() {
        return flatInputLayout;
    }

    protected TextInputLayout getStreetInputLayout() {
        return streetInputLayout;
    }

    protected TextInputLayout getCityInputLayout() {
        return cityInputLayout;
    }

    protected TextView getHeaderView() {
        return headerView;
    }
}
