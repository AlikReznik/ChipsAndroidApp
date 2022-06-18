package com.example.chips_androidstudioproject.Dialogs;

import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.chips_androidstudioproject.R;
import com.example.chips_androidstudioproject.modules.DataFlowControl;

public class loading_custom_dialog {
    /*
    *   Loading dialog
    * */
    private AlertDialog dialog;

    public loading_custom_dialog(){}
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DataFlowControl.activity);

        LayoutInflater inflater = DataFlowControl.activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_custom_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
    public void dismissLoadingDialog(){
        dialog.dismiss();
    }
}
