package com.example.chips.mainFragments;

import static android.app.Activity.RESULT_OK;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.chips.R;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.modules.User.UserAuth;
import com.example.chips.modules.User.UserFirestore;
import com.example.chips.utils.BitmapFunctions;
import com.example.chips.LoginActivity;
import com.example.chips.RecommendActivity;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment{
    /*
    *   Allows user to update information and change settings
    * */
    /*
     *   Layout variables
     * */
    private ImageView ImageView_UserImage;
    private TextView TextView_Username;
    private TextView TextView_UserBio;
    private LinearLayout LinearLayout_UserInformation;
    private TextView TextView_Recommend;
    private TextView TextView_Help;
    private TextView TextView_PermissionSettings;
    private TextView TextView_ResetEmail;
    private TextView TextView_ResetPassword;
    private TextView TextView_LogOut;

    /*
     *   Fragment variables
     * */
    private Bitmap userImage = null;
    private UserAuth userAuth;

    private final String DOCS = "https://docs.google.com/document/d/1fYknFZObvec2kFgaV1WM0QFY-v2uJi4WD7PwfbTg2gk/edit?usp=sharing";

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onStart() {
        super.onStart();

        initLayout();
        initInfo();
        initButtonListeners();
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   updateUserInformationDialog:
     *      Updates user information
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void updateUserInformationDialog() {
        /*
        *   Creates dialog
        * */
        final Dialog dialog = new Dialog(DataFlowControl.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_user_information_update);

        /*
        *   Layout variables
        * */
        final Button Button_Cancel = dialog.findViewById(R.id.Button_Cancel);
        final Button Button_Update = dialog.findViewById(R.id.Button_Update);
        final TextView EditText_Username = dialog.findViewById(R.id.EditText_Username);
        final TextView EditText_Bio = dialog.findViewById(R.id.EditText_Bio);

        /*
        *   Sets user information
        * */
        EditText_Username.setText(DataFlowControl.authUser.Username());
        EditText_Bio.setText(DataFlowControl.authUser.Bio());

        /*
        *   Button listeners
        * */
        Button_Cancel.setOnClickListener((v) ->{
            dialog.dismiss();
        });
        Button_Update.setOnClickListener((v) ->{
            if(!EditText_Username.getText().toString().equals("") || !(DataFlowControl.authUser.Bio().equals(""))){
                Map<String, Object> userSet = new HashMap<>();
                userSet.put("Username", EditText_Username.getText().toString());
                userSet.put("Bio", EditText_Bio.getText().toString());

                DataFlowControl.authUser.update(DataFlowControl.authUser.Uid(), userSet);
                UserFirestore.updateUser();

                TextView_Username.setText(userSet.get("Username").toString());
                TextView_UserBio.setText("Bio: \n" + userSet.get("Bio").toString());

                Toast.makeText(DataFlowControl.context, "User was updated successfully", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    /*
     *   onActivityResult:
     *      Runs when user return to chat settings window
     *
     *   Input:
     *       int requestCode, int resultCode, Intent data
     *
     *   Output:
     *       None
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            userImage = (Bitmap) extras.get("data");
            ImageView_UserImage.setImageBitmap(userImage);

            updateImage();
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            try{
                Uri selectedImage = data.getData();
                userImage = MediaStore.Images.Media.getBitmap(DataFlowControl.activity.getContentResolver(), selectedImage);
                ImageView_UserImage.setImageBitmap(userImage);

                updateImage();
            }catch(Exception error){}
        }
    }

    private void updateImage() {
        Map<String, Object> userSet = new HashMap<>();
        userSet.put("Image", BitmapFunctions.bitmapToString(userImage));

        DataFlowControl.authUser.update(DataFlowControl.authUser.Uid(), userSet);
        UserFirestore.updateUser();

        Toast.makeText(DataFlowControl.context, "Image was updated successfully", Toast.LENGTH_SHORT).show();
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
        ImageView_UserImage.setOnClickListener(view ->
        {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(cameraIntent, 0);
            } catch (ActivityNotFoundException error) {}
        });
        LinearLayout_UserInformation.setOnClickListener(view ->
        {
            updateUserInformationDialog();
        });
        TextView_Recommend.setOnClickListener(view ->
        {
            Intent intent = new Intent(DataFlowControl.context, RecommendActivity.class);
            startActivity(intent);
        });
        TextView_Help.setOnClickListener(view ->
        {
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(DOCS));
            startActivity(intent);
        });

        TextView_ResetEmail.setOnClickListener(view ->
        {
            EditText taskEditText = new EditText(DataFlowControl.context);
            taskEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            AlertDialog dialog = new AlertDialog.Builder(DataFlowControl.context)
                    .setTitle("Reset Email")
                    .setView(taskEditText)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String email = String.valueOf(taskEditText.getText());
                            userAuth.resetEmail(email);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        });

        TextView_ResetPassword.setOnClickListener(view ->
        {
            AlertDialog dialog = new AlertDialog.Builder(DataFlowControl.context)
                    .setTitle("Are you sure you want to reset your password?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userAuth.resetPassword("-");
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        });

        TextView_PermissionSettings.setOnClickListener(view -> {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        });

        TextView_LogOut.setOnClickListener(view ->
        {
            AlertDialog dialog = new AlertDialog.Builder(DataFlowControl.context)
                    .setTitle("Are you sure you want to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userAuth.signout();
                            Intent intent = new Intent(DataFlowControl.context, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
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
        userAuth = DataFlowControl.userAuth;

        if(DataFlowControl.authUser != null && TextView_Username != null){
            TextView_Username.setText(DataFlowControl.authUser.Username());
            if(!DataFlowControl.authUser.Bio().equals("")){
                TextView_UserBio.setText("Bio: \n" + DataFlowControl.authUser.Bio());
            }
            if(!DataFlowControl.authUser.Image().equals("")){
                ImageView_UserImage.setImageBitmap(BitmapFunctions.stringToBitMap(DataFlowControl.authUser.Image()));
            }
        }
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
        ImageView_UserImage = getActivity().findViewById(R.id.ImageView_UserImage);
        TextView_Username = getActivity().findViewById(R.id.TextView_Username);
        TextView_UserBio = getActivity().findViewById(R.id.TextView_UserBio);
        LinearLayout_UserInformation = getActivity().findViewById(R.id.LinearLayout_UserInformation);
        TextView_Recommend = getActivity().findViewById(R.id.TextView_Recommend);
        TextView_Help = getActivity().findViewById(R.id.TextView_Help);
        TextView_PermissionSettings = getActivity().findViewById(R.id.TextView_PermissionSettings);
        TextView_ResetEmail = getActivity().findViewById(R.id.TextView_ResetEmail);
        TextView_ResetPassword = getActivity().findViewById(R.id.TextView_ResetPassword);
        TextView_LogOut = getActivity().findViewById(R.id.TextView_LogOut);
    }
}