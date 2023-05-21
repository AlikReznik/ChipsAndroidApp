package com.example.chips.dialogs;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import com.example.chips.R;
import com.example.chips.utils.DataFlowControl;

public class LoadingCustomDialog {
    /*
    *   Loading dialog for waiting for firebase return
    * */
    /*
    *   Loading dialog variables:
    * */
    private AlertDialog dialog;

    /*
     *   LoadingCustomDialog:
     *      Loading dialog constructor
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public LoadingCustomDialog(){}

    /*
     *   startLoadingDialog:
     *      Creates dialog, connects the layout, starts it
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DataFlowControl.activity);

        LayoutInflater inflater = DataFlowControl.activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_custom_loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();}

    /*
     *   dismissLoadingDialog:
     *      Stops the loading dialog
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public void dismissLoadingDialog(){
        dialog.dismiss();
    }
}
