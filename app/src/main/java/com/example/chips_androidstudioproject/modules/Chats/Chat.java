package com.example.chips_androidstudioproject.modules.Chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chips_androidstudioproject.R;
import com.example.chips_androidstudioproject.modules.Messages.CustomMessage;
import com.example.chips_androidstudioproject.modules.User.ChatUser;
import com.example.chips_androidstudioproject.modules.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chat {
    private String name;
    private String uid;
    private String image;
    private List<ChatUser> users;
    private List<CustomMessage> messages;

    /*
     *   Important constructors
     * */
    public Chat(String uid, Map<String, Object> info) {
        this.name = info.get("Name").toString();
        this.uid = uid;
        this.image = info.get("Image").toString();
        this.messages = new ArrayList<CustomMessage>();
        addToUserList((ChatUser[]) info.get("Users"));
    }

    private void addToUserList(ChatUser[] usersArray)
    {
        for (ChatUser user : usersArray)
        {
            users.add(user);
        }
    }

    public View setView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview_item_chat, parent, false);

        TextView txt_name = view.findViewById(R.id.txt_name);

        txt_name.setText(this.name);

        return view;
    }

    public String Name() {
        return this.name;
    }

    public List<CustomMessage> Messages() {
        return this.messages;
    }

    public void addMessage(CustomMessage msg){
        this.messages.add(msg);
    }

    public List<ChatUser> Users(){
        return this.users;
    }

    public String Uid() {
        return this.uid;
    }
}
