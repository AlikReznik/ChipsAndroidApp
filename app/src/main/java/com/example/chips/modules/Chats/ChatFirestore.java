package com.example.chips.modules.Chats;

import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.chips.ChatActivity;
import com.example.chips.MainActivity;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.modules.User.ChatUser;
import com.example.chips.modules.User.UserFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.WriteBatch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatFirestore {
    /*
    *   Chat firestore class - controls chat variables in firestore
    * */
    /*
    *   Chat firestore variables
    * */
    private static final String COLLECTION = "Chats";


    /*
     *   getChat:
     *      Returns chat from firestore by uid
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public static void getChat(String uid){
        DataFlowControl.firestoreDb.collection(COLLECTION).document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    DataFlowControl.authUser.addChat(document.getId(), Objects.requireNonNull(document.getData())); // Updates user data
                    return;
                }
            }

            Toast.makeText(DataFlowControl.context, "Connection error accrued", Toast.LENGTH_SHORT).show();
        });
    }

    /*
     *   createChat:
     *      Creates chat using chatSet information
     *
     *   Input:
     *       Map<String, Object> chatSet
     *   Output:
     *       None
     * */
    public static void createChat(Map<String, Object> chatSet){
        DataFlowControl.firestoreDb.collection(COLLECTION)
                .add(chatSet)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        DataFlowControl.authUser.addChat(documentReference.getId(), chatSet);

                        /*
                         *   Adds chat to chat list to every user in the chat
                         * */
                        List<Map<String, Object>> usersChatSet = (List<Map<String, Object>>) chatSet.get("Users");
                        for(Map<String, Object> userChatSet : usersChatSet){
                            String userUid = (String) userChatSet.get("UserUid");
                            UserFirestore.addChatToUser(userUid, documentReference.getId());
                        }

                        Toast.makeText(DataFlowControl.context, "Chat was created", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DataFlowControl.context, MainActivity.class);
                        DataFlowControl.context.startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DataFlowControl.context, "Connection error accrued", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*
     *   createChatSet:
     *      Creates chatSet that helps create chats
     *
     *   Input:
     *       String name, String image, int type, List<String> userUids
     *   Output:
     *       Map<String, Object>
     * */
    public static Map<String, Object> createChatSet(String name, String image, int type, List<ChatUser> chatUsers) {
        Map<String, Object> chatSet = new HashMap<>();
        chatSet.put("Image", image);
        chatSet.put("Name", name);
        chatSet.put("Type", type);


        List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
        for (ChatUser chatUser : chatUsers) {
            Map<String, Object> chatUserSet = new HashMap<>();
            chatUserSet.put("UserUid", chatUser.Uid());
            chatUserSet.put("IsAdmin", chatUser.isAdmin());
            users.add(chatUserSet);
        }
        Map<String, Object> chatUserSet = new HashMap<>();
        chatUserSet.put("UserUid", DataFlowControl.authUser.Uid());
        chatUserSet.put("IsAdmin", true);
        users.add(chatUserSet);

        chatSet.put("Users", users);

        return chatSet;
    }

    /*
     *   updateChat:
     *      Updates chat's information
     *
     *   Input:
     *       Chat chat
     *   Output:
     *       None
     * */
    public static void updateChat(Chat chat) {
        WriteBatch batch = DataFlowControl.firestoreDb.batch();

        DocumentReference sfRef = DataFlowControl.firestoreDb.collection("Chats").document(chat.Uid());
        batch.update(sfRef, "Name", chat.Name());
        batch.update(sfRef, "Image", chat.Image());

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DataFlowControl.loadingDialog.dismissLoadingDialog();
            }
        });
    }

    /*
     *   addUserToChat:
     *      Adds user to chat
     *
     *   Input:
     *       Chat chat
     *   Output:
     *       None
     * */
    public static void addUserToChat(String userUid, String chatUid){
        Map<String, Object> chatUserSet = new HashMap<>();
        chatUserSet.put("UserUid", userUid);
        chatUserSet.put("IsAdmin", false);

        DataFlowControl.firestoreDb
                .collection(COLLECTION)
                .document(chatUid)
                .update("Users", FieldValue.arrayUnion(chatUserSet));
    }

    /*
     *   findPrivateChat:
     *      Checks if private chat exist => exist - opens it, doesn't exist - creates
     *
     *   Input:
     *       String userUid ,String userUsername
     *   Output:
     *       None
     * */
    public static void findPrivateChat(String userUid ,String userUsername) {
        int chatIndex = 0;
        for (Chat chat : DataFlowControl.authUser.Chats()){
            if(chat.Users().size() == 2){
                if(chat.Users().get(0).Uid().equals(userUid) && chat.Users().get(1).Uid().equals(DataFlowControl.authUser.Uid()))
                {
                    Intent intent = new Intent(DataFlowControl.context, ChatActivity.class);
                    intent.putExtra("ChatId", chatIndex);
                    DataFlowControl.context.startActivity(intent);
                    return;
                }
                if(chat.Users().get(1).Uid().equals(userUid) && chat.Users().get(0).Uid().equals(DataFlowControl.authUser.Uid()))
                {
                    Intent intent = new Intent(DataFlowControl.context, ChatActivity.class);
                    intent.putExtra("ChatId", chatIndex);
                    DataFlowControl.context.startActivity(intent);
                    return;
                }
            }
            chatIndex++;
        }

        /*
        *   If none all the chats are the chat that was searched for => createPrivateChat
        * */
        if(chatIndex == DataFlowControl.authUser.Chats().size()) {
            createPrivateChat(userUid, userUsername);
        }
    }

    /*
     *   deleteChatByUid:
     *      Deletes chat by uid
     *
     *   Input:
     *       int chatId
     *   Output:
     *       None
     * */
    public static void deleteChatByUid(int chatId){
        List<String> usersUid = new ArrayList<String>();
        setUsersUidList(usersUid, DataFlowControl.authUser.getChat(chatId));
        String chatUid = DataFlowControl.authUser.getChat(chatId).Uid();

        DataFlowControl.authUser.deleteChat(chatId);

        /*
        *   Removes chatUid from chat list of each chat user
        * */
        for (String userUid : usersUid){
            UserFirestore.removeChatUser(userUid, chatUid);
        }

        DataFlowControl.firestoreDb
                .collection(COLLECTION)
                .document(chatUid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(DataFlowControl.context, MainActivity.class);
                        DataFlowControl.context.startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DataFlowControl.context, "Connection error accrued", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*
     *   updateUserAdmin:
     *      Deletes chat by uid
     *
     *   Input:
     *       Chat chat, int chatId
     *   Output:
     *       None
     * */
    public static void updateChatUsers(Chat chat, int chatId) {
        Map<String, Object> update = new HashMap<>();
        update.put("Users", FieldValue.delete());

        List<Map<String, Object>> usersList = new ArrayList<Map<String, Object>>();
        for (ChatUser user : chat.Users()){
            usersList.add(user.set());
        }

        DataFlowControl.firestoreDb
                .collection(COLLECTION)
                .document(chat.Uid())
                .update(update)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DataFlowControl.firestoreDb
                                .collection(COLLECTION)
                                .document(chat.Uid())
                                .update("Users", usersList)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(DataFlowControl.context, ChatActivity.class);
                                        intent.putExtra("ChatId", chatId);
                                        DataFlowControl.context.startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DataFlowControl.context, "Connection error accrued", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    /*
     *   setUsersUidList:
     *      Sets chat users uids to a list
     *
     *   Input:
     *       Chat chat
     *   Output:
     *       None
     * */
    private static void setUsersUidList(List<String> list, Chat chat) {
        for(ChatUser user : chat.Users()){
            list.add(user.Uid());
        }
    }

    /*
     *   createPrivateChat:
     *      Create private chat
     *
     *   Input:
     *       String userUid ,String userUsername
     *   Output:
     *       None
     * */
    private static void createPrivateChat(String userUid, String userUsername) {
        List<ChatUser> chatUsers = new ArrayList<ChatUser>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("Bio", "");
        data.put("Username", "");
        data.put("Image","");
        data.put("Email", "");

        ChatUser user_1 = new ChatUser(userUid, data, false);

        chatUsers.add(user_1);

        Map<String, Object> chatSet = createChatSet(DataFlowControl.authUser.Username() + " & " + userUsername, "", 2, chatUsers);
        List<Map<String, Object>> usersChatSet = (List<Map<String, Object>>) chatSet.get("Users");
        usersChatSet.get(1).put("IsAdmin", false);

        createChat(chatSet);
    }

}
