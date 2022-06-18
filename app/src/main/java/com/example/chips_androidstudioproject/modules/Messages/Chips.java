package com.example.chips_androidstudioproject.modules.Messages;

import android.graphics.Bitmap;
import android.util.Log;
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

public class Chips  extends CustomMessage{
    /*
     * Special message data
     * */
    public String title, description;
    public Bitmap image;

    /*
     * Constructors
     * */
    public Chips(String sender, Timestamp time, Bitmap image, String title, String description)
    {
        super(sender, time);
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public Chips() { }

    /*
     * Implementation of the abstract methods
     * */
    @Override
    public View setView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview_item_message_chips, parent, false);

        TextView txt_sender = view.findViewById(R.id.txt_sender);
        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_description = view.findViewById(R.id.txt_description);
        ImageView iv_image = view.findViewById(R.id.imageView);

        txt_sender.setText("Sent by: "+this.sender);
        txt_title.setText(this.title);
        txt_description.setText(this.description);
        iv_image.setImageBitmap(this.image);

        return view;
    }

    @Override
    public Map<String, Object> convertToMap() {
        Map<String, Object> msg = new HashMap<>();
        msg.put("sender", this.sender);
        msg.put("time", this.time);
        msg.put("title", this.title);
        msg.put("desc", this.description);
        msg.put("image", Utils.BitmapToString(this.image));
        msg.put("type", -1);

        return msg;
    }

}
