package com.example.chips.modules.Messages;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.chips.utils.DataFlowControl;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    /*
    *   Message adapter class
    * */
    /*
     *   Message adapter variables
     * */
    private List<CustomMessage> messages;

    /*
     *   ContactsAdapter:
     *      Message adapter constructor
     *
     *   Input:
     *       List<CustomMessage> messages
     *   Output:
     *       None
     * */
    public MessageAdapter(List<CustomMessage> messages)
    {
        this.messages = messages;
    }

    /*
     *   getCount:
     *      Returns the amount of messages
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    @Override
    public int getCount() {
        return messages.size();
    }

    /*
     *   getItem:
     *      Returns message by position
     *
     *   Input:
     *       int position
     *   Output:
     *       None
     * */
    @Override
    public Object getItem(int position) {
        return messages.get(position);
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
     *      Returns the view item of the message to the listview
     *
     *   Input:
     *       int position, View view, ViewGroup viewGroup
     *   Output:
     *       View
     * */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = ((Activity)DataFlowControl.context).getLayoutInflater();

        CustomMessage temp = messages.get(position);
        view = temp.setView(layoutInflater, viewGroup);

        return view;
    }
}
