package ServerExchange.ServerRequests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.HashMap;

/**
 * Created by Dryush on 17.02.2018.
 */

public class GetImageRequest extends TokenServerRequest<Bitmap> {


    String METHOD_NAME = "get_image";
    String id = "";


    @Override
    protected ServerMethod getMethod() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return new ServerMethod(METHOD_NAME,params);
    }

    class JsonImageAnswer extends JsonServerAnswer{

        String image;
        @Override
        public Bitmap convert() {
            byte[] rawBitmap = Base64.decode(image.getBytes(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(rawBitmap, 0, rawBitmap.length);
            return bitmap;
        }
    }


    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonImageAnswer.class;
    }
}
