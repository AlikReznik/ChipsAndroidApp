package com.example.chips_androidstudioproject.modules;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    /*
    *   Methods that are used over the whole app
    * */
    public static Bitmap StringToBitMap(String image){
        try{
            byte[] encodeByte = Base64.decode(image,Base64.DEFAULT);
            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            return BitmapFactory.decodeStream(inputStream);
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public static String BitmapToString(Bitmap bitmap){
        if(bitmap != null){
            ByteArrayOutputStream bas = new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, bas);
            byte[] b = bas.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }
        return "";
    }
}
