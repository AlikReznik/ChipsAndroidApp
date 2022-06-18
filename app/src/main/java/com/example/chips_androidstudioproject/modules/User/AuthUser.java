package com.example.chips_androidstudioproject.modules.User;

import androidx.annotation.NonNull;
import com.example.chips_androidstudioproject.modules.Chats.Chat;
import com.example.chips_androidstudioproject.modules.Chats.ChatFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AuthUser extends BaseUser {
    private boolean status;
    private List<Chat> chats;
    /*
     *   Important constructors
     * */
    public AuthUser(){
        super();
        this.status = false;
        this.chats = new ArrayList<Chat>();
    }

    public AuthUser(String uid) {
        super(uid);
        this.status = true;
        this.chats = new ArrayList<Chat>();
    }

    /*
     *   Update user's data when it gets from firestore
     * */
    public void update(String uid, @NonNull Map<String, Object> info) {
        this.uid = uid;
        this.status = true;

        this.username = Objects.requireNonNull(info.get("Username")).toString();
        this.image = Objects.requireNonNull(info.get("Image")).toString();
        if(info.get("Bio") !=  null){
            this.bio = info.get("Bio").toString();
        }
        if(info.get("Image") !=  null){
            this.image = info.get("Image").toString();
        }
        if(info.get("Email") != null && !Objects.requireNonNull(info.get("Email")).toString().equals("")) {
            this.email = Objects.requireNonNull(info.get("Email")).toString();
            this.chats = new ArrayList<Chat>();
            setChatsData((String[])Objects.requireNonNull(info.get("Chats")));
        }
    }

    private void setChatsData(String[] chatsUid) {
        for(String uid : chatsUid)
            ChatFirestore.getChat(uid);
    }

    public void addChat(String uid, Map<String, Object> info){

        this.chats.add(new Chat(uid, info));
    }

    /*
     *   Clears user on logout
     * */
    public void clear() {
        this.uid = null;
        this.status = false;
    }

    public List<Chat> Chats() {
        return this.chats;
    }

    public Chat getChat(int id) {
        return this.Chats().get(id);
    }

    /*
    * Converts User to a map
    * */
    public Map<String, Object> set() {
        Map<String, Object> userSet = new HashMap<>();
        userSet.put("Email", this.email);
        userSet.put("Username", this.username);
        userSet.put("Username", this.username);
        userSet.put("Image", this.image);
        userSet.put("Chats", this.chats);
        userSet.put("Bio", this.bio);

        return userSet;
    }
}
