package com.example.chips.contentProvider;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.example.chips.modules.Contact.Contact;
import com.example.chips.utils.DataFlowControl;

import java.util.List;

public class ContactsContentProvider {
    /*
    *   Gives contacts information
    * */

    /*
     *   Content Provider variables
     * */
    private static ContentResolver contentResolver;
    private static Uri uri;
    private static Cursor cursor;

    /*
     *   getContacts:
     *      Sets all phones contact to list
     *
     *   Input:
     *       List<Contact> contacts
     *
     *   Output:
     *       None
     * */
    public static void getContacts(List<Contact> contacts){
        contentResolver = DataFlowControl.context.getContentResolver();
        uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        cursor = contentResolver.query(uri,null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()){
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                contacts.add(new Contact(contactName, contactNumber));
            }
        }
    }
}
