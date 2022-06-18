package com.example.chips_androidstudioproject;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chips_androidstudioproject.MainFragments.ChatsFragment;
import com.example.chips_androidstudioproject.modules.Chats.Chat;
import com.example.chips_androidstudioproject.modules.Chats.ChatFirestore;
import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.example.chips_androidstudioproject.modules.Messages.ImageMessage;
import com.example.chips_androidstudioproject.modules.Messages.MessageAdapter;
import com.example.chips_androidstudioproject.modules.Messages.MessageFirestore;
import com.example.chips_androidstudioproject.modules.Messages.TextMessage;
import com.google.firebase.Timestamp;

import java.util.Date;

public class chat_activity extends AppCompatActivity {
    private Button btn_return;
    private ListView lv_messages;
    private TextView txt_name;
    private TextView txt_users;
    private LinearLayout layout_settings;

    private EditText txt_textMessage;
    private Button btn_inputMessage;

    public static Chat chat;
    private static MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_layout);

        initLayout();
    }

    protected void onStart() {
        super.onStart();

        initInfo();
        initButtonListeners();
    }
    protected void onStop() {
        super.onStop();

        lv_messages.removeAllViewsInLayout();
        chat.Messages().clear();

        MessageFirestore.listener.remove();
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Timestamp timestamp = new Timestamp(new Date());

            MessageFirestore.addMessage(new ImageMessage(DataFlowControl.authUser.Username(), timestamp, imageBitmap));
        }
    }

    private void initButtonListeners() {
        btn_return.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        layout_settings.setOnClickListener(view ->
        {
            /* Chat Settings */
        });

        txt_textMessage.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.toString().length() != 0)
                    btn_inputMessage.setText("➤");
                else
                    btn_inputMessage.setText("\uD83D\uDCF7");
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        btn_inputMessage.setOnClickListener(view ->
        {
            if(txt_textMessage.getText().toString().length() == 0){
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 0);
                } catch (ActivityNotFoundException e) {
                    Log.d("Chat - ", "Error: " + e);
                }
            }else{
                Timestamp timestamp = new Timestamp(new Date());
                MessageFirestore.addMessage(new TextMessage(DataFlowControl.authUser.Username(), timestamp, txt_textMessage.getText().toString()));
                txt_textMessage.setText("");
            }
        });

        MessageFirestore.DataListener();
    }

    public static void updateListViewMessagesAdapter() {
        messageAdapter.notifyDataSetChanged();
    }
    private void initInfo() {
        int id = getIntent().getIntExtra("ChatId", 0);
        chat = DataFlowControl.authUser.getChat(id);
        // ChatFirestore.getUsers(chat.Uid());
        Log.d("Chat - ", chat.Users().toString());

        txt_name.setText(chat.Name());

        messageAdapter = new MessageAdapter(chat.Messages());
        lv_messages.setAdapter(messageAdapter);
    }

    private void initLayout() {
        btn_return = findViewById(R.id.btn_return);
        lv_messages = findViewById(R.id.lv_messages);
        txt_name = findViewById(R.id.txt_name);
        txt_users = findViewById(R.id.txt_bio);
        layout_settings = findViewById(R.id.layout_settings);
        txt_textMessage = findViewById(R.id.txt_textMessage);
        btn_inputMessage = findViewById(R.id.btn_inputMessage);
    }
}