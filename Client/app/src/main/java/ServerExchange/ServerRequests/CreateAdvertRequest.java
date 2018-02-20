package ServerExchange.ServerRequests;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import ServerExchange.Advert;
import ServerExchange.ImageConvert;
import ServerExchange.Location;

/**
 * Created by Dima on 20.02.2018.
 */

public class CreateAdvertRequest extends TokenServerRequest {

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }

    private String CREATE_ADVERT_METHOD_NAME = "CreateAdvert";

    private int id_task_located;
    private String text;
    private int bounty;
    private ArrayList<Base64> photo;

    public CreateAdvertRequest(String serverAddress) {
        super(serverAddress);
    }

    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("id_task_located",String.valueOf(this.id_task_located));
        params.put("text",text);
        params.put("bounty",String.valueOf(this.bounty));
        String photos = new String();
        for (Base64 photo:photo){
            photos+= ImageConvert.toBase64Str(photo);
            photos+=" ";
        }
        params.put("photo",photos);

        return new ServerMethod(CREATE_ADVERT_METHOD_NAME, params);
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {

        return CreateAdvertRequest.AdvertJsonServerAnswer.class;
    }


    class AdvertJsonServerAnswer extends JsonServerAnswer{

        public class ClientJson{
            public long id;
            public String name;
        }
        public GetAdvertRequest.AdvertJsonServerAnswer.ClientJson client;

        public class AdvertJson{
            public int bounty;
            class Comments{
                public long count;
                public long comments[];
            }
            public GetAdvertRequest.AdvertJsonServerAnswer.AdvertJson.Comments commentsContract;
            //TODO: Напомнить про заголовок

            public long id;
            public String kingdom;
            public long last_update;
            public long last_update_status;

            public class PhotosJson{
                public long count;
                public long photo[];
            }
            public GetAdvertRequest.AdvertJsonServerAnswer.AdvertJson.PhotosJson photoContact;
            public int status;
            public String text;
            public String town;
        }
        public GetAdvertRequest.AdvertJsonServerAnswer.AdvertJson object;

        public class WitcherJson{
            public long id;
            public String name;
        }
        public GetAdvertRequest.AdvertJsonServerAnswer.WitcherJson witcher;

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

    public void createAdvert(int id_task_located, String text, int bounty, ArrayList<Base64> photo, IServerAnswerHandler onCreateAdvertHandler) throws IOException {
        this.id_task_located = id_task_located;
        this.text = text;
        this.bounty = bounty;
        this.photo = photo;
        startRequest(onCreateAdvertHandler);
    }
}
