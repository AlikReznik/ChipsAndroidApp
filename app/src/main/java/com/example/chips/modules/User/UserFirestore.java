package com.example.chips.modules.User;

import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.chips.ChatSettingsActivity;
import com.example.chips.CreateChatActivity;
import com.example.chips.MainActivity;
import com.example.chips.mainFragments.SearchFragment;
import com.example.chips.modules.Chats.Chat;
import com.example.chips.utils.DataFlowControl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.WriteBatch;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserFirestore {
    /*
     *   user firestore class - controls user variables in firestore
     * */
    /*
     *   User firestore variables
     * */
    private static final String COLLECTION = "Users";

    /*
     *   createUser:
     *      Creates new user
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public static void createUser() {
        DataFlowControl.firestoreDb
                .collection(COLLECTION)
                .document(DataFlowControl.authUser.Uid())
                .set(DataFlowControl.authUser.set())
                .addOnCompleteListener(task -> {
                    Intent intent = new Intent(DataFlowControl.context, MainActivity.class);
                    DataFlowControl.context.startActivity(intent);
                });
    }

    /*
     *   getUser:
     *      Gets user data from firebase and updates user
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public static void getUser() {
        DataFlowControl.firestoreDb.collection(COLLECTION).document(DataFlowControl.authUser.Uid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    /*
                    *   Updates user data
                    * */
                    DataFlowControl.authUser.update(document.getId(), Objects.requireNonNull(document.getData()));

                    Toast.makeText(DataFlowControl.context, "Hi, " + DataFlowControl.authUser.Username(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DataFlowControl.context, MainActivity.class);
                    DataFlowControl.context.startActivity(intent);
                    return;
                }
                DataFlowControl.loadingDialog.dismissLoadingDialog();
                Toast.makeText(DataFlowControl.context, "Connection error accrued", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     *   updateUser:
     *      Updates firestore user data
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
        public static void updateUser() {
        WriteBatch batch = DataFlowControl.firestoreDb.batch();

        DocumentReference sfRef = DataFlowControl.firestoreDb.collection(COLLECTION).document(DataFlowControl.authUser.Uid());
        batch.update(sfRef, "Username", DataFlowControl.authUser.Username());
        batch.update(sfRef, "Image", DataFlowControl.authUser.Image());
        batch.update(sfRef, "Bio", DataFlowControl.authUser.Bio());

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DataFlowControl.context, "User was updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     *   getUserProfileByUsername:
     *      Gets all users by username search
     *
     *   Input:
     *       String username
     *
     *   Output:
     *       None
     * */
    public static void getUserProfileByUsername(String activity, String username) {
        switch(activity){
            case "Search-Fragment":
                DataFlowControl.firestoreDb.collection(COLLECTION)
                        .whereGreaterThanOrEqualTo("Username", username)
                        .whereLessThan("Username",  username + "z")
                        .limit(10).get().addOnCompleteListener(task -> {
                            SearchFragment.users.clear();
                            for (DocumentSnapshot dc : task.getResult().getDocuments()) {
                                BaseUser user = new BaseUser();
                                user.update(dc.getId(), dc.getData());
                                if(!user.Uid().equals(DataFlowControl.authUser.Uid())) {
                                    SearchFragment.users.add(user);
                                }
                            }
                            DataFlowControl.loadingDialog.dismissLoadingDialog();
                            SearchFragment.updateListViewProfileUserAdapter();
                        });

                break;
            case "AddUser-Activity":
                DataFlowControl.firestoreDb.collection(COLLECTION)
                        .whereGreaterThanOrEqualTo("Username", username)
                        .whereLessThan("Username",  username + "z")
                        .limit(10).get().addOnCompleteListener(task -> {
                            ChatSettingsActivity.usersSearch.clear();
                            for (DocumentSnapshot dc : task.getResult().getDocuments()) {
                                BaseUser user = new BaseUser();
                                user.update(dc.getId(), dc.getData());
                                if(!user.Uid().equals(DataFlowControl.authUser.Uid())) {
                                    ChatSettingsActivity.usersSearch.add(user);
                                }
                            }
                            DataFlowControl.loadingDialog.dismissLoadingDialog();
                            ChatSettingsActivity.updateListViewUserSearch();
                        });

                break;
            case "AddUserCreateChat-Activity":
                DataFlowControl.firestoreDb.collection(COLLECTION)
                        .whereGreaterThanOrEqualTo("Username", username)
                        .whereLessThan("Username",  username + "z")
                        .limit(10).get().addOnCompleteListener(task -> {
                            CreateChatActivity.usersSearch.clear();
                            for (DocumentSnapshot dc : task.getResult().getDocuments()) {
                                BaseUser user = new BaseUser();
                                user.update(dc.getId(), dc.getData());
                                if(!user.Uid().equals(DataFlowControl.authUser.Uid())) {
                                    CreateChatActivity.usersSearch.add(user);
                                }
                            }
                            DataFlowControl.loadingDialog.dismissLoadingDialog();
                            CreateChatActivity.updateListViewUserSearch();
                        });

                break;
        }
    }

    /*
     *   getChatUserByUid:
     *      Gets user by uid and adds to chat
     *
     *   Input:
     *       Chat chat, String userUid, boolean isAdmin
     *
     *   Output:
     *       None
     * */
    public static void getChatUserByUid(Chat chat, String userUid, boolean isAdmin) {
        DataFlowControl.firestoreDb.collection(COLLECTION).document(userUid)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ChatUser chatuser = new ChatUser(document.getId(), document.getData(), isAdmin);
                        chat.addUser(chatuser);
                        return;
                    }
                }
                Toast.makeText(DataFlowControl.context, "Connection error accrued", Toast.LENGTH_SHORT).show();
            });
    }

    /*
     *   addChatToUser:
     *      Adds chat uid to chat list in user firestore data
     *
     *   Input:
     *       String userUid, String chatUid
     *
     *   Output:
     *       None
     * */
    public static void addChatToUser(String userUid, String chatUid) {
        DataFlowControl.firestoreDb
                .collection(COLLECTION)
                .document(userUid)
                .update("Chats", FieldValue.arrayUnion(chatUid));
    }

    /*
     *   removeChatUser:
     *      Removes chat uid from chat list in user firestore data
     *
     *   Input:
     *       String userUid, String chatUid
     *
     *   Output:
     *       None
     * */
    public static void removeChatUser(String userUid, String chatUid) {
        Map<String, Object> update = new HashMap<>();
        update.put("Chats", FieldValue.delete());

        /*
        *   Removes "Chats" list
        * */
        DataFlowControl.firestoreDb
                .collection(COLLECTION)
                .document(userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> chatList = (List<String>) document.getData().get("Chats");
                                chatList.remove(chatUid);

                                /*
                                *   Adds new "Chats" list without the chat
                                * */
                                DataFlowControl.firestoreDb
                                        .collection(COLLECTION)
                                        .document(userUid)
                                        .update(update)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                DataFlowControl.firestoreDb
                                                        .collection(COLLECTION)
                                                        .document(userUid)
                                                        .update("Chats", chatList)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(DataFlowControl.context, "Data was updated successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(DataFlowControl.context, "Connection error accrued", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    /*
     *   getUsersProfiles:
     *      Sets all users information to list from which the function was called
     *
     *   Input:
     *       String activity
     *
     *   Output:
     *       None
     * */
    public static void getUsersProfiles(String activity) {
        switch(activity){
            case "Search-Fragment":
                DataFlowControl.firestoreDb
                        .collection(COLLECTION)
                        .get()
                        .addOnCompleteListener(task -> {
                            SearchFragment.users.clear();
                            for (DocumentSnapshot dc : task.getResult().getDocuments()) {
                                BaseUser user = new BaseUser();
                                user.update(dc.getId(), dc.getData());
                                if(!user.Uid().equals(DataFlowControl.authUser.Uid())) {
                                    SearchFragment.users.add(user);
                                }
                            }
                            DataFlowControl.loadingDialog.dismissLoadingDialog();
                            SearchFragment.updateListViewProfileUserAdapter();
                        });

                break;
            case "AddUser-Activity":
                DataFlowControl.firestoreDb
                        .collection(COLLECTION)
                        .get()
                        .addOnCompleteListener(task -> {
                            ChatSettingsActivity.usersSearch.clear();
                            for (DocumentSnapshot dc : task.getResult().getDocuments()) {
                                BaseUser user = new BaseUser();
                                user.update(dc.getId(), dc.getData());
                                if(!user.Uid().equals(DataFlowControl.authUser.Uid())) {
                                    ChatSettingsActivity.usersSearch.add(user);
                                }
                            }

                            DataFlowControl.loadingDialog.dismissLoadingDialog();
                            ChatSettingsActivity.updateListViewUserSearch();
                        });

                break;
            case "AddUserCreateChat-Activity":
                DataFlowControl.firestoreDb
                        .collection(COLLECTION)
                        .get()
                        .addOnCompleteListener(task -> {
                            CreateChatActivity.usersSearch.clear();
                            for (DocumentSnapshot dc : task.getResult().getDocuments()) {
                                BaseUser user = new BaseUser();
                                user.update(dc.getId(), dc.getData());
                                if(!user.Uid().equals(DataFlowControl.authUser.Uid())) {
                                    CreateChatActivity.usersSearch.add(user);
                                }
                            }
                            DataFlowControl.loadingDialog.dismissLoadingDialog();
                            CreateChatActivity.updateListViewUserSearch();
                        });

                break;
        }
    }
}
