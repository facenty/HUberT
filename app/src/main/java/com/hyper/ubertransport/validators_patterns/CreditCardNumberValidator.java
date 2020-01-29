package com.hyper.ubertransport.validators_patterns;

import com.google.android.material.textfield.TextInputLayout;

public class CreditCardNumberValidator extends AbstractValidator {

    public CreditCardNumberValidator(TextInputLayout textInputLayout, String message, String messageIfBlank) {
        super(textInputLayout, message, messageIfBlank);
        String patternString = "\\d{16}";
        compilePattern(patternString);
    }
}
