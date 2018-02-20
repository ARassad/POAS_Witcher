package ServerExchange.ServerRequests;

import java.util.Date;
import java.util.HashMap;

import ServerExchange.Advert;
import ServerExchange.Comment;
import ServerExchange.Location;

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
            public int bounty;
            class Comments{
                public long count;
                public long comments[];
            }
            public Comments commentsContract;
            //TODO: Напомнить про заголовок

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
            public String text;
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
            java.util.Date dateOfLastUpdate = new java.util.Date(object.last_update);
            //TODO: Узнать, как приходят фотки
            Advert advert = new Advert(object.id, "fix it", object.text, null, new Location(object.kingdom, object.town),
                    object.bounty, client.id, null/*other method*/, witcher.id, Advert.AdvertStatus.fromInt(object.status),
                    dateOfLastUpdate, null);
            return advert;
        }
    }

    public void getAdvert(long id, IServerAnswerHandler onGetAdvertHandler){
        this.id = id;
        startRequest(onGetAdvertHandler);
    }

}
