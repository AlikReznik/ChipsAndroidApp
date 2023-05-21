package com.example.chips.modules.Chats;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.chips.utils.DataFlowControl;

public class ChatAdapter extends BaseAdapter {
    /*
    *   Chat adapter class
    * */
    /*
     *   getCount:
     *      Returns the amount of chats
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    @Override
    public int getCount() {
        if(DataFlowControl.authUser != null)
            return DataFlowControl.authUser.Chats().size();
        return 0;
    }

    /*
     *   getItem:
     *      Returns chat by position in user's chat list
     *
     *   Input:
     *       int position
     *   Output:
     *       None
     * */
    @Override
    public Object getItem(int position) {
        return DataFlowControl.authUser.Chats().get(position);
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
     *      Returns the view item of the chat to the listview
     *
     *   Input:
     *       int position, View view, ViewGroup viewGroup
     *   Output:
     *       View
     * */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = ((Activity)DataFlowControl.context).getLayoutInflater();

        Chat temp = DataFlowControl.authUser.Chats().get(position);
        view = temp.setView(layoutInflater, viewGroup);

        return view;
    }
}
