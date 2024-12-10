package com.daimlertruck.dtag.internal.android.mbt.test.ui.login;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneNumberTextWatcher implements TextWatcher {

    private EditText editText;
    private boolean isFormatting;

    public PhoneNumberTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isFormatting) {
            isFormatting = true;
            formatPhoneNumber(s);
            isFormatting = false;
        }
    }

    private void formatPhoneNumber(Editable s) {
        // Remove previous hyphens
        String phone = s.toString().replaceAll("-", "");

        // Insert hyphens according to the format "XXX-XXX-XX-XX"
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < phone.length(); i++) {
            if (i == 3 || i == 6 || i == 8) {
                formatted.append("-");
            }
            formatted.append(phone.charAt(i));
        }

        // Set the formatted text back to the EditText
        editText.setText(formatted.toString());

        // Move the cursor to the end of the text
        editText.setSelection(formatted.length());
    }
}
