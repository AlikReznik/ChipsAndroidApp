package com.example.chips;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.utils.BitmapFunctions;
import com.example.chips.modules.Chats.ChatFirestore;
import com.example.chips.modules.User.BaseUser;
import com.example.chips.modules.User.ChatUser;
import com.example.chips.modules.User.UserAdapter;
import com.example.chips.modules.User.UserFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateChatActivity extends AppCompatActivity {
    /*
    *   Create chat activity
    * */

    /*
     *   Layout variables
     * */
    private TextView TextView_ReturnButton;
    private EditText EditText_ChatName;
    private ListView ListView_ChatUsers;
    private TextView TextView_AddUser;
    private TextView TextView_CreateChat;

    /*
     *   Activity variables
     * */
    private static UserAdapter userAdapter;

    /*
     *   Chat Information
     * */
    private List<BaseUser> chatUsers;

    /*
     *   Add user dialog static variables:
     * */
    public static List<BaseUser> usersSearch;
    private static UserAdapter userSearchAdapter;

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);
    }


    protected void onStart() {
        super.onStart();

        initLayout();
        initInfo();
        initButtonListeners();
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   userProfileDialog:
     *      Show user's profile
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void userProfileDialog(int position){
        final Dialog dialog = new Dialog(DataFlowControl.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_user_profile);

        BaseUser user = chatUsers.get(position);

        ImageView ImageView_UserImage = dialog.findViewById(R.id.ImageView_UserImage);
        TextView TextView_Username = dialog.findViewById(R.id.TextView_Username);
        TextView TextView_Bio = dialog.findViewById(R.id.TextView_UserBio);
        TextView TextView_CreatePrivateChat = dialog.findViewById(R.id.TextView_CreatePrivateChat);


        if(BitmapFunctions.stringToBitMap(user.Image()) != null){
            ImageView_UserImage.setImageBitmap(BitmapFunctions.stringToBitMap(user.Image()));
        }
        TextView_Username.setText(user.Username());
        TextView_Bio.setText(user.Bio());

        TextView_CreatePrivateChat.setOnClickListener(view ->
        {
            dialog.dismiss();
            ChatFirestore.findPrivateChat(user.Uid(), user.Username());
        });

        dialog.show();
    }

    /*
     *   updateUserStatusDialog:
     *      Updates user to become an admin or remove from the chat
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void updateUserStatusDialog(int userId) {
        final Dialog dialog = new Dialog(DataFlowControl.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_custom_user_long_click);

        final TextView TextView_Username = dialog.findViewById(R.id.TextView_Username);
        final Button Button_Cancel = dialog.findViewById(R.id.Button_Cancel);
        final Button Button_RemoveUser = dialog.findViewById(R.id.Button_RemoveUser);
        final Button Button_UpdateAdminUser = dialog.findViewById(R.id.Button_UpdateAdminUser);

        TextView_Username.setText("What would you like to do with "+ chatUsers.get(userId).Username() +"?");
        Button_Cancel.setOnClickListener((v) ->{
            dialog.dismiss();
        });
        Button_UpdateAdminUser.setOnClickListener((v) ->{
            ChatUser user = (ChatUser) chatUsers.get(userId);
            user.setAdmin(true);
            updateListViewProfileUserAdapter();
            dialog.dismiss();
        });
        Button_RemoveUser.setOnClickListener((v) ->{
            chatUsers.remove(userId);
            updateListViewProfileUserAdapter();
            dialog.dismiss();
        });

        dialog.show();
    }

    /*
     *   addUserDialog:
     *      Add user to chat dialog
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void addUserDialog() {
        final Dialog dialog = new Dialog(DataFlowControl.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chat_setting_user_search);

        final ImageView ImageView_Search = dialog.findViewById(R.id.ImageView_Search);
        final ListView ListView_Users = dialog.findViewById(R.id.ListView_Users);
        final EditText EditText_Search = dialog.findViewById(R.id.EditText_Search);
        final TextView TextView_CancelButton = dialog.findViewById(R.id.TextView_CancelButton);

        usersSearch = new ArrayList<BaseUser>();
        userSearchAdapter = new UserAdapter(usersSearch);
        ListView_Users.setAdapter(userSearchAdapter);

        TextView_CancelButton.setOnClickListener((v) ->{
            dialog.dismiss();
        });

        ImageView_Search.setOnClickListener(v ->
        {
            DataFlowControl.loadingDialog.startLoadingDialog();
            if(EditText_Search.getText().toString().equals("")){
                UserFirestore.getUsersProfiles("AddUserCreateChat-Activity");
            }

            if(!EditText_Search.getText().toString().equals("")){
                UserFirestore.getUserProfileByUsername("AddUserCreateChat-Activity", EditText_Search.getText().toString());
            }
        });

        ListView_Users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                /*
                 *   Checks if the user is already in the chat
                 * */

                boolean userFound = false;

                for(BaseUser user : chatUsers){
                    if(user.Uid().equals(usersSearch.get(position).Uid())){
                        userFound = true;
                    }
                }

                if (userFound) {
                    Toast.makeText(DataFlowControl.context, "User is already in the chat", Toast.LENGTH_SHORT).show();
                } else {
                    BaseUser baseUser = usersSearch.get(position);
                    Map<String, Object> data = new HashMap<String, Object>();

                    data.put("Bio", baseUser.Bio());
                    data.put("Username", baseUser.Username());
                    data.put("Image", baseUser.Image());
                    data.put("Email", baseUser.Email());

                    ChatUser user = new ChatUser(baseUser.Uid(), data, false);
                    chatUsers.add(user);
                    updateListViewProfileUserAdapter();
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*
     *   updateListViewUserSearch:
     *      Updates search user listview
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public static void updateListViewUserSearch() {
        userSearchAdapter.notifyDataSetChanged();
    }

    /*
     *   updateListViewProfileUserAdapter:
     *      Updates chat user listview
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void updateListViewProfileUserAdapter() {
        userAdapter.notifyDataSetChanged();
    }

    /*
     *   convertUserList:
     *      Adds all chat users to a list
     *
     *   Input:
     *       List<BaseUser> users
     *
     *   Output:
     *       List<ChatUser>
     * */
    private List<ChatUser> convertUserList(List<BaseUser> users) {
        List<ChatUser> list = new ArrayList<ChatUser>();
        for (BaseUser user : users){
            list.add((ChatUser)user);
        }
        return list;
    }


    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   initButtonListeners:
     *      Button listeners
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void initButtonListeners() {
        TextView_ReturnButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(DataFlowControl.context, MainActivity.class);
            startActivity(intent);
        });

        ListView_ChatUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView <?> adapter, View view, int position, long arg){
                userProfileDialog(position);
            }
        });

        ListView_ChatUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView <?> adapter, View view, int position, long arg) {
                updateUserStatusDialog(position);
                return true;
            }
        });

        TextView_AddUser.setOnClickListener(view ->
        {
            addUserDialog();
        });

        TextView_CreateChat.setOnClickListener(view ->
        {
            if(EditText_ChatName.getText().toString().equals("")){
                Toast.makeText(DataFlowControl.context, "Change chat name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(chatUsers.size() < 2){
                Toast.makeText(DataFlowControl.context, "Add more users to the chat", Toast.LENGTH_SHORT).show();
                return;
            }

            ChatFirestore.createChat(ChatFirestore.createChatSet(EditText_ChatName.getText().toString(), "", 1, convertUserList(chatUsers)));
        });
    }

    /*
     *   initInfo:
     *      Sets layout information
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void initInfo() {
        DataFlowControl.context = this;
        DataFlowControl.activity = this;

        chatUsers = new ArrayList<BaseUser>();

        userAdapter = new UserAdapter(chatUsers);
        ListView_ChatUsers.setAdapter(userAdapter);
    }

    /*
     *   initLayout:
     *      Sets layout variables
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
        private void initLayout() {
        TextView_ReturnButton = findViewById(R.id.TextView_ReturnButton);
        EditText_ChatName = findViewById(R.id.EditText_ChatName);
        ListView_ChatUsers = findViewById(R.id.ListView_ChatUsers);
        TextView_AddUser = findViewById(R.id.TextView_AddUser);
        TextView_CreateChat = findViewById(R.id.TextView_CreateChat);
    }

}