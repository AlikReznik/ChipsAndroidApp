package com.example.chips_androidstudioproject.modules.User;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.chips_androidstudioproject.modules.DataFlowControl;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    List<BaseUser> users;

    public UserAdapter(List<BaseUser> users)
    {
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = ((Activity) DataFlowControl.context).getLayoutInflater();

        BaseUser temp = users.get(position);
        view = temp.setView(layoutInflater, viewGroup);

        return view;
    }
}
