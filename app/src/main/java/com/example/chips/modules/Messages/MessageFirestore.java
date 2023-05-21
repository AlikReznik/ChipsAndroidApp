package com.example.chips.modules.Messages;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.example.chips.ChatActivity;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.utils.BitmapFunctions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MessageFirestore {
    /*
     *   Message firestore class - controls message variables in firestore
     * */
    /*
     *   Message firestore variables
     * */
    public static ListenerRegistration listener;

    /*
     *   dataListener:
     *      Updates chat when a new message is sent => Creates new message
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public static void dataListener(){
        listener = DataFlowControl.firestoreDb.collection("Chats/"+ ChatActivity.chat.Uid()+"/Messages")
            .orderBy("Time")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            int type = Integer.parseInt(dc.getDocument().getData().get("Type").toString());

                            String sender = dc.getDocument().getData().get("Sender").toString();
                            Timestamp time = (Timestamp) dc.getDocument().getData().get("Time");

                            CustomMessage cmsg = null;
                            if (type == 0) {
                                String msg = dc.getDocument().getData().get("Msg").toString();
                                cmsg = new TextMessage(sender, time, msg);
                            }
                            if (type == 1) {
                                String msg = dc.getDocument().getData().get("Msg").toString();
                                Bitmap bm = BitmapFunctions.stringToBitMap(msg);
                                cmsg = new ImageMessage(sender, time, bm);
                            }
                            if (type == 2) {
                                String msg = dc.getDocument().getData().get("Image").toString();
                                String title = dc.getDocument().getData().get("Title").toString();
                                String desc = dc.getDocument().getData().get("Description").toString();
                                Bitmap bm = BitmapFunctions.stringToBitMap(msg);
                                cmsg = new Chips(sender, time, bm, title, desc);
                            }
                            if(cmsg != null) {
                                ChatActivity.chat.addMessage(cmsg);
                                ChatActivity.updateListViewMessagesAdapter();
                            }
                        }
                    }
                }
            });
    }

    /*
     *   addMessage:
     *      Adds new message to the chat
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public static void addMessage(CustomMessage msg) {
        Map<String, Object> msgMap = msg.convertToMap();
        DataFlowControl.firestoreDb
                .collection("Chats/"+ ChatActivity.chat.Uid()+"/Messages")
                .add(msgMap)
                .addOnCompleteListener(task -> {
                    DataFlowControl.loadingDialog.dismissLoadingDialog();
                });
    }

}
