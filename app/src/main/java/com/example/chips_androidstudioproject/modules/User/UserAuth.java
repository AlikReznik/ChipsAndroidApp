package com.example.chips_androidstudioproject.modules.User;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAuth {
    /*
     *   Manages auth with google firebase auth
     * */
    private static final String TAG = "Authentication - ";
    private final FirebaseAuth mAuth;

    /*
     *   Constructor - sets important data to and checks for previous logins
     * */
    public UserAuth(Context context){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DataFlowControl.context = context;

        // Checks for previous logins
        if(currentUser != null){
            Log.d(TAG, "SignIn user with email successfully");
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            DataFlowControl.authUser = new AuthUser(firebaseUser.getUid());
            UserFirestore.getUser();
        }else{
            Log.d(TAG, "User wasn't found");
            DataFlowControl.authUser = new AuthUser();
        }
    }

    /*
     *   Login with email
     * */
    public void signUpWithEmail(String email, String password, String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) DataFlowControl.context, task -> {
                    if (task.isSuccessful()) {
                        Log.w(TAG, "SignUp user with email successfully");
                        DataFlowControl.loadingDialog.dismissLoadingDialog();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        Map<String, Object> userSet = new HashMap<>();
                        userSet.put("Email", email);
                        userSet.put("Username", username);
                        userSet.put("Image", "");

                        String[] chats = { "zd5EnOjQMEzyA6hukgBB" };
                        userSet.put("Chats", chats);

                        DataFlowControl.authUser.update(Objects.requireNonNull(firebaseUser).getUid(), userSet);
                        UserFirestore.createUser();
                    } else {
                        DataFlowControl.loadingDialog.dismissLoadingDialog();
                        AuthErrorHandler.errorHandler((FirebaseAuthException) Objects.requireNonNull(task.getException()));
                        Log.w(TAG, "SignUp user with email unsuccessfully", task.getException());
                    }
                });
    }

    /*
     *   Signup with email
     * */
    public void signInWithEmail(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) DataFlowControl.context, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "SignIn user with email successfully");
                        DataFlowControl.loadingDialog.dismissLoadingDialog();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        DataFlowControl.authUser.setUid(Objects.requireNonNull(firebaseUser).getUid());
                        UserFirestore.getUser();
                    } else {
                        DataFlowControl.loadingDialog.dismissLoadingDialog();
                        Log.w(TAG, "SignIn user with email unsuccessfully", task.getException());
                        AuthErrorHandler.errorHandler((FirebaseAuthException) Objects.requireNonNull(task.getException()));
                    }
                });
    }

    /*
     *   Logout
     * */
    public void signout(){
        Log.d(TAG, "SignOut");
        FirebaseAuth.getInstance().signOut();
        DataFlowControl.authUser.clear();
    }

    /*
    * Email check
    * */
    public static boolean emailCheck(String email){
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._]{1,16}+@[a-z]{1,7}\\.[a-z]{1,3}$");
        Matcher mail = pattern.matcher(email);

        return mail.find();
    }
}
