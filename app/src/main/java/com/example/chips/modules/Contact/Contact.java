package com.example.chips.modules.Contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.chips.R;

public class Contact {
    /*
    *   Phone contact class
    * */

    /*
     *   Contact variables:
     * */
    private String phone_number;
    private String name;

    /*
     *   Contact:
     *      Contact constructor
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public Contact(String name, String phone_number) {
        this.phone_number = phone_number;
        this.name = name;
    }

    /*
     *   setView:
     *      Sets contact information to contacts listview layout
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public View setView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview_item_contact, parent, false);

        TextView TextView_ContactName = view.findViewById(R.id.TextView_ContactName);
        TextView TextView_ContactPhoneNumber = view.findViewById(R.id.TextView_ContactPhoneNumber);

        TextView_ContactName.setText(this.name);
        TextView_ContactPhoneNumber.setText(this.phone_number);

        return view;
    }

    /*
     *   Phone_number:
     *      Returns phone_number
     *
     *   Input:
     *       None
     *   Output:
     *       String
     * */
    public String Phone_number() {
        return phone_number;
    }

    /*
     *   Name:
     *      Returns name
     *
     *   Input:
     *       None
     *   Output:
     *       String
     * */
    public String Name() {
        return name;
    }
}
