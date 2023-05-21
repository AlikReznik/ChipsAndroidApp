package com.example.chips.mainFragments;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.chips.R;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.utils.BitmapFunctions;
import com.example.chips.modules.Chats.ChatFirestore;
import com.example.chips.modules.User.UserAdapter;
import com.example.chips.modules.User.BaseUser;
import com.example.chips.modules.User.UserFirestore;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    /*
    *   Searches users by username and gives information about them
    * */
    /*
     *   Layout variables
     * */
    private ListView ListView_Users;
    private ImageView ImageView_Search;
    private EditText EditText_Search;

    /*
     *   Fragment variables
     * */
    public static List<BaseUser> users;
    private static UserAdapter userAdapter;

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onStart() {
        super.onStart();

        initLayout();
        initInfo();
        initButtonListeners();
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   userProfileDialog:
     *      Creates dialog with user information
     *
     *   Input:
     *       int position
     *
     *   Output:
     *       None
     * */
    private void userProfileDialog(int position){
        final Dialog dialog = new Dialog(DataFlowControl.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_user_profile);

        /*---------------------------------------------------------------------------------------------------*/

        /*
        *   Dialog variables
        * */
        BaseUser user;

        /*
         *   Layout variables
         * */
        ImageView ImageView_UserImage = dialog.findViewById(R.id.ImageView_UserImage);
        TextView TextView_Username = dialog.findViewById(R.id.TextView_Username);
        TextView TextView_Bio = dialog.findViewById(R.id.TextView_UserBio);
        TextView TextView_CreatePrivateChat = dialog.findViewById(R.id.TextView_CreatePrivateChat);

        /*---------------------------------------------------------------------------------------------------*/

        /*
         *   Initializing information
         * */

        user = SearchFragment.users.get(position);

        if(BitmapFunctions.stringToBitMap(user.Image()) != null){
            ImageView_UserImage.setImageBitmap(BitmapFunctions.stringToBitMap(user.Image()));
        }
        TextView_Username.setText(user.Username());
        TextView_Bio.setText(user.Bio());

        /*
         *   Button Listeners
         *  */
        TextView_CreatePrivateChat.setOnClickListener(view ->
        {
            dialog.dismiss();
            ChatFirestore.findPrivateChat(user.Uid(), user.Username());
        });

        dialog.show();
    }

     /*
     *   updateListViewProfileUserAdapter:
     *      Updates users list on call
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public static void updateListViewProfileUserAdapter() {
        userAdapter.notifyDataSetChanged();
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
        ImageView_Search.setOnClickListener(view ->
        {
            DataFlowControl.loadingDialog.startLoadingDialog();
            if(EditText_Search.getText().toString().equals("")){
                UserFirestore.getUsersProfiles("Search-Fragment");
            }

            if(!EditText_Search.getText().toString().equals("")){
                UserFirestore.getUserProfileByUsername("Search-Fragment", EditText_Search.getText().toString());
            }
        });

        ListView_Users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView <?> adapter, View view, int position, long arg){
                userProfileDialog(position);

            }
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
        users = new ArrayList<BaseUser>();

        userAdapter = new UserAdapter(users);
        ListView_Users.setAdapter(userAdapter);
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
    private void initLayout(){
        ImageView_Search = getActivity().findViewById(R.id.ImageView_Search);
        ListView_Users = getActivity().findViewById(R.id.ListView_Users);
        EditText_Search = getActivity().findViewById(R.id.EditText_Search);
    }
}