package ServerExchange.ServerRequests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;


/**
 * Created by Дима on 18.02.2018.
 */

public class UpdateAdvertRequest extends TokenServerRequest {

    private String UPDATE_ADVERT_METHOD_NAME = "EditAdvert";

    private String name;
    private String text;
    private String id;
    private String id_witcher;
    private String status;
    private String id_task_located;
    private String bounty;
    private String photo_del;

    public UpdateAdvertRequest(String address) {super(address);}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("about", text);
        params.put("name", name);
        //params.put("photo", photo);

        return new ServerMethod(UPDATE_ADVERT_METHOD_NAME, params);
    }

    class JsonUpdateAdvertServerAnswer extends JsonServerAnswer{

        String about;
        long id;
        String name;
        ArrayList<Base64> photo = new ArrayList();

        @Override
        public Boolean convert() {
            return status.equals("OK");
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonUpdateAdvertServerAnswer.class;
    }

    public void updateAdvert(String name, String text, IServerAnswerHandler onUpdateAdvertHandler) throws IOException {
        this.name = name;
        this.text = text;
        startRequest(onUpdateAdvertHandler);
    }
}
