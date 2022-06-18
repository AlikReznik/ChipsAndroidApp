package com.example.chips_androidstudioproject.MainFragments;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.chips_androidstudioproject.R;
import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.example.chips_androidstudioproject.modules.User.UserAuth;
import com.example.chips_androidstudioproject.modules.User.UserFirestore;
import com.example.chips_androidstudioproject.modules.Utils;
import com.example.chips_androidstudioproject.login_activity;


import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment{
    private ImageView imageView_userImage;
    private EditText txt_username;
    private Button btn_save;
    private Button btn_logout;
    private EditText txt_bio;

    private Bitmap userImage = null;
    private UserAuth userAuth;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userAuth = DataFlowControl.userAuth;
        
        initLayout();
        initInfo();
        initButtonListeners();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            userImage = (Bitmap) extras.get("data");

            imageView_userImage.setImageBitmap(userImage);
        }
    }

    private void initInfo() {
        if(DataFlowControl.authUser != null){
            txt_username.setText(DataFlowControl.authUser.Username());
            txt_bio.setText(DataFlowControl.authUser.Bio());
            if(!DataFlowControl.authUser.Image().equals("")){
                imageView_userImage.setImageBitmap(Utils.StringToBitMap(DataFlowControl.authUser.Image()));
            }
        }
    }

    private void initButtonListeners() {
        imageView_userImage.setOnClickListener(view ->
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, 0);
            } catch (ActivityNotFoundException error) {
                System.out.println("Error: " + error);
            }

        });

        btn_save.setOnClickListener(view ->
        {
            if(!txt_username.getText().toString().equals(DataFlowControl.authUser.Username()) || DataFlowControl.authUser.Image() != Utils.BitmapToString(userImage) || !(DataFlowControl.authUser.Bio().equals(""))){
                Map<String, Object> userSet = new HashMap<>();
                userSet.put("Username", txt_username.getText().toString());
                userSet.put("Image", Utils.BitmapToString(userImage));
                userSet.put("Bio", txt_bio.getText().toString());

                DataFlowControl.authUser.update(DataFlowControl.authUser.Uid(), userSet);
                UserFirestore.updateUser();

                Toast.makeText(DataFlowControl.context, "User was updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

        btn_logout.setOnClickListener(view ->
        {
            userAuth.signout();
            Intent intent = new Intent(DataFlowControl.context, login_activity.class);
            startActivity(intent);
        });

    }

    private void initLayout(){
        imageView_userImage = getActivity().findViewById(R.id.imageView_userImage);
        txt_username = getActivity().findViewById(R.id.txt_username);
        btn_save = getActivity().findViewById(R.id.btn_save);
        btn_logout = getActivity().findViewById(R.id.btn_logout);
        txt_bio = getActivity().findViewById(R.id.txt_bio);
    }
}