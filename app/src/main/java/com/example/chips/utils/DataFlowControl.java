package com.example.chips.utils;

import android.app.Activity;
import android.content.Context;
import com.example.chips.dialogs.LoadingCustomDialog;
import com.example.chips.modules.User.AuthUser;
import com.example.chips.modules.User.UserAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataFlowControl {
    /*
     *   Manages important data to access from all activities
     * */

    /*
    *   Activity context & activity
    * */
    public static Context context;
    public static Activity activity;

    /*
    *   User
    * */
    public static AuthUser authUser;
    /*
    *   Firebase user controller
    * */
    public static UserAuth userAuth;
    /*
    *   Firebase firestore database
    * */
    public static FirebaseFirestore firestoreDb;

    /*
    *   Loading dialog
    * */
    public static LoadingCustomDialog loadingDialog;
}
