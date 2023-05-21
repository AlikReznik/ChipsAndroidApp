package com.example.chips.modules.User;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.chips.utils.DataFlowControl;
import java.util.List;

public class UserAdapter extends BaseAdapter {
    /*
     *   User adapter class
     * */
    /*
     *   User adapter variables
     * */
    private List<BaseUser> users;

    /*
     *   UserAdapter:
     *      User adapter constructor
     *
     *   Input:
     *       List<BaseUser> users
     *   Output:
     *       None
     * */
    public UserAdapter(List<BaseUser> users)
    {
        this.users = users;
    }

    /*
     *   getCount:
     *      Returns the amount of users
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    @Override
    public int getCount() {
        return users.size();
    }

    /*
     *   getItem:
     *      Returns user by position
     *
     *   Input:
     *       int position
     *   Output:
     *       None
     * */
    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    /*
     *   getItemId:
     *      Returns 0
     *
     *   Input:
     *       int position
     *   Output:
     *       None
     * */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
     *   getView:
     *      Returns the view item of the user to the listview
     *
     *   Input:
     *       int position, View view, ViewGroup viewGroup
     *   Output:
     *       View
     * */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = ((Activity) DataFlowControl.context).getLayoutInflater();

        BaseUser temp = users.get(position);
        view = temp.setView(layoutInflater, viewGroup);

        return view;
    }
}
