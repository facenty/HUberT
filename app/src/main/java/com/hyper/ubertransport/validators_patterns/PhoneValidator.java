package com.hyper.ubertransport.validators_patterns;

import com.google.android.material.textfield.TextInputLayout;

public class PhoneValidator extends AbstractValidator {

    public PhoneValidator(TextInputLayout textInputLayout, String message, String messageIfBlank) {
        super(textInputLayout, message, messageIfBlank);
        String patternString = "\\d{9}";
        compilePattern(patternString);
    }
}
