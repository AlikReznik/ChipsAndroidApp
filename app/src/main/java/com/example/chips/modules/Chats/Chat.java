package com.example.chips.modules.Chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.chips.R;
import com.example.chips.modules.Messages.CustomMessage;
import com.example.chips.modules.User.ChatUser;
import com.example.chips.modules.User.UserFirestore;
import com.example.chips.utils.BitmapFunctions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chat {
    /*
    *   Chat class
    * */
    /*
    *   Chat variables:
    * */
    private String name;
    private String uid;
    private String image;
    private List<ChatUser> users;
    private List<CustomMessage> messages;

    /*
     *   Chat:
     *      Chat constructor
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public Chat(String uid, Map<String, Object> info) {
        this.name = info.get("Name").toString();
        this.uid = uid;
        this.image = info.get("Image").toString();
        this.messages = new ArrayList<CustomMessage>();
        this.users = new ArrayList<ChatUser>();

        addToUserList((ArrayList<Map<String, Object>>) info.get("Users"));
    }

    /*
     *   setView:
     *      Sets chat information to chats listview layout
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public View setView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview_item_chat, parent, false);

        TextView TextView_ChatName = view.findViewById(R.id.TextView_ChatName);
        TextView TextView_ChatUsers = view.findViewById(R.id.TextView_ChatUsers);
        ImageView ImageView_ChatImage = view.findViewById(R.id.ImageView_ChatImage);

        TextView_ChatName.setText(this.name);
        if(!getUserUsername().equals(""))
            TextView_ChatUsers.setText(getUserUsername());
        ImageView_ChatImage.setImageBitmap(BitmapFunctions.stringToBitMap(this.image));

        return view;
    }

    /*
     *   sortUsers:
     *      Sorts chat users by admin & regular users
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public void sortUsers() {
        boolean sorted = false;
        ChatUser temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < this.users.size() - 1; i++) {
                if (ChatUser.boolToInt(this.users.get(i).isAdmin()) < ChatUser.boolToInt(this.users.get(i+1).isAdmin())) {
                    temp = this.users.get(i);
                    this.users.set(i, this.users.get(i+1));
                    this.users.set(i+1, temp);
                    sorted = false;
                }
            }
        }
    }

    /*
     *   addToUserList:
     *      Adds chat to user
     *
     *   Input:
     *       ArrayList<Map<String, Object>> usersArray
     *   Output:
     *       None
     * */
    private void addToUserList(ArrayList<Map<String, Object>> usersArray)
    {
        if(usersArray != null && usersArray.size() > 0){
            for (Map<String, Object> user : usersArray)
            {
                UserFirestore.getChatUserByUid(this, user.get("UserUid").toString(), (boolean) user.get("IsAdmin"));
            }
        }
    }

    /*
     *   setName:
     *      Sets new name
     *
     *   Input:
     *       String name
     *   Output:
     *       None
     * */
    public void setName(String name) {
        this.name = name;
    }

    /*
     *   setImage:
     *      Sets new image
     *
     *   Input:
     *       String image
     *   Output:
     *       None
     * */
    public void setImage(String image) {
        this.image = image;
    }

    /*
     *   addUser:
     *      Adds user to chat user list
     *
     *   Input:
     *       ChatUser user
     *   Output:
     *       None
     * */
    public void addUser(ChatUser user){
        if(user != null)
            this.users.add(user);
    }

    /*
     *   addMessage:
     *      Adds message to message list
     *
     *   Input:
     *       CustomMessage msg
     *   Output:
     *       None
     * */
    public void addMessage(CustomMessage msg){
        this.messages.add(msg);
    }

    /*
     *   Users:
     *      Returns users
     *
     *   Input:
     *       None
     *   Output:
     *       List<ChatUser>
     * */
    public List<ChatUser> Users(){
        return this.users;
    }

    /*
     *   Name:
     *      Returns name
     *
     *   Input:
     *       None
     *   Output:
     *       String
     * */
    public String Name() {
        return this.name;
    }

    /*
     *   Image:
     *      Returns image
     *
     *   Input:
     *       None
     *   Output:
     *       String
     * */
    public String Image() {
        return this.image;
    }

    /*
     *   Image:
     *      Returns uid
     *
     *   Input:
     *       None
     *   Output:
     *       String
     * */
    public String Uid() {
        return this.uid;
    }
    /*
     *   Messages:
     *      Returns messages
     *
     *   Input:
     *       None
     *   Output:
     *       List<CustomMessage>
     * */
    public List<CustomMessage> Messages() {
        return this.messages;
    }

    /*
     *   getUserUsername:
     *      Returns chat user's usernames
     *
     *   Input:
     *       None
     *   Output:
     *       String
     * */
    private String getUserUsername() {
        String result = "";
        for (ChatUser user : this.users){
            result+=user.Username()+"| ";
        }
        return result;
    }

    /*
     *   isUser:
     *       Checks if user is in the chat
     *
     *   Input:
     *       User uid
     *   Output:
     *       True - If user was found
     *       False - If user wasn't found
     * */
    public boolean isUser(String uid) {
        for(ChatUser user : this.users){
            if(user.Uid().equals(uid))
                return true;
        }

        return false;
    }

    /*
     *   getMessage:
     *      Returns message by position in the message list
     *
     *   Input:
     *       int position
     *   Output:
     *       CustomMessage
     * */
    public CustomMessage getMessage(int position) {
        return this.messages.get(position);
    }
}
