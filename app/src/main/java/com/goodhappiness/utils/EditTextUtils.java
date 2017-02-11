package com.goodhappiness.utils;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.widget.EditText;

/**
 * Created by 电脑 on 2016/4/6.
 */
public class EditTextUtils {
    public static int editTextReduceCount(EditText editText, int buyUnit) {
        int count = Integer.valueOf(editText.getText().toString());
        if (count - buyUnit <= 0) {
            return count;
        } else {
            count = count - buyUnit;
        }
        if (count % buyUnit != 0 && count > buyUnit) {
            count = count - count % buyUnit;
        }
        editText.setText(count + "");
        Editable text = editText.getText();
        Spannable spanText = text;
        Selection.setSelection(spanText, text.length());
        return Integer.valueOf(editText.getText().toString());
    }

    public static int editTextAddCount(EditText editText, int buyUnit) {
        int count = Integer.valueOf(editText.getText().toString());
        count += buyUnit;
        if(count>=9999999){
            count = 9999999;
        }
        if (count % buyUnit != 0 && count > buyUnit) {
            count = count - count % buyUnit;
        }
        editText.setText(count + "");
        Editable text = editText.getText();
        Spannable spanText = text;
        Selection.setSelection(spanText, text.length());
        return Integer.valueOf(editText.getText().toString());
    }
}
