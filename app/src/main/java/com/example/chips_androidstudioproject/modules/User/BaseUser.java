package com.example.chips_androidstudioproject.modules.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.chips_androidstudioproject.R;
import com.example.chips_androidstudioproject.modules.Chats.Chat;
import com.example.chips_androidstudioproject.modules.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class BaseUser {
    protected String username;
    protected String email;
    protected String image;
    protected String uid;
    protected String bio;

    public BaseUser(String uid) {
        this.uid = uid;
        this.username = "";
        this.bio = "";
        this.image = "";
        this.email = "";
    }

    public BaseUser() {}

    public void update(String uid, @NonNull Map<String, Object> info) {
        this.uid = uid;
        this.bio = Objects.requireNonNull(info.get("Bio")).toString();
        this.username = Objects.requireNonNull(info.get("Username")).toString();
        this.image = Objects.requireNonNull(info.get("Image")).toString();
        this.email = Objects.requireNonNull(info.get("Email")).toString();
    }
    /*
     *   Getters and Setters
     * */
    public String Uid() {
        return this.uid;
    }

    public String Username() {
        return this.username;
    }

    public String Image() {
        return this.image;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String Bio() {
        return this.bio;
    }

    public View setView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview_item_profileuser, parent, false);

        TextView txt_name = view.findViewById(R.id.txt_name);
        TextView txt_bio = view.findViewById(R.id.txt_bio);
        ImageView iv_image = view.findViewById(R.id.imageview_image);

        txt_name.setText(this.username);
        txt_bio.setText(this.bio);
        iv_image.setImageBitmap(Utils.StringToBitMap(this.image));

        return view;
    }
}
