package com.example.chips.utils;

import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuthException;

public class AuthErrorHandler {
    /*
     *   Handles firebase auth errors
     *
     * */

    /*
     *   errorHandler:
     *      Toasts auth error
     *
     *   Input:
     *       FirebaseAuthException error
     *
     *   Output:
     *       None
     * */
    public static void errorHandler(FirebaseAuthException error){
        String errorCode = error.getErrorCode();

        switch (errorCode) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(DataFlowControl.context, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(DataFlowControl.context, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(DataFlowControl.context, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(DataFlowControl.context, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(DataFlowControl.context, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(DataFlowControl.context, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(DataFlowControl.context, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(DataFlowControl.context, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(DataFlowControl.context, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(DataFlowControl.context, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(DataFlowControl.context, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(DataFlowControl.context, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(DataFlowControl.context, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(DataFlowControl.context, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(DataFlowControl.context, "The given password is invalid.", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
