package com.example.chips.modules.Messages;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.chips.R;
import com.example.chips.utils.DataFlowControl;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class TextMessage extends CustomMessage{
    /*
    *   Text message class
    * */
    /*
     *   Text message variables:
     * */
    private String msg;

    /*
     *   TextMessage:
     *      Text message constructor
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public TextMessage(String sender, Timestamp time, String msg)
    {
        super(sender, time);
        this.msg = msg;
    }

    /*
    *    Implementation of the abstract function
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
        View view = layoutInflater.inflate(R.layout.listview_item_message_text, parent, false);

        TextView TextView_Username = view.findViewById(R.id.TextView_Username);
        TextView TextView_TextMessage = view.findViewById(R.id.TextView_TextMessage);

        TextView_Username.setText(this.sender + " ");
        TextView_TextMessage.setText(this.msg);

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
        msg.put("Msg", this.msg);
        msg.put("Type", 0);

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
        dialog.setContentView(R.layout.dialog_text_message);

        final TextView TextView_Username = dialog.findViewById(R.id.TextView_Username);
        final TextView TextView_Date = dialog.findViewById(R.id.TextView_Date);
        final TextView TextView_TextMessage = dialog.findViewById(R.id.TextView_TextMessage);

        TextView_Username.setText("Sent By: " + this.sender);
        TextView_Date.setText("Sent At: " + this.time.toDate().toString());
        TextView_TextMessage.setText(this.msg);

        dialog.show();
    }
}
