package com.example.chips_androidstudioproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chips_androidstudioproject.Dialogs.loading_custom_dialog;
import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.example.chips_androidstudioproject.modules.User.UserAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class login_activity extends AppCompatActivity {
    private UserAuth userAuth;

    /* Layout variables */
    private TextView txt_email;
    private TextView txt_password;
    private Button btn_signUpActivity;
    private Button btn_logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        /* Setup DataFlowControl variables */

        DataFlowControl.firestoreDb = FirebaseFirestore.getInstance();
        userAuth = new UserAuth(this);
        DataFlowControl.userAuth = userAuth;
        DataFlowControl.activity = login_activity.this;
        DataFlowControl.context = login_activity.this;
        DataFlowControl.loadingDialog = new loading_custom_dialog();

        Intent serviceIntent = new Intent(this, InternetConnectionService.class);
        startForegroundService(serviceIntent);

        /* Check user auth => MainActivity / Loads Activity */

        if(DataFlowControl.authUser == null){
            Log.d("SignIn", "UserExists");
            DataFlowControl.loadingDialog.startLoadingDialog();
        }else{
            initLayout();
            initButtonListeners();
        }
    }

    /* Being called when activity is re opened */
    public void onStart() {
        super.onStart();
        DataFlowControl.activity = login_activity.this;
        DataFlowControl.context = login_activity.this;
    }

    /* Starts Main activity on a successful login */
    public static void intentToMainActivity(Context context) {
        Toast.makeText(context, "Hi, " + DataFlowControl.authUser.Username(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /* Button listeners */
    private void initButtonListeners() {
        btn_logIn.setOnClickListener(view ->
            {
                boolean emailStatus = UserAuth.emailCheck(txt_email.getText().toString());
                boolean passwords = txt_password.getText().toString().length() >= 6;
                if(emailStatus & passwords){
                    DataFlowControl.loadingDialog.startLoadingDialog();
                    userAuth.signInWithEmail(txt_email.getText().toString(), txt_password.getText().toString());
                }else{
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            });

        btn_signUpActivity.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, signup_activity.class);
            startActivity(intent);
        });
    }

    /* Sets variables */
    private void initLayout(){
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        btn_signUpActivity = findViewById(R.id.btn_signUpActivity);
        btn_logIn = findViewById(R.id.btn_logIn);
    }
}