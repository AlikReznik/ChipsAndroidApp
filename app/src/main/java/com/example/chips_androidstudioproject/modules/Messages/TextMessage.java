package com.example.chips_androidstudioproject.modules.Messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chips_androidstudioproject.R;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class TextMessage extends CustomMessage{
    /*
     * Special message data
     * */
    public String msg;

    /*
     * Constructors
     * */
    public TextMessage(String sender, Timestamp time, String msg)
    {
        super(sender, time);
        this.msg = msg;
    }

    public TextMessage() { }

    /*
     * Implementation of the abstract methods
     * */
    @Override
    public View setView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview_item_message_text, parent, false);

        TextView tv_sender = view.findViewById(R.id.txt_sender);
        TextView tv_message = view.findViewById(R.id.txt_message);

        tv_sender.setText(this.sender + " ");
        tv_message.setText(this.msg);

        return view;
    }

    @Override
    public Map<String, Object> convertToMap() {
        Map<String, Object> msg = new HashMap<>();
        msg.put("Sender", this.sender);
        msg.put("Time", this.time);
        msg.put("Msg", this.msg);
        msg.put("Type", 0);

        return msg;
    }
}
