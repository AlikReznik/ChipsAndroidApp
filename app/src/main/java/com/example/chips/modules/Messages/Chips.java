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

public class Chips extends CustomMessage{
    /*
    *   Chips message class
    * */
    /*
     *   Chips variables:
     * */
    private String title, description;
    private Bitmap image;

    /*
     *   Chips:
     *      Chips constructor
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public Chips(String sender, Timestamp time, Bitmap image, String title, String description)
    {
        super(sender, time);
        this.title = title;
        this.description = description;
        this.image = image;
    }
    /*
     * Implementation of the abstract functions
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
        View view = layoutInflater.inflate(R.layout.listview_item_message_chips, parent, false);

        TextView TextView_Username = view.findViewById(R.id.TextView_Username);
        TextView TextView_ChipTitle = view.findViewById(R.id.TextView_ChipTitle);
        ImageView ImageView_ChipMessage = view.findViewById(R.id.ImageView_ChipMessage);

        TextView_Username.setText(this.sender);
        TextView_ChipTitle.setText(this.title);
        ImageView_ChipMessage.setImageBitmap(this.image);

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
        msg.put("Title", this.title);
        msg.put("Description", this.description);
        msg.put("Image", BitmapFunctions.bitmapToString(this.image));
        msg.put("Type", 2);

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
        dialog.setContentView(R.layout.dialog_chip_message);

        final TextView TextView_Username = dialog.findViewById(R.id.TextView_Username);
        final TextView TextView_Date = dialog.findViewById(R.id.TextView_Date);
        final ImageView ImageView_ChipMessage = dialog.findViewById(R.id.ImageView_ChipMessage);
        final TextView TextView_ChipTitle = dialog.findViewById(R.id.TextView_ChipTitle);
        final TextView TextView_ChipDescription = dialog.findViewById(R.id.TextView_ChipDescription);

        TextView_Username.setText("Sent By: " + this.sender);
        TextView_Date.setText("Sent At: " + this.time.toDate().toString());
        ImageView_ChipMessage.setImageBitmap(this.image);
        TextView_ChipTitle.setText(this.title);
        TextView_ChipDescription.setText(this.description);

        dialog.show();
    }
}
