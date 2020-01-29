package com.hyper.ubertransport.validators_patterns;

import com.google.android.material.textfield.TextInputLayout;

public class CCVValidator extends AbstractValidator {

    public CCVValidator(TextInputLayout textInputLayout, String message, String messageIfBlank) {
        super(textInputLayout, message, messageIfBlank);
        String patternString = "\\d{3}";
        compilePattern(patternString);
    }
}
