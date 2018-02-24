package ServerExchange.ServerRequests;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ServerExchange.Comment;
import ServerExchange.ImageConvert;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dryush on 16.02.2018.
 */

public class GetCommentsRequest extends TokenServerRequest<LinkedList<Comment>> {

    @Override
    protected RequestType getRequestType(){ return RequestType.GET; }

    String METHOD_NAME = "GetListComments";
    long id;

    @Override
    //TODO: Привести в к формату запроса
    protected ServerMethod getMethod() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("type", type.toServeRequestValue());
        return new ServerMethod(METHOD_NAME, params);
    }

    public enum Type{
        PROFILE,
        ADVERT;

        public String toServeRequestValue(){
            String val = "";
            if (this == PROFILE){
                val = "profile";
            } else if (this == ADVERT){
                val = "contract";
            }
            return val;
        }
    }
    Type type;



    //TODO: уточнить ответ от сервера
    class JsonCommentsAnswer extends JsonServerAnswer{
        class JsonObj {
            class JsonComment {
                public String photo;
                public String name;
                public long date;
                public long id_prof;
                public String text;
            }

            HashMap<String, JsonComment> id_comments;
        }
        JsonObj object;
        @Override
        public LinkedList<Comment> convert() {
            LinkedList<Comment> coms = new LinkedList<>();
            if (object.id_comments != null) {
                for (Map.Entry<String, JsonObj.JsonComment> comEm : object.id_comments.entrySet()) {
                    JsonObj.JsonComment com = comEm.getValue();
                    Date date = new java.util.Date(com.date * 1000);
                    coms.addLast(new Comment(com.text, com.id_prof, com.name, date, ImageConvert.fromBase64Str(com.photo)));
                }
            }
            return coms;
        }
    }


    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonCommentsAnswer.class;
    }


    public void getAdvertComments(long id, IServerAnswerHandler onGetCommentsHandler){
        type = Type.ADVERT;
        this.id = id;
        startRequest(onGetCommentsHandler);
    }
    public void getProfileComments(long id, IServerAnswerHandler onGetCommentsHandler){
        type = Type.PROFILE;
        this.id = id;
        startRequest(onGetCommentsHandler);

    }

    public void getLoggedProfile(IServerAnswerHandler onGetProfileHandler){
        getProfileComments(LoginRequest.getLoggedUserId(), onGetProfileHandler);
    }
}
