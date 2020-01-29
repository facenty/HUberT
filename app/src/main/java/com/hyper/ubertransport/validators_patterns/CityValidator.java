package com.hyper.ubertransport.validators_patterns;

import com.google.android.material.textfield.TextInputLayout;

public class CityValidator extends AbstractValidator {

    public CityValidator(TextInputLayout textInputLayout, String message, String messageIfBlank) {
        super(textInputLayout, message, messageIfBlank);
        String patternString = "[a-zA-Z0-9\\._\\-]{3,}";
        compilePattern(patternString);
    }
}

