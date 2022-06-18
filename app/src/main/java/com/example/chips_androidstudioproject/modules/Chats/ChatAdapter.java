package com.example.chips_androidstudioproject.modules.Chats;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.chips_androidstudioproject.modules.DataFlowControl;

public class ChatAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        if(DataFlowControl.authUser != null)
            return DataFlowControl.authUser.Chats().size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return DataFlowControl.authUser.Chats().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = ((Activity)DataFlowControl.context).getLayoutInflater();

        Chat temp = DataFlowControl.authUser.Chats().get(position);
        view = temp.setView(layoutInflater, viewGroup);

        return view;
    }
}
