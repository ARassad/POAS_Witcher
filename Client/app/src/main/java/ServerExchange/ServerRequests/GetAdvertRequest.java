package ServerExchange.ServerRequests;

import java.util.HashMap;

import ServerExchange.Advert;
import ServerExchange.Comment;

/**
 * Created by Dryush on 16.02.2018.
 */

public class GetAdvertRequest extends TokenServerRequest<Advert> {

    public GetAdvertRequest(String serverAddress) {
        super(serverAddress);
    }

    @Override
    protected ServerMethod getMethod() {
        return null;
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return null;
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

}
