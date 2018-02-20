package ServerExchange.ServerRequests;

import java.util.HashMap;
import java.util.LinkedList;

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
        public class HistoryContractJson{
            public String header;
            public long id_client;
            public long id_contract;
            public long id_witcher;
            public long last_update;
            public long last_status_update;
            public int status; //TODO: определиться с обозначением статус
        }
        public class HistoryContainerJson{
            public long count;
            public HistoryContractJson contract[];
        }
        HistoryContainerJson history;
        public String about;
        public long id;
        public String name;
        public String photo;
        public String type; // TODO: Изменить, когда допилится запрос, возможно это будет не так

        @Override
        public Profile convert() {
            LinkedList<AdvertCard> advertCards = new LinkedList<>();
            for (HistoryContractJson contr : history.contract){
                java.util.Date date = new java.util.Date(contr.last_status_update); //TODO: Узнать надо ли домножить на 1000
                Advert.AdvertStatus status = Advert.AdvertStatus.COMPLETED; //TODO: Определиться с обозначением статуса
                advertCards.addLast( new AdvertCard(contr.id_contract, contr.header, contr.id_client, contr.id_witcher, date, status));
            }
            Profile.ProfileType ptype = type.equals("client") ? Profile.ProfileType.WITCHER : Profile.ProfileType.CUSTOMER;
            Profile profile = new Profile(id, name, about, ptype, advertCards, ImageConvert.fromBase64Str(photo));
            return profile;
        }
    }

    public void getProfile(long id, IServerAnswerHandler onGetProfileHandler){
        this.id = id;
        startRequest(onGetProfileHandler);
    }
}
