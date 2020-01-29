package com.hyper.ubertransport.validators_patterns;

import com.google.android.material.textfield.TextInputLayout;

public class FlatValidator extends AbstractValidator {

    public FlatValidator(TextInputLayout textInputLayout, String message, String messageIfBlank) {
        super(textInputLayout, message, messageIfBlank);
        String patternString = "\\d{1,}";
        compilePattern(patternString);
    }
}
