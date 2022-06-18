package com.example.chips_androidstudioproject.modules.Messages;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.chips_androidstudioproject.modules.Chats.Chat;
import com.example.chips_androidstudioproject.modules.DataFlowControl;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    List<CustomMessage> messages;

    public MessageAdapter(List<CustomMessage> messages)
    {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = ((Activity)DataFlowControl.context).getLayoutInflater();

        CustomMessage temp = messages.get(position);
        view = temp.setView(layoutInflater, viewGroup);

        return view;
    }
}
