package com.example.chips;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chips.modules.Chats.Chat;
import com.example.chips.modules.Chats.ChatFirestore;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.modules.Messages.Chips;
import com.example.chips.modules.Messages.MessageFirestore;
import com.example.chips.modules.User.BaseUser;
import com.example.chips.modules.User.ChatUser;
import com.example.chips.modules.User.UserAdapter;
import com.example.chips.utils.BitmapFunctions;
import com.example.chips.modules.User.UserFirestore;
import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatSettingsActivity extends AppCompatActivity {
    /*
    *   Chat settings activity
    * */

    /*
     *   Layout variables
     * */
    private TextView TextView_ReturnButton;
    private TextView TextView_ChatName;
    private ListView ListView_ChatUsers;
    private TextView TextView_AddUser;
    private TextView TextView_CreateChip;
    private TextView TextView_DeleteChat;
    private TextView TextView_LeaveChat;

    /*
    *   Activity variables
    * */
    private Bitmap chipImage;
    private boolean isAdmin;
    private int chatId = 0;
    private Chat chat;
    private static UserAdapter userAdapter;

    /*
     *   Add user dialog static variables:
     * */
    public static List<BaseUser> usersSearch;
    private static UserAdapter userSearchAdapter;

    /*
     *   Create global chip variables
     * */
    private ImageView ImageView_ChipImage;
    private Bitmap chatImage;

    /*
     *   update chat information dialog variables
     * */
    private ImageView ImageView_ChatImage;

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_settings);
    }

    protected void onStart() {
        super.onStart();

        initLayout();
        initInfo();
        initButtonListeners();
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   updateChatInformationDialog:
     *      Update chat information dialog
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void updateChatInformationDialog() {
        final Dialog dialog = new Dialog(DataFlowControl.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chat_information_update);

        final Button Button_Cancel = dialog.findViewById(R.id.Button_Cancel);
        final Button Button_Update = dialog.findViewById(R.id.Button_Update);
        final EditText EditText_ChatName = dialog.findViewById(R.id.EditText_ChatName);
        ImageView_ChatImage = dialog.findViewById(R.id.ImageView_ChatImage);

        EditText_ChatName.setText(chat.Name());
        ImageView_ChatImage.setImageBitmap(chatImage);

        Button_Cancel.setOnClickListener((v) ->{
            dialog.dismiss();
        });

        Button_Update.setOnClickListener(v ->
        {
            DataFlowControl.loadingDialog.startLoadingDialog();

            if(!EditText_ChatName.getText().toString().equals("") && !BitmapFunctions.bitmapToString(chatImage).equals("")){
                TextView_ChatName.setText(EditText_ChatName.getText().toString());
                chat.setName(EditText_ChatName.getText().toString());
                chat.setImage(BitmapFunctions.bitmapToString(chatImage));
                ChatFirestore.updateChat(chat);
            }else{
                Toast.makeText(DataFlowControl.context, "Chat information invalid", Toast.LENGTH_SHORT).show();
                DataFlowControl.loadingDialog.dismissLoadingDialog();
            }
            dialog.dismiss();
        });
        ImageView_ChatImage.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(cameraIntent, 0);
            } catch (ActivityNotFoundException error) {
                Toast.makeText(DataFlowControl.context, "An error accrued", Toast.LENGTH_SHORT).show();
            }
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
                UserFirestore.getUsersProfiles("AddUser-Activity");
            }

            if(!EditText_Search.getText().toString().equals("")){
                UserFirestore.getUserProfileByUsername("AddUser-Activity", EditText_Search.getText().toString());
            }
        });

        ListView_Users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                /*
                 *   Checks if the user is already in the chat
                 * */
                boolean userFound = chat.isUser((usersSearch.get(position).Uid()));

                if (userFound) {
                    Toast.makeText(DataFlowControl.context, "User is already in the chat", Toast.LENGTH_SHORT).show();
                } else {
                    /*
                     *   Add user to chat's user list
                     * */
                    ChatFirestore.addUserToChat(usersSearch.get(position).Uid(), chat.Uid());

                    /*
                    *   Gets user by uid and adds to chat
                    * */
                    UserFirestore.getChatUserByUid(chat, usersSearch.get(position).Uid(), false);

                    /*
                     *   Add chat to user's chat list
                     * */
                    UserFirestore.addChatToUser(usersSearch.get(position).Uid(), chat.Uid());

                    Intent intent = new Intent(DataFlowControl.context, ChatActivity.class);
                    intent.putExtra("ChatId", chatId);
                    startActivity(intent);
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
     *   createChipDialog:
     *      Create chip dialog
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void createChipDialog() {
        final Dialog dialog = new Dialog(DataFlowControl.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_chip);

        final Button Button_Cancel = dialog.findViewById(R.id.Button_Cancel);
        final Button Button_Create = dialog.findViewById(R.id.Button_Create);
        final EditText EditText_ChipTitle = dialog.findViewById(R.id.EditText_ChipTitle);
        final EditText EditText_ChipDescription = dialog.findViewById(R.id.EditText_ChipDescription);

        ImageView_ChipImage = dialog.findViewById(R.id.ImageView_ChipImage);

        Button_Cancel.setOnClickListener((v) ->{
            dialog.dismiss();
        });

        Button_Create.setOnClickListener(v ->
        {
            DataFlowControl.loadingDialog.startLoadingDialog();

            boolean isTitle = !EditText_ChipTitle.getText().toString().equals("");
            boolean isDesc = !EditText_ChipDescription.getText().toString().equals("");
            if(chipImage != null){
                Bitmap emptyBitmap = Bitmap.createBitmap(chipImage.getWidth(), chipImage.getHeight(), chipImage.getConfig());
                boolean isImage = !chipImage.sameAs(emptyBitmap);

                if (isTitle && isDesc && isImage) {

                    Timestamp timestamp = new Timestamp(new Date());
                    MessageFirestore.addMessage(new Chips(DataFlowControl.authUser.Username(), timestamp, chipImage, EditText_ChipTitle.getText().toString(), EditText_ChipDescription.getText().toString()));

                    Toast.makeText(DataFlowControl.context, "Chip was created", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }else {
                    Toast.makeText(DataFlowControl.context, "Missing information!", Toast.LENGTH_SHORT).show();

                }
            }

        });
        ImageView_ChipImage.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(cameraIntent, 1);
            } catch (ActivityNotFoundException error) {
                Toast.makeText(DataFlowControl.context, "An error accrued", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

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

        BaseUser user = chat.Users().get(position);

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

        TextView_Username.setText("What would you like to do with "+ chat.Users().get(userId).Username() +"?");
        Button_Cancel.setOnClickListener((v) ->{
            dialog.dismiss();
        });
        Button_UpdateAdminUser.setOnClickListener((v) ->{
            chat.Users().get(userId).setAdmin(true);
            ChatFirestore.updateChatUsers(chat, chatId);
            dialog.dismiss();
        });
        Button_RemoveUser.setOnClickListener((v) ->{
            if(chat.Users().size() <= 2)
                Toast.makeText(DataFlowControl.context, "You cannot remove user", Toast.LENGTH_SHORT).show();
            else {
                chat.Users().remove(chat.Users().get(userId));
                UserFirestore.removeChatUser(chat.Users().get(userId).Uid(), chat.Uid());

                ChatFirestore.updateChatUsers(chat, chatId);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    /*
     *   deleteChatConfirmDialog:
     *      Confirm dialog to delete the chat
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void deleteChatConfirmDialog() {
        AlertDialog dialog = new AlertDialog.Builder(DataFlowControl.context)
                .setTitle("Are you sure you want to delete the chat?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChatFirestore.deleteChatByUid(chatId);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    /*
     *   leaveChatConfirmDialog:
     *      Confirm dialog to leave the chat
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void leaveChatConfirmDialog() {
        AlertDialog dialog = new AlertDialog.Builder(DataFlowControl.context)
                .setTitle("Are you sure you want to leave the chat?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chat.Users().remove(DataFlowControl.authUser);
                        UserFirestore.removeChatUser(DataFlowControl.authUser.Uid(), chat.Uid());

                        ChatFirestore.updateChatUsers(chat, -1);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    /*
     *   convertUserList:
     *      Adds all chat users to a list
     *
     *   Input:
     *       List<ChatUser> users
     *
     *   Output:
     *       List<BaseUser>
     * */
    private List<BaseUser> convertUserList(List<ChatUser> users) {
        List<BaseUser> list = new ArrayList<BaseUser>();
        for (ChatUser user : users){
            list.add((BaseUser)user);
        }
        return list;
    }

    /*
     *   onActivityResult:
     *      Runs when user return to chat settings window
     *
     *   Input:
     *       int requestCode, int resultCode, Intent data
     *
     *   Output:
     *       None
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            chatImage = (Bitmap) extras.get("data");
            ImageView_ChatImage.setImageBitmap(chatImage);
            chat.setImage(BitmapFunctions.bitmapToString(chatImage));
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            chipImage = (Bitmap) extras.get("data");
            ImageView_ChipImage.setImageBitmap(chipImage);
        }
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
            Intent intent = new Intent(DataFlowControl.context, ChatActivity.class);
            intent.putExtra("ChatId", chatId);
            startActivity(intent);
        });

        TextView_ChatName.setOnClickListener(view -> {
            if(DataFlowControl.authUser.isAdmin(chat.Users())) {
                updateChatInformationDialog();
            }
        });

        TextView_CreateChip.setOnClickListener(view ->
        {
            createChipDialog();
        });

         TextView_AddUser.setOnClickListener(view ->
         {
             addUserDialog();
         });

         ListView_ChatUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick (AdapterView <?> adapter, View view, int position, long arg){
                 if(!DataFlowControl.authUser.Uid().equals(chat.Users().get(position).Uid())){
                     userProfileDialog(position);
                 }
             }
         });

         ListView_ChatUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView <?> adapter, View view, int position, long arg) {
                if(!DataFlowControl.authUser.Uid().equals(chat.Users().get(position).Uid()) && isAdmin){
                    updateUserStatusDialog(position);
                }
                return true;
            }
        });

        TextView_DeleteChat.setOnClickListener(view ->
        {
            deleteChatConfirmDialog();
        });

        TextView_LeaveChat.setOnClickListener(view ->
        {
            leaveChatConfirmDialog();
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

        chatId = getIntent().getIntExtra("ChatId", 0);
        chat = DataFlowControl.authUser.getChat(chatId);
        chat.sortUsers();

        isAdmin = DataFlowControl.authUser.isAdmin(chat.Users());
        chatImage = BitmapFunctions.stringToBitMap(chat.Image());

        TextView_ChatName.setText(chat.Name());

        List<BaseUser> baseUserList = convertUserList(chat.Users());
        userAdapter = new UserAdapter(baseUserList);
        ListView_ChatUsers.setAdapter(userAdapter);

        /*
        *   User isn't admin -> Regular user
        * */
        if(!isAdmin){
            TextView_CreateChip.setVisibility(View.GONE);
            TextView_AddUser.setVisibility(View.GONE);
            TextView_DeleteChat.setVisibility(View.GONE);
        }

        /*
         *   Private chat -> 2 users
         * */
        if(chat.Users().size() <= 3){
            TextView_DeleteChat.setVisibility(View.VISIBLE);
            TextView_LeaveChat.setVisibility(View.GONE);
        }
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
        TextView_CreateChip = findViewById(R.id.TextView_CreateChip);
        TextView_ChatName = findViewById(R.id.TextView_ChatName);
        ListView_ChatUsers = findViewById(R.id.ListView_ChatUsers);
        TextView_AddUser = findViewById(R.id.TextView_AddUser);
        TextView_DeleteChat = findViewById(R.id.TextView_DeleteChat);
        TextView_LeaveChat = findViewById(R.id.TextView_LeaveChat);
    }

}