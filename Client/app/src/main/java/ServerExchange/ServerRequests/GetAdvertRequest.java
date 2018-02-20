package ServerExchange.ServerRequests;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ServerExchange.Advert;
import ServerExchange.Comment;
import ServerExchange.ImageConvert;
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

            public class ClientJson {
                public long id;
                public String name;
            }

            public ClientJson client;

            public class AdvertJson {
                public int bounty;
                public String header;

                public long id;
                public String kingdom;
                public long last_update;
                public long last_update_status;

                public class PhotosJson {
                    public long count;
                    public HashMap<String,String> photo;
                }

                public PhotosJson photoContact;
                public int status;
                public String text;
                public String town;
            }

            public AdvertJson object;

            public class WitcherJson {
                public long id;
                public String name;
            }

            public WitcherJson witcher;

        @Override
        public Advert convert() {
            java.util.Date dateOfLastUpdate = new java.util.Date(object.last_update * 1000);

            LinkedList<Bitmap> imgs = new LinkedList<>();
            for (Map.Entry<String, String> phEntry : object.photoContact.photo.entrySet()){
                imgs.addLast(ImageConvert.fromBase64Str(phEntry.getValue()));
            }

            Advert advert = new Advert(object.id, object.header, object.text, imgs, new Location(object.kingdom, object.town),
                    object.bounty, client.id, null/*other method*/, witcher.id, Advert.AdvertStatus.fromInt(object.status),
                    dateOfLastUpdate, null/*other method*/);
            return advert;
        }
    }

    public void getAdvert(long id, IServerAnswerHandler onGetAdvertHandler){
        this.id = id;
        startRequest(onGetAdvertHandler);
    }

}
