package com.example.chips;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chips.contentProvider.ContactsContentProvider;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.modules.Contact.Contact;
import com.example.chips.modules.Contact.ContactsAdapter;
import java.util.ArrayList;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {
    /*
    *   Recommend activity
    * */
    /*
     *   Recommend activity variables
     * */
    private List<Contact> contacts = new ArrayList<Contact>();
    private final String MESSAGE = ", Join me and start using Chips)";
    private SmsManager smsManager;

    /*
     *   Layout variables
     * */
    private TextView TextView_ReturnButton;

    private TextView TextView_Help;
    private ListView ListView_Contacts;
    private ContactsAdapter contactsAdapter;

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);}

    protected void onStart() {
        super.onStart();

        initLayout();
        initInfo();
        initButtonListeners();
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   sentMessage:
     *      Sends the chosen contact a message with recommendation to install the app
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void sentMessage(Contact contact){
        String text = "Hi "+contact.Name()+MESSAGE;
        smsManager.sendTextMessage(contact.Phone_number(), null, text, null, null);
        Toast.makeText(DataFlowControl.context, "Message was sent", Toast.LENGTH_SHORT).show();
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   initButtonListeners:
     *      Button listeners
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void initButtonListeners() {
        TextView_ReturnButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(DataFlowControl.context, MainActivity.class);
            startActivity(intent);
        });
        ListView_Contacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView <?> adapter, View view, int position, long arg) {
                sentMessage(contacts.get(position));
                return true;
            }
        });
        TextView_Help.setOnClickListener(view ->
        {
            Toast.makeText(DataFlowControl.context, "Long click on the contacts to recommend the app!", Toast.LENGTH_SHORT).show();
        });
    }

    /*
     *   initInfo:
     *      Sets layout information
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void initInfo() {
        DataFlowControl.context = this;
        DataFlowControl.activity = this;

        smsManager = SmsManager.getDefault();

        ContactsContentProvider.getContacts(contacts);

        contactsAdapter = new ContactsAdapter(contacts);
        ListView_Contacts.setAdapter(contactsAdapter);
    }

    /*
     *   initLayout:
     *      Sets layout variables
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void initLayout(){
        TextView_ReturnButton = findViewById(R.id.TextView_ReturnButton);
        TextView_Help = findViewById(R.id.TextView_Help);
        ListView_Contacts = findViewById(R.id.ListView_Contacts);
    }

}