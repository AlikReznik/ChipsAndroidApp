package com.example.chips_androidstudioproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chips_androidstudioproject.MainFragments.SearchFragment;
import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.example.chips_androidstudioproject.modules.User.BaseUser;
import com.example.chips_androidstudioproject.modules.Utils;

public class userProfile_activity extends AppCompatActivity {
    private ImageView imageView_userImage;
    private TextView txt_username;
    private Button btn_message;
    private Button btn_return;
    private TextView txt_bio;

    private BaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);

        initLayout();
        initInfo();
        initButtonListeners();
    }


    private void initInfo() {
        DataFlowControl.context = userProfile_activity.this;
        DataFlowControl.activity = userProfile_activity.this;

        int id = getIntent().getIntExtra("UserId", 0);
        user = SearchFragment.users.get(id);

        imageView_userImage.setImageBitmap(Utils.StringToBitMap(user.Image()));
        txt_username.setText(user.Username());
        txt_bio.setText(user.Bio());
    }

    private void initButtonListeners() {
        btn_message.setOnClickListener(view ->
        {
            /*Intent intent = new Intent(DataFlowControl.context, -......-.class);
            startActivity(intent);*/
        });
        btn_return.setOnClickListener(view ->
        {
            Intent intent = new Intent(DataFlowControl.context, MainActivity.class);
            startActivity(intent);
        });
    }

    private void initLayout(){
        imageView_userImage = findViewById(R.id.imageView_userImage);
        txt_username = findViewById(R.id.txt_username);
        btn_message = findViewById(R.id.btn_message);
        btn_return = findViewById(R.id.btn_return);
        txt_bio = findViewById(R.id.txt_bio);
    }
}