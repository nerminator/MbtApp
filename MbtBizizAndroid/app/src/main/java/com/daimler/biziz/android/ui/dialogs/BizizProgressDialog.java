package com.daimler.biziz.android.ui.dialogs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.daimler.biziz.android.R;

public class BizizProgressDialog {
    private AlertDialog progressDialog;

    public static BizizProgressDialog getDialog(){
        return new BizizProgressDialog();
    }

    public void show(Context c) {
        if (progressDialog ==null){
            progressDialog = ProgressDialog.show(c, "", "");
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.loading);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void dismiss() {
        if (progressDialog==null||!progressDialog.isShowing()) {
            return;
        }
        progressDialog.dismiss();
    }

    private BizizProgressDialog(){}

}
