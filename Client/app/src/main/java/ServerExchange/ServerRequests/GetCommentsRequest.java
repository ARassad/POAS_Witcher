package ServerExchange.ServerRequests;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ServerExchange.Comment;
import ServerExchange.ImageConvert;
import ServerExchange.Profile;

/**
 * Created by Dryush on 16.02.2018.
 */

public class GetCommentsRequest extends TokenServerRequest<LinkedList<Comment>> {

    @Override
    protected RequestType getRequestType(){ return RequestType.GET; }

    String METHOD_NAME = "get_list_comment";
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
        class JsonComment{
            public String photo;
            public String author_name;
            public long author_id;
            public long date_of_create;
            public long id;
            public String text;
        }
        HashMap<String,JsonComment> comments;

        @Override
        public LinkedList<Comment> convert() {
            LinkedList<Comment> coms = new LinkedList<>();
            for (Map.Entry<String, JsonComment> comEm : comments.entrySet()){
                JsonComment com = comEm.getValue();
                Date date = new java.util.Date(com.date_of_create * 1000);
                coms.addLast( new Comment(com.id, com.text, com.author_id, com.author_name, date, ImageConvert.fromBase64Str(com.photo)));
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

}
