package com.example.chips_androidstudioproject.modules;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.example.chips_androidstudioproject.Dialogs.loading_custom_dialog;
import com.example.chips_androidstudioproject.modules.User.AuthUser;
import com.example.chips_androidstudioproject.modules.User.UserAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataFlowControl {
    /*
     *   Manages important data to access from all activities
     * */
    @SuppressLint("StaticFieldLeak")
    public static AuthUser authUser;
    public static Context context;
    public static UserAuth userAuth;
    public static FirebaseFirestore firestoreDb;
    public static Activity activity;
    public static loading_custom_dialog loadingDialog;
}
