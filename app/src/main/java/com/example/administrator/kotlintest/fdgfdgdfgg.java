package com.example.administrator.kotlintest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;


/**
 * Created by wol on 2017/8/8.
 */

public class fdgfdgdfgg extends Activity {

    private boolean isRemember;
    private TextView rememberPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isRemember) {
            setimg(R.drawable.icon_selected);
        } else {
            rememberPassword.setCompoundDrawables(getDrawable(R.drawable.icon_selected), null, null, null);
        }
        isRemember = !isRemember;

    }

    private void setimg(int img) {
        rememberPassword.setCompoundDrawables(getDrawable(img), null, null, null);
    }
}
