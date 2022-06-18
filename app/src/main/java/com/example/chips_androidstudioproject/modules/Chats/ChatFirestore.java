package com.example.chips_androidstudioproject.modules.Chats;

import android.util.Log;

import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.example.chips_androidstudioproject.modules.User.ChatUser;
import com.example.chips_androidstudioproject.modules.User.UserFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatFirestore {
    /*
     *   Manages "Chats/[chat's id]" firestore's collection data
     * */
    private static final String TAG = "Chat Firestore - ";
    private static final String COLLECTION = "Chats";

    public static void getChat(String uid){
        DataFlowControl.firestoreDb.collection(COLLECTION).document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "Document was found successfully " + document.getData());
                    DataFlowControl.authUser.addChat(document.getId(), Objects.requireNonNull(document.getData())); // Updates user data
                } else {
                    Log.d(TAG, "Document wasn't found");
                }
            } else {
                Log.d(TAG, "An error accrued ", task.getException());
            }
        });
    }

    public static void getUsers(String uid) {
        DataFlowControl.firestoreDb.collection(COLLECTION).document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Map<String, Object>> data = (ArrayList) task.getResult().getData().get("Users");
                for (Map<String, Object> object : data){
                    //ChatUser user = new ChatUser(object.get("Uid").toString(), (Boolean) object.get("IsAdmin"));
                    Log.d("UserFirebase", object.get("Uid").toString());

                    // UserFirestore.updateProfileUser(user);
                }
            } else {
                Log.d(TAG, "An error accrued ", task.getException());
            }
        });
    }
}
