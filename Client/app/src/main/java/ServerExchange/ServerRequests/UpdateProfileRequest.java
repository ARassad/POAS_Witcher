package ServerExchange.ServerRequests;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import ServerExchange.Password;

/**
 * Created by Дима on 16.02.2018.
 */

public class UpdateProfileRequest extends TokenServerRequest {

    private String UPDATE_PROFILE_METHOD_NAME = "EditProfile";

    private String name = null;
    private String text = null;
    private String photo = null;
    private String password = null;

    public UpdateProfileRequest(String address) {super(address);}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        if (text != null && !"".equals(text)) {
            params.put("about", text);
            text = null;
        }

        if (name != null && !"".equals(name)) {
            params.put("name", name);
            name = null;
        }

        if (photo != null && !"".equals(photo)) {
            params.put("photo", photo);
            photo = null;
        }

        if (password != null && !"".equals(password)) {
            params.put("password", password);
            password = null;
        }

        return new ServerMethod(UPDATE_PROFILE_METHOD_NAME, params);
    }

    class JsonUpdateProfileServerAnswer extends JsonServerAnswer{
        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonUpdateProfileServerAnswer.class;
    }

    public void updateProfile(String name, String text, Bitmap photo, IServerAnswerHandler onUpdateProfileHandler) throws IOException {
        this.name = name;
        this.text = text;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        this.photo  = Base64.encodeToString(byteArray, Base64.DEFAULT);

        startRequest(onUpdateProfileHandler);
    }

    public void updatePassword(String password ,IServerAnswerHandler onUpdateProfileHandler){
        this.password = Password.encode(password);
        startRequest(onUpdateProfileHandler);
    }

}
