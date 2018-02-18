package ServerExchange;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Dryush on 18.02.2018.
 */

public class ImageConvert {

    public static Bitmap fromBase64Str(String base64Str){

        byte[] rawBitmap = Base64.decode(base64Str.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(rawBitmap, 0, rawBitmap.length);
        return bitmap;
    }

    public static String toBase64Str(Bitmap img){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String base64Str = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64Str;
    }




}
