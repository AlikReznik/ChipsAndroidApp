package com.example.chips;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.modules.User.UserAuth;

public class SignUpActivity extends AppCompatActivity {
    /*
     *   Layout variables
     * */
    private EditText EditText_Email;
    private EditText EditText_Username;
    private EditText EditText_Password;
    private EditText EditText_PasswordRepeat;
    private Button Button_Signup;
    private TextView TextView_Login;

    /*
     *   Activity variables
     * */
    private UserAuth userAuth;

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);}

    protected void onStart() {
        super.onStart();

        initLayout();
        initInfo();
        initButtonListeners();
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   signInInfoValidator:
     *      Validates user information
     *
     *   Input:
     *       String username, String email, String password, String rePassword
     *
     *   Output:
     *       None
     * */
    private void signInInfoValidator(String username, String email, String password, String rePassword) {
        boolean usernameStatus = !username.equals("");
        boolean emailStatus = UserAuth.emailValidator(email);
        boolean passwordStatus = passwordValidator(password, rePassword);
        if(usernameStatus & emailStatus & passwordStatus){
            DataFlowControl.loadingDialog.startLoadingDialog();
            userAuth.signUpWithEmail(email, password, username);
        }else{
            Toast.makeText(DataFlowControl.context, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     *   passwordValidator:
     *      Validates password
     *
     *   Input:
     *       String password, String rePassword
     *
     *   Output:
     *       Boolean
     * */
    private Boolean passwordValidator(String password, String rePassword) {
        return password.length() >= 6 && password.equals(rePassword);
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
        Button_Signup.setOnClickListener(view ->
        {
            signInInfoValidator(EditText_Username.getText().toString(), EditText_Email.getText().toString(), EditText_Password.getText().toString(), EditText_PasswordRepeat.getText().toString());
        });

        TextView_Login.setOnClickListener(view ->
        {
            Intent intent = new Intent(DataFlowControl.context, LoginActivity.class);
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
        DataFlowControl.context = this;
        DataFlowControl.activity = this;

        userAuth = DataFlowControl.userAuth;
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
        EditText_Username = findViewById(R.id.EditText_Username);
        EditText_Password = findViewById(R.id.EditText_Password);
        EditText_PasswordRepeat = findViewById(R.id.EditText_PasswordRepeat);
        Button_Signup = findViewById(R.id.Button_Signup);
        TextView_Login = findViewById(R.id.TextView_Login);
    }

}