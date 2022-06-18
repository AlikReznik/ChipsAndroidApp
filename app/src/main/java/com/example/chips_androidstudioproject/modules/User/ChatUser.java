package com.example.chips_androidstudioproject.modules.User;

import java.util.Map;

public class ChatUser extends BaseUser {
    boolean isAdmin;

    public ChatUser(String uid, boolean isAdmin) {
        super(uid);
        this.isAdmin = isAdmin;
    }

    public void update(String uid, Map<String, Object> data) {}
}
