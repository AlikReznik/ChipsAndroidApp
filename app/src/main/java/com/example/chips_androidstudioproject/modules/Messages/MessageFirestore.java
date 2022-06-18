package com.example.chips_androidstudioproject.modules.Messages;

import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.Nullable;

import com.example.chips_androidstudioproject.chat_activity;
import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.example.chips_androidstudioproject.modules.Utils;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Objects;

public class MessageFirestore {
    /*
     *   Manages "Chats/[chat's id]" firestore's collection data
     * */
    private static final String TAG = "Message Firestore - ";
    private static final String COLLECTION = "Chats";
    public static ListenerRegistration listener;

    public static void DataListener(){
        listener = DataFlowControl.firestoreDb.collection("Chats/"+ chat_activity.chat.Uid()+"/messagesUid")
            .orderBy("Time")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            int type = Integer.parseInt(dc.getDocument().getData().get("Type").toString());

                            String sender = dc.getDocument().getData().get("Sender").toString();
                            Timestamp time = (Timestamp) dc.getDocument().getData().get("tTime");

                            CustomMessage cmsg = null;
                            if (type == 0) {
                                String msg = dc.getDocument().getData().get("Msg").toString();
                                cmsg = new TextMessage(sender, time, msg);
                            }
                            if (type == 1) {
                                String msg = dc.getDocument().getData().get("Msg").toString();
                                Bitmap bm = Utils.StringToBitMap(msg);
                                cmsg = new ImageMessage(sender, time, bm);
                            }
                            if (type == -1) {
                                String msg = dc.getDocument().getData().get("Image").toString();
                                String title = dc.getDocument().getData().get("Title").toString();
                                String desc = dc.getDocument().getData().get("Desc").toString();
                                Bitmap bm = Utils.StringToBitMap(msg);
                                cmsg = new Chips(sender, time, bm, title, desc);
                            }
                            if(cmsg != null) {
                                chat_activity.chat.addMessage(cmsg);
                                chat_activity.updateListViewMessagesAdapter();
                            }
                        }
                    }
                }
            });
    }

    public static void addMessage(CustomMessage msg) {
        Map<String, Object> msgMap = msg.convertToMap();
        try{
            DataFlowControl.firestoreDb.collection("Chats/"+chat_activity.chat.Uid()+"/messagesUid").add(msgMap);
        }catch (Exception e){
            Log.d("Error", "Error adding document: ", e);
        }
    }

}
