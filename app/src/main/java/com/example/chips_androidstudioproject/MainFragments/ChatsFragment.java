package com.example.chips_androidstudioproject.MainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.example.chips_androidstudioproject.R;
import com.example.chips_androidstudioproject.chat_activity;
import com.example.chips_androidstudioproject.modules.Chats.ChatAdapter;
import com.example.chips_androidstudioproject.modules.DataFlowControl;

public class ChatsFragment  extends Fragment {
    private ListView lv_chats;
    private static ChatAdapter chatAdapter;
    public ChatsFragment() {
        super(R.layout.fragment_chats);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initLayout();
        initInfo();
        initButtonListeners();
    }

    private void initInfo() {
        chatAdapter = new ChatAdapter();
        lv_chats.setAdapter(chatAdapter);
    }

    private void initButtonListeners() {
        lv_chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView <?> adapter, View view, int position, long arg){
                Intent intent = new Intent(DataFlowControl.context, chat_activity.class);
                intent.putExtra("ChatId", position);
                startActivity(intent);
            }
        });
    }

    private void initLayout(){
        lv_chats = getActivity().findViewById(R.id.lv_chats);
    }
}
