package ServerExchange.ServerRequests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;


/**
 * Created by Dima on 18.02.2018.
 */

public class UpdateAdvertRequest extends TokenServerRequest<Boolean> {

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }
	
	private String UPDATE_ADVERT_METHOD_NAME = "EditAdvert";

    private int id;
    private int id_witcher;
    private int status;
    private int id_task_located;
    private String text;
    private int bounty;
    private String photo_del;
    private String photo_new;

    public UpdateAdvertRequest(String address) {super(address);}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("id",String.valueOf(this.id));
        params.put("id_witcher",String.valueOf(this.id_witcher));
        params.put("status",String.valueOf(this.status));
        params.put("id_task_located",String.valueOf(this.id_task_located));
        params.put("text", text);
        params.put("bounty",String.valueOf(this.bounty));

        return new ServerMethod(UPDATE_ADVERT_METHOD_NAME, params);
    }

    class JsonUpdateAdvertServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return status.equals("OK");
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonUpdateAdvertServerAnswer.class;
    }

    public void updateAdvert( int id, int id_witcher, int status, int id_task_located, String text,
                              int bounty, IServerAnswerHandler onUpdateAdvertHandler) throws IOException {
        this.id = id;
        this.id_witcher = id_witcher;
        this.status = status;
        this.id_task_located = id_task_located;
        this.text = text;
        this.bounty = bounty;
        startRequest(onUpdateAdvertHandler);
    }
}
