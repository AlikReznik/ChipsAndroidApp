package com.example.chips_androidstudioproject.modules.User;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chips_androidstudioproject.MainFragments.SearchFragment;
import com.example.chips_androidstudioproject.chat_activity;
import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.example.chips_androidstudioproject.login_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;
import java.util.Objects;

public class UserFirestore {
    /*
     *   Manages "Users" firestore's collection data
     * */
    @SuppressLint("StaticFieldLeak")
    private static final String TAG = "User Firestore - ";
    private static final String COLLECTION = "Users";

    /*
    *   Gets user data from firestore using uid and updates activity screen
    * */
    public static void getUser() {
        DataFlowControl.firestoreDb.collection(COLLECTION).document(DataFlowControl.authUser.Uid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "Document was found successfully " + document.getData());
                    DataFlowControl.authUser.update(document.getId(), Objects.requireNonNull(document.getData())); // Updates user data
                    login_activity.intentToMainActivity(DataFlowControl.context);
                } else {
                    Log.d(TAG, "Document wasn't found");
                }
            } else {
                Log.d(TAG, "An error accrued ", task.getException());
            }
        });
    }

    /*
     *   Creates or updates new user in firestore
     * */
    public static void updateUser() {
        WriteBatch batch = DataFlowControl.firestoreDb.batch();

        DocumentReference sfRef = DataFlowControl.firestoreDb.collection("Users").document(DataFlowControl.authUser.Uid());
        batch.update(sfRef, "Username", DataFlowControl.authUser.Username());
        batch.update(sfRef, "Image", DataFlowControl.authUser.Image());

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "User was changed successfully");
            }
        });

    }

    public static void getUsersProfiles() {
        DataFlowControl.firestoreDb.collection("Users").get().addOnCompleteListener(task -> {
            SearchFragment.users.clear();
            for (DocumentSnapshot dc : task.getResult().getDocuments()) {
                BaseUser user = new BaseUser();
                user.update(dc.getId(), dc.getData());
                Log.d(TAG, "User uid: "+user.Uid());
                SearchFragment.users.add(user);
            }
            DataFlowControl.loadingDialog.dismissLoadingDialog();
            SearchFragment.updateListViewProfileUserAdapter();
        });
    }

    public static void getUserProfileByUsername(String username) {
        DataFlowControl.firestoreDb.collection("Users")
            .whereGreaterThanOrEqualTo("Username", username)
            .whereLessThan("Username",  username + "z")
            .limit(10).get().addOnCompleteListener(task -> {
                SearchFragment.users.clear();
                for (DocumentSnapshot dc : task.getResult().getDocuments()) {
                    BaseUser user = new BaseUser();
                    user.update(dc.getId(), dc.getData());
                    Log.d(TAG, "User uid: "+user.Uid());
                    SearchFragment.users.add(user);
                }
                DataFlowControl.loadingDialog.dismissLoadingDialog();
                SearchFragment.updateListViewProfileUserAdapter();
        });
    }

    public static void updateProfileUser(ChatUser user) {

        Log.d("UserFirebase", user.Uid());
        /*DataFlowControl.firestoreDb.collection(COLLECTION).document(user.Uid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
//                    user.update(document.getId(), document.getData());
                } else {
                    Log.d(TAG, "Document wasn't found");
                }
            } else {
                Log.d(TAG, "An error accrued ", task.getException());
            }
        });*/
    }

    public static void createUser() {
        try{
            DataFlowControl.firestoreDb.collection("Users").add(DataFlowControl.authUser.set());
        }catch (Exception e){
            Log.d("Error", "Error adding document: ", e);
        }
    }
}
