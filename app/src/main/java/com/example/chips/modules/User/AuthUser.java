package com.example.chips.modules.User;

import android.util.Log;
import com.example.chips.mainFragments.ChatsFragment;
import com.example.chips.modules.Chats.Chat;
import com.example.chips.modules.Chats.ChatFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AuthUser extends BaseUser {
    /*
    *   AuthUser class - logged in user
    * */
    /*
     *  Auth user variables:
     * */
    private boolean status;
    private List<Chat> chats;

    /*
     *   AuthUser:
     *      Auth user constructor
     *
     *   Input:
     *       String uid
     *   Output:
     *       None
     * */
    public AuthUser(String uid) {
        super(uid);
        this.status = true;
        this.chats = new ArrayList<Chat>();
    }

    /*
     *   AuthUser:
     *      Auth user constructor
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public AuthUser(){
        super();
        this.status = false;
        this.chats = new ArrayList<Chat>();
    }

    /*
     *   update:
     *      Updates user information
     *
     *   Input:
     *       String uid, Map<String, Object> info
     *   Output:
     *       None
     * */
    @Override
    public void update(String uid, Map<String, Object> info) {
        this.uid = uid;
        this.status = true;

        if(info.get("Username") !=  null){
            this.username = info.get("Username").toString();
        }
        if(info.get("Bio") !=  null){
            this.bio = info.get("Bio").toString();
        }
        if(info.get("Image") !=  null){
            this.image = info.get("Image").toString();
        }
        if(info.get("Email") != null) {
            this.email = Objects.requireNonNull(info.get("Email")).toString();
        }
        if(info.get("Chats") != null){
            this.chats = new ArrayList<Chat>();
            setChatsData((ArrayList<String>)info.get("Chats"));
        }
    }

    /*
     *   toString:
     *      Creates a string of the information about the user
     *
     *   Input:
     *       None
     *   Output:
     *       String
     * */
    @Override
    public String toString() {
        String imageOutput = "";
        if(!this.image.equals("")){
            imageOutput = "...";
        }
        return "AuthUser{" +
                "status=" + status +
                ", chats=" + chats +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", image='" + imageOutput + '\'' +
                ", uid='" + uid + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }

    /*
     *   setChatsData:
     *      Sets chat data
     *
     *   Input:
     *       List<String> chatsUid
     *
     *   Output:
     *       String
     * */
    private void setChatsData(List<String> chatsUid) {
        if(chatsUid.size() > 0){
            Log.d("AuthUser", "chats uids: "+chatsUid.toString());
            for(String uid : chatsUid)
                ChatFirestore.getChat(uid);
        }
    }

    /*
     *   Chats:
     *      Returns all chats
     *
     *   Input:
     *       None
     *
     *   Output:
     *       List<Chat>
     * */
    public List<Chat> Chats() {
        return this.chats;
    }

    /*
     *   getChat:
     *      Returns chat by position in the chat list
     *
     *   Input:
     *       int position
     *
     *   Output:
     *       Chat
     * */
    public Chat getChat(int position) {
        return this.chats.get(position);
    }

    /*
     *   isAdmin:
     *      Return if the user is an admin in the chat or not
     *
     *   Input:
     *       List<ChatUser> users
     *
     *   Output:
     *       boolean
     * */
    public boolean isAdmin(List<ChatUser> users) {
        for (ChatUser user : users){
            if(user.Uid().equals(this.uid))
                return user.isAdmin;
        }
        return false;
    }

    /*
     *   set:
     *      Converts User to a map
     *
     *   Input:
     *       None
     *
     *   Output:
     *       Map<String, Object>
     * */
    public Map<String, Object> set() {
        Map<String, Object> userSet = new HashMap<>();
        userSet.put("Email", this.email);
        userSet.put("Username", this.username);

        List<String> chatList = new ArrayList<String>();
        for(Chat chat : this.chats){
            chatList.add(chat.Uid());
        }
        userSet.put("Image", this.image);
        userSet.put("Chats", chatList);
        userSet.put("Bio", this.bio);

        return userSet;
    }

    /*
     *   addChat:
     *      Adds new chat to chat list
     *
     *   Input:
     *       String uid, Map<String, Object> info
     *
     *   Output:
     *       None
     * */
    public void addChat(String uid, Map<String, Object> info){
        this.chats.add(new Chat(uid, info));
        ChatsFragment.updateListView();
    }

    /*
     *   deleteChat:
     *      Removes chat from chat list by position
     *
     *   Input:
     *       int position
     *
     *   Output:
     *       None
     * */
    public void deleteChat(int position) {
        this.chats.remove(position);
    }

    /*
     *   sortChats:
     *      Sorts chats
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public void sortChats() {
        boolean sorted = false;
        Chat temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < this.chats.size() - 1; i++) {
                if (this.chats.get(i).Users().size() > this.chats.get(i+1).Users().size()) {
                    temp = this.chats.get(i);
                    this.chats.set(i, this.chats.get(i+1));
                    this.chats.set(i+1, temp);
                    sorted = false;
                }
            }
        }
    }

    /*
     *   clear:
     *      Clears user on logout
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public void clear() {
        this.uid = null;
        this.status = false;
    }
}
