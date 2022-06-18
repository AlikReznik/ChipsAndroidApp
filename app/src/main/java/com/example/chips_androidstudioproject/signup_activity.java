package com.example.chips_androidstudioproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.example.chips_androidstudioproject.modules.User.UserAuth;

public class signup_activity extends AppCompatActivity {
    private UserAuth userAuth;

    /* Layout variables */
    private TextView txt_username;
    private TextView txt_email;
    private TextView txt_password_1;
    private TextView txt_password_2;
    private Button btn_signUp;
    private Button btn_logInActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity_layout);

        /* Setup DataFlowControl variables */
        DataFlowControl.context = this;
        userAuth = DataFlowControl.userAuth;

        initLayout();
        initButtonListeners();
    }

    /* Being called when activity is re opened */
    public void onStart() {
        super.onStart();
        DataFlowControl.activity = signup_activity.this;
    }

    /* Button listeners */
    private void initButtonListeners() {
        btn_signUp.setOnClickListener(view ->
        {

            boolean usernameStatus = !txt_username.getText().toString().equals("");
            boolean emailStatus = UserAuth.emailCheck(txt_email.getText().toString());
            boolean passwords = txt_password_1.getText().toString().length() >= 6 && txt_password_1.getText().toString().equals(txt_password_2.getText().toString());
            if(usernameStatus & emailStatus & passwords){
                DataFlowControl.loadingDialog.startLoadingDialog();
                userAuth.signUpWithEmail(txt_email.getText().toString(), txt_password_1.getText().toString(), txt_username.getText().toString());
            }else{
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        btn_logInActivity.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, login_activity.class);
            startActivity(intent);
        });
    }

    /* Sets variables */
    private void initLayout(){
        txt_email = findViewById(R.id.txt_email);
        txt_username = findViewById(R.id.txt_username);
        txt_password_1 = findViewById(R.id.txt_password_1);
        txt_password_2 = findViewById(R.id.txt_password_2);
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_logInActivity = findViewById(R.id.btn_logInActivity);
    }

}