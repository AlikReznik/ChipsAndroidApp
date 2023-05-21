package com.example.chips.modules.User;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.chips.R;
import com.example.chips.utils.BitmapFunctions;
import java.util.HashMap;
import java.util.Map;

public class ChatUser extends BaseUser {
    /*
    *   ChatUser class
    * */
    /*
     *  Chat user variables:
     * */
    protected boolean isAdmin;

    /*
     *   ChatUser:
     *      Auth user constructor
     *
     *   Input:
     *       String uid
     *   Output:
     *       None
     * */
    public ChatUser(String uid, Map<String, Object> data, boolean isAdmin) {
        super(uid, data);
        this.isAdmin = isAdmin;
    }

    /*
     *   setView:
     *      Listview item setter
     *
     *   Input:
     *       LayoutInflater layoutInflater, ViewGroup parent
     *   Output:
     *       None
     * */
    @Override
    public View setView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview_item_user_profile, parent, false);

        TextView TextView_Username = view.findViewById(R.id.TextView_Username);
        ImageView ImageView_UserImage = view.findViewById(R.id.ImageView_UserImage);

        TextView_Username.setText(this.username);
        if(this.isAdmin)
            TextView_Username.setTextColor(Color.parseColor("#eb4634"));
        ImageView_UserImage.setImageBitmap(BitmapFunctions.stringToBitMap(this.image));

        return view;
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

        return "ChatUser{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", image='" + imageOutput + '\'' +
                ", uid='" + uid + '\'' +
                ", bio='" + bio + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    /*
     *   isAdmin:
     *      Returns if the user is an admin or not
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public boolean isAdmin() {
        return this.isAdmin;
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
        Map<String, Object> compactUser = new HashMap<>();
        compactUser.put("IsAdmin", this.isAdmin);
        compactUser.put("Uid", this.uid);
        return compactUser;
    }

    /*
     *   setAdmin:
     *      Sets new user state => true is admin false is a regular user
     *
     *   Input:
     *       boolean state
     *
     *   Output:
     *       None
     * */
    public void setAdmin(boolean state) {
        this.isAdmin = state;
    }

    /*
     *   boolToInt:
     *      Translates bool state to int => true - 1, false - 0
     *
     *   Input:
     *       boolean state
     *
     *   Output:
     *       int
     * */
    public static int boolToInt(boolean state) {
        return state ? 1 : 0;
    }

}
