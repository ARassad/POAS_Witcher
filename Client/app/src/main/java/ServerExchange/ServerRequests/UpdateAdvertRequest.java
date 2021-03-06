package ServerExchange.ServerRequests;

import android.content.Intent;
import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.LinkedList;

import ServerExchange.ImageConvert;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;


/**
 * Created by Dima on 18.02.2018.
 */

public class UpdateAdvertRequest extends TokenServerRequest<Boolean> {

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }
	
	private String UPDATE_ADVERT_METHOD_NAME = "EditAdvert";

    private Long id;
    //private int id_witcher;
    //private int status;
    private Long id_task_located;
    private String header;
    private String text;
    private Integer bounty;
    private LinkedList<String> photo_del;
    private LinkedList<String> photo_new;

    public UpdateAdvertRequest(String address) {super(address);}
    public UpdateAdvertRequest() {super();}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("id",this.id);
        this.id = null;
        //params.put("id_witcher",this.id_witcher);
        //params.put("status",this.status);
        if (id_task_located!= null) {
            params.put("id_task_located", this.id_task_located);
            this.id_task_located = null;
        }
        if (text != null) {
            params.put("text", text);
            this.text = null;
        }
        if (header != null){
            params.put("header", header);
        }
        if (bounty != null) {
            params.put("bounty", this.bounty);
            this.bounty = null;
        }

        if ( photo_del!= null) {
            params.put("photo_del", this.photo_del);
            this.photo_del = null;
        }

        if (photo_new != null) {
            params.put("photo_new", this.photo_new);
            this.photo_new = null;
        }

        return new ServerMethod(UPDATE_ADVERT_METHOD_NAME, params);
    }

    class JsonUpdateAdvertServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonUpdateAdvertServerAnswer.class;
    }

    public void updateAdvert(long id, Long id_task_located, String text, String header,
                             Integer bounty, IServerAnswerHandler onUpdateAdvertHandler){
        this.id = id;
        //this.status = status;
        this.header = header;
        this.id_task_located = id_task_located;
        this.text = text;
        this.bounty = bounty;
        startRequest(onUpdateAdvertHandler);
    }

    public void updateAdvert(long id, String header, String text, Long id_task_located,
                             Integer bounty, LinkedList<Bitmap> imgsToAdd, LinkedList<Bitmap> imgsToRemove, IServerAnswerHandler onUpdateAdvertHandler){
        this.id = id;
        //this.status = status;
        this.header = header;
        this.id_task_located = id_task_located;
        this.text = text;
        this.bounty = bounty;

        LinkedList <String> _imgsToDel = new LinkedList<>();
        if (imgsToRemove != null) {
            for (Bitmap img : imgsToRemove) {
                _imgsToDel.addLast(ImageConvert.toBase64Str(img));
            }
        }
        this.photo_del = _imgsToDel;

        LinkedList <String> _imgsToAdd = new LinkedList<>();
        if (imgsToAdd != null) {
            for (Bitmap img : imgsToAdd) {
                _imgsToAdd.addLast(ImageConvert.toBase64Str(img));
            }
        }
        this.photo_new = _imgsToAdd;

        startRequest(onUpdateAdvertHandler);
    }

    public void addPhotos(long id, LinkedList<Bitmap> imgsToAdd){
        this.id = id;
        LinkedList <String> _imgsToAdd = new LinkedList<>();
        for (Bitmap img : imgsToAdd){
            _imgsToAdd.addLast(ImageConvert.toBase64Str(img));
        }
        this.photo_new = _imgsToAdd;
    }

    public void removePhotos(long id, LinkedList<Bitmap> imgsToRemove){
        this.id = id;
        LinkedList <String> _imgsToRemove = new LinkedList<>();
        for (Bitmap img : imgsToRemove){
            _imgsToRemove.addLast(ImageConvert.toBase64Str(img));
        }
        this.photo_del = _imgsToRemove;
    }

}
