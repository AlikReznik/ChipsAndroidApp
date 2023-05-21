package com.example.chips.modules.Messages;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chips.R;
import com.example.chips.utils.DataFlowControl;
import com.example.chips.utils.BitmapFunctions;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class ImageMessage extends CustomMessage{
    /*
    *   Image message class
    * */
    /*
     *   Image message variables:
     * */
    private Bitmap msg;

    /*
     *   ImageMessage:
     *      Image message constructor
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public ImageMessage(String sender, Timestamp time, Bitmap msg)
    {
        super(sender, time);
        this.msg = msg;
    }

    /*
     *  Implementation of the abstract functions
     * */

    /*
     *   setView:
     *      Listview item setter
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    @Override
    public View setView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview_item_message_image, parent, false);

        TextView TextView_Username = view.findViewById(R.id.TextView_Username);
        ImageView ImageView_ImageMessage = view.findViewById(R.id.ImageView_ImageMessage);

        TextView_Username.setText(this.sender + " ");
        ImageView_ImageMessage.setImageBitmap(this.msg);

        return view;
    }

    /*
     *   convertToMap:
     *      Converts the object to an object for firestore
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    @Override
    public Map<String, Object> convertToMap() {
        Map<String, Object> msg = new HashMap<>();
        msg.put("Sender", this.sender);
        msg.put("Time", this.time);
        msg.put("Msg", BitmapFunctions.bitmapToString(this.msg));
        msg.put("Type", 1);

        return msg;
    }

    /*
     *   openDialog:
     *     Open message dialog
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    @Override
    public void openDialog() {
        final Dialog dialog = new Dialog(DataFlowControl.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_image_message);

        final TextView TextView_Username = dialog.findViewById(R.id.TextView_Username);
        final TextView TextView_Date = dialog.findViewById(R.id.TextView_Date);
        final ImageView ImageView_ImageMessage = dialog.findViewById(R.id.ImageView_ImageMessage);

        TextView_Username.setText("Sent By: " + this.sender);
        TextView_Date.setText("Sent At: " + this.time.toDate().toString());
        ImageView_ImageMessage.setImageBitmap(this.msg);

        dialog.show();
    }
}
