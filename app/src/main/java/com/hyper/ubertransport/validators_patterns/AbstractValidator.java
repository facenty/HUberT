package com.hyper.ubertransport.validators_patterns;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractValidator {

    private Pattern pattern;
    private TextInputLayout textInputLayout;
    private String message;
    private String messageIfBlank;


    AbstractValidator() {
    }

    AbstractValidator(TextInputLayout textInputLayout, String message, String messageIfBlank) {

        this.textInputLayout = textInputLayout;
        this.message = message;
        this.messageIfBlank = messageIfBlank;
    }

    public boolean validate(String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public boolean validate() {
        if (textInputLayout == null) return false;
        boolean result = true;
        String input = textInputLayout.getEditText().getText().toString();
        Matcher matcher = pattern.matcher(input);
        if (input.length() != 0) {
            if (!matcher.matches()) {
                textInputLayout.setError(message);
                result = false;
            }

        } else {
            textInputLayout.setError(messageIfBlank);
            result = false;
        }

        return result;
    }

    void compilePattern(String patternString) {
        this.pattern = Pattern.compile(patternString);
    }


}
