package com.example.siddharthgupta.healthcare;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by siddharth gupta on 24-02-2017.
 */
public class MyEditText extends EditText {
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void setError(CharSequence error, Drawable icon) {
        setCompoundDrawables(null, null, icon, null);
    }
}
