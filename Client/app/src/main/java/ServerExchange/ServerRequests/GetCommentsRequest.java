package ServerExchange.ServerRequests;

import java.util.HashMap;
import java.util.LinkedList;

import ServerExchange.Comment;
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



    class JsonCommentsAnswer extends JsonServerAnswer{


        @Override
        public LinkedList<Comment> convert() {
            return null;
        }
    }


    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return null;
    }

    /* TODO:
    public LinkedList<Comment> getAdvertComments(long id){
        type = Type.ADVERT;
    }
    public LinkedList<Comment> getProfileComments(long id){
        type = Type.PROFILE;
    }
*/
}
