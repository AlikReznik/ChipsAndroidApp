package com.example.chips.modules.Contact;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.chips.utils.DataFlowControl;

import java.util.List;

public class ContactsAdapter extends BaseAdapter {
    /*
    *   Contacts adapter class
    * */
    /*
    *   Contact adapter variables
    * */
    private List<Contact> contacts;

    /*
     *   ContactsAdapter:
     *      Contacts adapter constructor
     *
     *   Input:
     *       List<Contact> contacts
     *   Output:
     *       None
     * */
    public ContactsAdapter(List<Contact> contacts)
    {
        this.contacts = contacts;
    }

    /*
     *   getCount:
     *      Returns the amount of contacts
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    @Override
    public int getCount() {
        if(contacts != null)
            return contacts.size();
        return 0;
    }

    /*
     *   getItem:
     *      Returns contact by position
     *
     *   Input:
     *       int position
     *   Output:
     *       None
     * */
    @Override
    public Object getItem(int position) {
        return contacts.get(position);
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
     *      Returns the view item of the contact to the listview
     *
     *   Input:
     *       int position, View view, ViewGroup viewGroup
     *   Output:
     *       View
     * */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = ((Activity)DataFlowControl.context).getLayoutInflater();

        Contact temp = contacts.get(position);
        view = temp.setView(layoutInflater, viewGroup);

        return view;
    }
}
