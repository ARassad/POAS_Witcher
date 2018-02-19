package ServerExchange.ServerRequests;

import java.util.HashMap;

import ServerExchange.Advert;
import ServerExchange.Comment;

/**
 * Created by Dryush on 16.02.2018.
 */

public class GetAdvertRequest extends TokenServerRequest<Advert> {

    private String GET_ADVERT_METHOD_NAME = "GetAdvert";

    private long id;

    public GetAdvertRequest(String serverAddress) {
        super(serverAddress);
    }

    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("id",String.valueOf(this.id));

        return new ServerMethod(GET_ADVERT_METHOD_NAME, params);
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {

        return AdvertJsonServerAnswer.class;
    }


    class AdvertJsonServerAnswer extends JsonServerAnswer{

        class CommentsProfilesJson{
            String count;
            HashMap<String, CommentJson> commentsProfile;

            class CommentJson{
                String create_date;
                String order;
                String text;
                //Comment convert(){
                    //Comment com = new Comment()
                //}

            }
        }

        public String about;
        public String id;
        public String name;
        public String photo;
        CommentsProfilesJson commentsProfile;

        @Override
        public Advert convert() {
            return null;
        }
    }

    public void getAdvert(long id, IServerAnswerHandler onGetAdvertHandler){
        this.id = id;
        startRequest(onGetAdvertHandler);
    }

}
