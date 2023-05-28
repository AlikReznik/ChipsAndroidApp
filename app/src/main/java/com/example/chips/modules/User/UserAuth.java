package com.example.chips.modules.User;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.chips.LoginActivity;
import com.example.chips.R;
import com.example.chips.utils.AuthErrorHandler;
import com.example.chips.utils.DataFlowControl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAuth {
    /*
     *   Manages auth with google firebase auth
     * */

    /*
     *  UserAuth variables:
     * */
    private FirebaseAuth mAuth;

    /*
     *   UserAuth:
     *      User auth constructor
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public UserAuth(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        /*
        *   Check if user is already logged in
        * */
        if(currentUser != null){
            DataFlowControl.loadingDialog.startLoadingDialog();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            DataFlowControl.authUser = new AuthUser(firebaseUser.getUid());
            UserFirestore.getUser();
        }else{
            DataFlowControl.authUser = new AuthUser();
        }
    }

    /*
     *   signUpWithEmail:
     *      Login with email
     *
     *   Input:
     *       String email, String password, String username
     *
     *   Output:
     *       None
     * */
    public void signUpWithEmail(String email, String password, String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) DataFlowControl.context, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        Map<String, Object> userSet = new HashMap<>();
                        userSet.put("Email", email);
                        userSet.put("Username", username);
                        userSet.put("Image", "");
                        userSet.put("Bio", "Hi, I am a Chips user!");

                        List<String> chats = new ArrayList<String>();
                        userSet.put("Chats", chats);

                        DataFlowControl.authUser.update(firebaseUser.getUid(), userSet);

                        Map<String, Object> chat = new HashMap<>();
                        chat.put("Name", "Global chat");
                        chat.put("Image", "");
                        chat.put("Users", null);

                        UserFirestore.createUser();

                        notifyOnSignup();
                    } else {
                        AuthErrorHandler.errorHandler((FirebaseAuthException) Objects.requireNonNull(task.getException()));
                    }
                    DataFlowControl.loadingDialog.dismissLoadingDialog();

                });
    }

    /*
     *   notifyOnSignup:
     *      Informs the user to update information after the sign up
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private static void notifyOnSignup(){
        NotificationManager notification_manager = (NotificationManager) DataFlowControl.context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification_builder = null;
        Random rand = new Random();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channel_id = "1000";
            CharSequence name = "Chips notification";
            String description = "Chips app notification";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(channel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notification_manager.createNotificationChannel(mChannel);
            notification_builder = new NotificationCompat.Builder(DataFlowControl.context, channel_id);
        } else {
            notification_builder = new NotificationCompat.Builder(DataFlowControl.context);
        }
        notification_builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("New Sign up!")
                .setContentText("Welcome "+DataFlowControl.authUser.Username() + ", Go to profile page to update your Image and Bio")
                .setPriority(Notification.PRIORITY_MAX);

        notification_manager.notify(rand.nextInt(1000), notification_builder.build());
    }

    /*
     *   signInWithEmail:
     *      Creates account with email
     *
     *   Input:
     *       String email, String password
     *
     *   Output:
     *       None
     * */
    public void signInWithEmail(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) DataFlowControl.context, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        DataFlowControl.authUser.setUid(Objects.requireNonNull(firebaseUser).getUid());
                        UserFirestore.getUser();
                    } else {
                        AuthErrorHandler.errorHandler((FirebaseAuthException) Objects.requireNonNull(task.getException()));
                    }
                    DataFlowControl.loadingDialog.dismissLoadingDialog();
                });
    }

    /*
     *   resetEmail:
     *      Resets email
     *
     *   Input:
     *       String email
     *
     *   Output:
     *       None
     * */
    public void resetEmail(String email) {
        mAuth.getCurrentUser().updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DataFlowControl.context, "Email was updated", Toast.LENGTH_SHORT).show();

                            signout();
                            Intent intent = new Intent(DataFlowControl.context, LoginActivity.class);
                            DataFlowControl.context.startActivity(intent);
                        }else{
                            Toast.makeText(DataFlowControl.context, "An error accrued, "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        DataFlowControl.loadingDialog.dismissLoadingDialog();
                    }
                });
    }

    /*
     *   resetPassword:
     *      Resets password
     *
     *   Input:
     *       String email
     *
     *   Output:
     *       None
     * */
    public void resetPassword(String email){
        if(email.equals("-")){
            mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DataFlowControl.context, "An email for a password reset was sent", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DataFlowControl.context, "An error accrued, "+task.getException(), Toast.LENGTH_SHORT).show();
                            }

                            DataFlowControl.loadingDialog.dismissLoadingDialog();

                            signout();
                            Intent intent = new Intent(DataFlowControl.context, LoginActivity.class);
                            DataFlowControl.context.startActivity(intent);
                        }
                    });
        }else{
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DataFlowControl.context, "An email for a password reset was sent", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DataFlowControl.context, "An error accrued, "+task.getException(), Toast.LENGTH_SHORT).show();
                            }

                            DataFlowControl.loadingDialog.dismissLoadingDialog();
                        }
                    });
        }

    }

    /*
     *   signout:
     *      Signout
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public void signout(){
        FirebaseAuth.getInstance().signOut();
        DataFlowControl.authUser.clear();
    }


    /*
     *   emailValidator:
     *      Checks if an email is legit
     *
     *   Input:
     *       String email
     *
     *   Output:
     *       boolean
     * */
    public static boolean emailValidator(String email){
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._]{1,16}+@[a-z]{1,7}\\.[a-z]{1,3}$");
        Matcher mail = pattern.matcher(email);

        return mail.find();
    }
}
