package com.example.chips_androidstudioproject.modules.Messages;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chips_androidstudioproject.R;
import com.example.chips_androidstudioproject.modules.Utils;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class ImageMessage extends CustomMessage{
    /*
     * Special message data
     * */
    public Bitmap msg;

    /*
     * Constructors
     * */
    public ImageMessage(String sender, Timestamp time, Bitmap msg)
    {
        super(sender, time);
        this.msg = msg;
    }

    public ImageMessage() { }

    /*
     * Implementation of the abstract methods
     * */
    @Override
    public View setView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview_item_message_image, parent, false);

        TextView tv_sender = view.findViewById(R.id.txt_sender);
        ImageView iv_image = view.findViewById(R.id.imageview_image);

        tv_sender.setText(this.sender + " ");
        iv_image.setImageBitmap(this.msg);

        return view;
    }

    @Override
    public Map<String, Object> convertToMap() {
        Map<String, Object> msg = new HashMap<>();
        msg.put("Sender", this.sender);
        msg.put("Time", this.time);
        msg.put("Msg", Utils.BitmapToString(this.msg));
        msg.put("Type", 1);

        return msg;
    }

}
