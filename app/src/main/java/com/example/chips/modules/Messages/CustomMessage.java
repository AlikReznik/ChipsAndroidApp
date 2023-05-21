package com.example.chips.modules.Messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.Timestamp;
import java.util.Map;

public abstract class CustomMessage {
    /*
    *   Custom message class
    * */
    /*
 *      CustomMessage variables:
     * */
    protected String sender;
    protected Timestamp time;

    /*
     *   CustomMessage:
     *      Custom message constructor
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public CustomMessage(String sender, Timestamp time)
    {
        this.sender = sender;
        this.time = time;
    }


    /*
    *   Abstract functions
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
    public abstract View setView(LayoutInflater layoutInflater, ViewGroup parent);

    /*
     *   convertToMap:
     *      Converts the object to an object for firestore
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public abstract Map<String, Object> convertToMap();


    /*
     *   openDialog:
     *     Open message dialog
     *
     *   Input:
     *       None
     *   Output:
     *       None
     * */
    public abstract void openDialog();
}
