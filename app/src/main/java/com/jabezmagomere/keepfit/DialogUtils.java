package com.jabezmagomere.keepfit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils {
    public static ProgressDialog showProgressDialog(Context context, String Message){
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setMessage(Message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }
}
