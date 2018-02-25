package ServerExchange;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Dryush on 18.02.2018.
 */

public class ImageConvert {

    public static Bitmap fromBase64Str(String base64Str){

        if (base64Str == null)
            return null; //TODO : костыль
        Bitmap bitmap = null;
        try {
            base64Str = base64Str.replace(' ', '+');
            //byte[] rawBitmap = Base64.decode(base64Str.getBytes("UTF-8"), Base64.URL_SAFE);
            byte[] rawBitmap = Base64.decode(base64Str.getBytes(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(rawBitmap, 0, rawBitmap.length);
        } catch (Exception e) {
            e.printStackTrace();//Не должно быть такого
        }
        return bitmap;
    }

    public static String toBase64Str(Bitmap img){
        if (img == null)
            return null; //TODO : костыль
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String base64Str = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64Str;
    }




}
