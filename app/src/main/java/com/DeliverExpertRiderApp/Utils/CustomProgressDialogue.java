package com.DeliverExpertRiderApp.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.expertorider.R;


public class CustomProgressDialogue extends Dialog {
    public CustomProgressDialogue(Context context) {
        super(context);

        WindowManager.LayoutParams wlmp = getWindow().getAttributes();

        wlmp.gravity = Gravity.CENTER;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(dialog -> onBackPressed());
        View view = LayoutInflater.from(context).inflate(
                R.layout.custom_progress_layout, null);
        setContentView(view);

    }
}