package ServerExchange.ServerRequests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ServerExchange.Advert;
import ServerExchange.AdvertCard;
import ServerExchange.ImageConvert;
import ServerExchange.Profile;


/**
 * Created by Дима on 16.02.2018.
 */

public class GetProfileRequest extends TokenServerRequest<Profile> {

   @Override
    protected RequestType getRequestType(){ return RequestType.GET; }
	
	private String GET_PROFILE_METHOD_NAME = "GetProfile";

    private long id;

    public GetProfileRequest(String address) {super(address);}

    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("id",this.id);

        return new ServerMethod(GET_PROFILE_METHOD_NAME, params);
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return ProfileJsonServerAnswer.class;
    }

    class ProfileJsonServerAnswer extends JsonServerAnswer{
        public class JsonObj {
            public class HistoryContractJson {
                public String header;
                public long id_client;
                public long id_contract;
                public long id_witcher;
                public long last_update;
                public long last_status_update;
                public int status;
            }

            public class HistoryContainerJson {
                public long count;
                public HashMap<String, HistoryContractJson> contract;
            }

            HistoryContainerJson history;
            public String about;
            public long id;
            public String name;
            public String photo;
            public String type;
        }
        JsonObj object;
        @Override
        public Profile convert() {
            LinkedList<AdvertCard> advertCards = new LinkedList<>();
            for (Map.Entry<String, JsonObj.HistoryContractJson> contrEn : object.history.contract.entrySet()){
                JsonObj.HistoryContractJson contr = contrEn.getValue();

                java.util.Date date = new java.util.Date(contr.last_status_update * 1000);

                Advert.AdvertStatus status = Advert.AdvertStatus.fromInt(contr.status);

                advertCards.addLast( new AdvertCard(contr.id_contract, contr.header, contr.id_client, contr.id_witcher, date, status));
            }
            Profile.ProfileType ptype = object.type.equals("client") ? Profile.ProfileType.WITCHER : Profile.ProfileType.CUSTOMER;
            Profile profile = new Profile(id, object.name, object.about, ptype, advertCards, ImageConvert.fromBase64Str(object.photo));
            return profile;
        }
    }

    public void getProfile(long id, IServerAnswerHandler onGetProfileHandler){
        this.id = id;
        startRequest(onGetProfileHandler);
    }
}
