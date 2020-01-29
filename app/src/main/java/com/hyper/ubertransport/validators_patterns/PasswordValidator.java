package com.hyper.ubertransport.validators_patterns;

import com.google.android.material.textfield.TextInputLayout;

public class PasswordValidator extends AbstractValidator {

    public PasswordValidator(TextInputLayout textInputLayout, String message, String messageIfBlank) {
        super(textInputLayout, message, messageIfBlank);
        String patternString = "\\w{6,}";
        compilePattern(patternString);
    }
}
