package com.example.chips.mainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.example.chips.CreateChatActivity;
import com.example.chips.R;
import com.example.chips.ChatActivity;
import com.example.chips.modules.Chats.ChatAdapter;
import com.example.chips.utils.DataFlowControl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatsFragment extends Fragment {
    /*
    *   Fragment that shows all user chats
    * */
    /*
     *   Layout variables
     * */
    private ListView ListView_Chats;
    private FloatingActionButton Button_CreateChat;

    /*
    *   Fragment variables
    * */
    private static ChatAdapter chatAdapter;

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ChatsFragment() {
        super(R.layout.fragment_chats);
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
     *   updateListView:
     *      Updates chat list on call
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public static void updateListView() {
        if (chatAdapter == null)
            chatAdapter = new ChatAdapter();
        DataFlowControl.authUser.sortChats();
        chatAdapter.notifyDataSetChanged();
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
        ListView_Chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent intent = new Intent(DataFlowControl.context, ChatActivity.class);
                intent.putExtra("ChatId", position);
                startActivity(intent);
            }
        });
        Button_CreateChat.setOnClickListener(view ->
        {
            Intent intent = new Intent(DataFlowControl.context, CreateChatActivity.class);
            startActivity(intent);
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
        DataFlowControl.authUser.sortChats();
        if (chatAdapter == null)
            chatAdapter = new ChatAdapter();

        ListView_Chats.setAdapter(chatAdapter);
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
        ListView_Chats = getActivity().findViewById(R.id.ListView_Chats);
        Button_CreateChat = getActivity().findViewById(R.id.Button_CreateChat);
    }
}
