package ServerExchange.ServerRequests;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import ServerExchange.ImageConvert;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dima on 20.02.2018.
 */

public class CreateAdvertRequest extends TokenServerRequest<Boolean> {

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }

    private String CREATE_ADVERT_METHOD_NAME = "CreateAdvert";

    private int id_task_located;
    private String text;
    private String header;
    private int bounty;
    private LinkedList<String> photos;

    public CreateAdvertRequest(String serverAddress) {
        super(serverAddress);
    }
    public CreateAdvertRequest() {
        super();
    }

    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("id_task_located",this.id_task_located);
        params.put("text",text);
        params.put("header", header);
        params.put("bounty", this.bounty);
        params.put("photo", photos);

        return new ServerMethod(CREATE_ADVERT_METHOD_NAME, params);
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {

        return CreateAdvertJsonServerAnswer.class;
    }


    class CreateAdvertJsonServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    public void createAdvert(String header, String text, int bounty, int id_task_located,  ArrayList<Bitmap> photos, IServerAnswerHandler onCreateAdvertHandler)  {
        this.id_task_located = id_task_located;
        this.text = text;
        this.header = header;
        this.bounty = bounty;
        LinkedList<String> photoStrs = new LinkedList<>();
        for (Bitmap img : photos){
            photoStrs.addLast( ImageConvert.toBase64Str(img));
        }

        this.photos = photoStrs;
        startRequest(onCreateAdvertHandler);
    }
}
