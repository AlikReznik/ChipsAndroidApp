package com.example.chips;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.chips.modules.Chats.Chat;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.modules.Messages.ImageMessage;
import com.example.chips.modules.Messages.MessageAdapter;
import com.example.chips.modules.Messages.MessageFirestore;
import com.example.chips.modules.Messages.TextMessage;
import com.google.firebase.Timestamp;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    /*
    *   Chat activity
    * */
    /*
     *   Layout variables
     * */
    private TextView TextView_ReturnButton;
    private ImageView ImageView_SendMessage;
    private TextView TextView_ChatTitle;
    private EditText EditText_TextMessage;
    private ListView ListView_Messages;

    /*
     *   Activity variables
     * */
    public static Chat chat;
    private static MessageAdapter messageAdapter;
    private int chatId = 0;

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    protected void onStart() {
        super.onStart();

        initLayout();
        initInfo();
        initButtonListeners();
    }

    protected void onStop() {
        super.onStop();

        ListView_Messages.removeAllViewsInLayout();
        chat.Messages().clear();

        MessageFirestore.listener.remove();
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   updateListViewMessagesAdapter:
     *      Updates chat messages listview
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public static void updateListViewMessagesAdapter() {
        messageAdapter.notifyDataSetChanged();
    }

    /*
     *   Runs when user return to chat settings window
     *
     *   Input:
     *       int requestCode, int resultCode, Intent data
     *
     *   Output:
     *       None
     * */
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Timestamp timestamp = new Timestamp(new Date());
            MessageFirestore.addMessage(new ImageMessage(DataFlowControl.authUser.Username(), timestamp, imageBitmap));
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(DataFlowControl.activity.getContentResolver(), selectedImage);

                Timestamp timestamp = new Timestamp(new Date());
                MessageFirestore.addMessage(new ImageMessage(DataFlowControl.authUser.Username(), timestamp, imageBitmap));
            } catch (Exception error) {
            }
        }
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

        TextView_ChatTitle.setOnClickListener(view ->
        {
            Intent intent = new Intent(DataFlowControl.context, ChatSettingsActivity.class);
            intent.putExtra("ChatId", chatId);
            startActivity(intent);
        });

        EditText_TextMessage.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0)
                    ImageView_SendMessage.setImageDrawable(ContextCompat.getDrawable(DataFlowControl.context, R.drawable.ic_send));
                else
                    ImageView_SendMessage.setImageDrawable(ContextCompat.getDrawable(DataFlowControl.context, R.drawable.ic_camera));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        ImageView_SendMessage.setOnClickListener(view ->
        {
            if (EditText_TextMessage.getText().toString().length() == 0) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);
            } else {
                Timestamp timestamp = new Timestamp(new Date());
                MessageFirestore.addMessage(new TextMessage(DataFlowControl.authUser.Username(), timestamp, EditText_TextMessage.getText().toString()));
                EditText_TextMessage.setText("");
            }
        });

        ListView_Messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                chat.getMessage(position).openDialog();
            }
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
        DataFlowControl.activity = this;
        DataFlowControl.context = this;

        chatId = getIntent().getIntExtra("ChatId", 0);
        chat = DataFlowControl.authUser.getChat(chatId);

        TextView_ChatTitle.setText(chat.Name());

        messageAdapter = new MessageAdapter(chat.Messages());
        ListView_Messages.setAdapter(messageAdapter);

        ListView_Messages.smoothScrollToPosition(0);

        MessageFirestore.dataListener();
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
    private void initLayout() {
        TextView_ReturnButton = findViewById(R.id.TextView_ReturnButton);
        ListView_Messages = findViewById(R.id.ListView_Messages);
        TextView_ChatTitle = findViewById(R.id.TextView_ChatTitle);
        EditText_TextMessage = findViewById(R.id.EditText_TextMessage);
        ImageView_SendMessage = findViewById(R.id.ImageView_SendMessage);
    }

}