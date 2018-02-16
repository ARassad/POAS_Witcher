package ServerExchange.ServerRequests;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * Created by Дима on 16.02.2018.
 */

public class UpdateProfileRequest extends TokenServerRequest {

    private String UPDATE_PROFILE_METHOD_NAME = "EditProfile";

    private String name;
    private String text;
    private Base64 photo;
    private String password;

    public UpdateProfileRequest(String address) {super(address);}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("about", text);
        params.put("name", name);
        //params.put("photo", photo);
        params.put("password", password);

        return new ServerMethod(UPDATE_PROFILE_METHOD_NAME, params);
    }

    class JsonUpdateProfileServerAnswer extends JsonServerAnswer{

        String about;
        long id;
        String name;
        Base64 photo;



        @Override
        public Boolean convert() {
            return status.equals("OK");
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {


        return JsonServerAnswer.class;
    }

    public void updateProfile(String name, String text, Base64 photo, String password, IServerAnswerHandler onUpdateProfileHandler) throws IOException {
        this.name = name;
        this.text = text;
        this.photo = photo;
        this.password = password;
        startRequest(onUpdateProfileHandler);
    }
}
