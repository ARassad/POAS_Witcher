package ServerExchange.ServerRequests;

import java.util.HashMap;

import ServerExchange.Advert;
import ServerExchange.Comment;

/**
 * Created by Dryush on 16.02.2018.
 */

public class GetAdvertRequest extends TokenServerRequest<Advert> {

    @Override
    protected RequestType getRequestType(){ return RequestType.GET; }
	
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

        public class ClientJson{
            public long id;
            public String name;
        }
        public ClientJson client;

        public class AdvertJson{
            public long bounty;
            class Comments{
                public long count;
                public long comments[];
            }
            public Comments commentsContract;

            public long id;
            public String kingdom;
            public long last_update;
            public long last_update_status;

            public class PhotosJson{
                public long count;
                public long photo[];
            }
            public PhotosJson photoContact;
            public int status;
            public String town;
        }
        public AdvertJson object;

        public class WitcherJson{
            public long id;
            public String name;
        }
        public WitcherJson witcher;

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
