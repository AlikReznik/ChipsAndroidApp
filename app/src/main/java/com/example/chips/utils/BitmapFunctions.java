package com.example.chips.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class BitmapFunctions {
    /*
    *
    *   Helps to translate bitmap to string and the other way around
    *
    * */


    /*
     *   StringToBitMap:
     *      Converts string to bitmap
     *
     *   Input:
     *       String image
     *
     *   Output:
     *       Bitmap
     * */
    public static Bitmap stringToBitMap(String image){
        try{
            byte[] encodeByte = Base64.decode(image,Base64.DEFAULT);
            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            return BitmapFactory.decodeStream(inputStream);
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    /*
     *   BitmapToString:
     *      Converts bitmap to string
     *
     *   Input:
     *       Bitmap bitmap
     *
     *   Output:
     *       String
     * */
    public static String bitmapToString(Bitmap bitmap){
        if(bitmap != null){
            ByteArrayOutputStream bas = new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, bas);
            byte[] b = bas.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }
        return "";
    }
}
