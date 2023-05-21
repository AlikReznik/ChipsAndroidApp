package com.example.chips.modules.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.chips.R;
import com.example.chips.utils.BitmapFunctions;
import java.util.Map;

public class BaseUser {
    /*
    *   BaseUser class
    * */
    /*
     *  Base user variables:
     * */
    protected String username;
    protected String email;
    protected String image;
    protected String uid;
    protected String bio;

    /*
     *   BaseUser:
     *      Base user constructor
     *
     *   Input:
     *       String uid
     *   Output:
     *       None
     * */
    public BaseUser(String uid) {
        this.uid = uid;
        this.username = "";
        this.bio = "";
        this.image = "";
        this.email = "";
    }

    /*
     *   BaseUser:
     *      Base user constructor
     *
     *   Input:
     *       String uid, Map<String, Object> info
     *
     *   Output:
     *       None
     * */
    public BaseUser(String uid, Map<String, Object> info) {
        this.uid = uid;
        this.bio = info.get("Bio").toString();
        this.username = info.get("Username").toString();
        this.image = info.get("Image").toString();
    }

    /*
     *   BaseUser:
     *      Base user constructor
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public BaseUser() {}

    /*
     *   update:
     *      Updates user information
     *
     *   Input:
     *       String uid, Map<String, Object> info
     *   Output:
     *       None
     * */
    public void update(String uid, Map<String, Object> info) {
        this.uid = uid;
        this.bio = info.get("Bio").toString();
        this.username = info.get("Username").toString();
        this.image = info.get("Image").toString();
        this.email = info.get("Email").toString();
    }

    /*
     *   setView:
     *      Listview item setter
     *
     *   Input:
     *       LayoutInflater layoutInflater, ViewGroup parent
     *   Output:
     *       View
     * */
    public View setView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview_item_user_profile, parent, false);

        TextView TextView_Username = view.findViewById(R.id.TextView_Username);
        ImageView ImageView_UserImage = view.findViewById(R.id.ImageView_UserImage);

        TextView_Username.setText(this.username);
        ImageView_UserImage.setImageBitmap(BitmapFunctions.stringToBitMap(this.image));

        return view;
    }

    /*
     *   Uid:
     *      Returns user uid
     *
     *   Input:
     *       None
     *
     *   Output:
     *       String
     * */
    public String Uid() {
        return this.uid;
    }

    /*
     *   Username:
     *      Returns user username
     *
     *   Input:
     *       None
     *
     *   Output:
     *       String
     * */
    public String Username() {
        return this.username;
    }

    /*
     *   Image:
     *      Returns user image
     *
     *   Input:
     *       None
     *
     *   Output:
     *       String
     * */
    public String Image() {
        return this.image;
    }

    /*
     *   Bio:
     *      Returns user bio
     *
     *   Input:
     *       None
     *   Output:
     *       String
     * */
    public String Bio() {
        return this.bio;
    }

    /*
     *   Bio:
     *      Returns user email
     *
     *   Input:
     *       None
     *   Output:
     *       String
     * */
    public String Email() {
        return this.email;
    }

    /*
     *   setUid:
     *      Sets new uid
     *
     *   Input:
     *       None
     *   Output:
     *       String
     * */
    public void setUid(String uid){
        this.uid = uid;
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

        return "BaseUser{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", image='" + imageOutput + '\'' +
                ", uid='" + uid + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
