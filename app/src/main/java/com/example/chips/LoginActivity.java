package com.example.chips;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chips.dialogs.LoadingCustomDialog;
import com.example.chips.services.InternetConnectionService;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.modules.User.UserAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    /*
    *   Login activity
    * */
    /*
     *   Layout variables
     * */
    private EditText EditText_Email;
    private EditText EditText_Password;
    private TextView TextView_Register;
    private Button Button_Login;
    private TextView TextView_ResetPassword;

    /*
     *   Activity variables
     * */
    private UserAuth userAuth;

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected void onStart() {
        super.onStart();

        initInfo();
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   login:
     *      Checks information and logins
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void login() {
        boolean emailStatus = UserAuth.emailValidator(EditText_Email.getText().toString());
        boolean passwords = EditText_Password.getText().toString().length() >= 6;
        if(emailStatus & passwords){
            DataFlowControl.loadingDialog.startLoadingDialog();
            userAuth.signInWithEmail(EditText_Email.getText().toString(), EditText_Password.getText().toString());
        }else{
            Toast.makeText(DataFlowControl.context, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     *   resetPasswordDialog:
     *      Opens reset password dialog
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void resetPasswordDialog(){
        final Dialog dialog = new Dialog(DataFlowControl.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_reset_password);

        final Button Button_cancel = dialog.findViewById(R.id.Button_Cancel);
        final Button Button_resetPassword = dialog.findViewById(R.id.Button_ResetPassword);
        final EditText EditText_email = dialog.findViewById(R.id.EditText_Email);

        Button_cancel.setOnClickListener((v) ->{
            dialog.dismiss();
        });
        Button_resetPassword.setOnClickListener(view ->
        {
            if(EditText_email.getText().toString().equals("")) {
                Toast.makeText(DataFlowControl.context, "To reset your password enter you email", Toast.LENGTH_SHORT).show();
            }else{
                DataFlowControl.loadingDialog.startLoadingDialog();
                DataFlowControl.userAuth.resetPassword(EditText_email.getText().toString());
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   initButtonListeners:
     *      Button listeners
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void initButtonListeners() {
        Button_Login.setOnClickListener(view ->
        {
            login();
        });

        TextView_ResetPassword.setOnClickListener(view ->
        {
            resetPasswordDialog();
        });

        TextView_Register.setOnClickListener(view ->
        {
            Intent intent = new Intent(DataFlowControl.context, SignUpActivity.class);
            startActivity(intent);
        });
    }

    /*
     *   initInfo:
     *      Sets layout information
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void initInfo() {
        /*
         *   DataFlow variables
         *  */
        DataFlowControl.loadingDialog = new LoadingCustomDialog();
        DataFlowControl.context = this;
        DataFlowControl.activity = this;

        /*
         *   Internet connection service starter
         *  */
        Intent serviceIntent = new Intent(DataFlowControl.context, InternetConnectionService.class);
        startService(serviceIntent);


        DataFlowControl.firestoreDb = FirebaseFirestore.getInstance();
        userAuth = new UserAuth();
        DataFlowControl.userAuth = userAuth;

        /* Check user auth => MainActivity / Loads Activity */

        if(DataFlowControl.authUser == null){
            DataFlowControl.loadingDialog.startLoadingDialog();
        }else{
            initLayout();
            initButtonListeners();
        }
    }

    /*
     *   initLayout:
     *      Sets layout variables
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void initLayout(){
        EditText_Email = findViewById(R.id.EditText_Email);
        EditText_Password = findViewById(R.id.EditText_Password);
        TextView_Register = findViewById(R.id.TextView_Register);
        Button_Login = findViewById(R.id.Button_Login);
        TextView_ResetPassword = findViewById(R.id.TextView_ResetPassword);
    }
}