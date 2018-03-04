package ServerExchange.ServerRequests;

import android.media.session.MediaSession;

import ServerExchange.Advert;
import ServerExchange.Profile;

/**
 * Created by Dryush on 15.02.2018.
 */

public abstract class LoginRequest<AnswerType> extends ServerRequest<AnswerType> {
    public LoginRequest(String serverAddress) {
        super(serverAddress);
    }
    public LoginRequest() {super();}

    static private Long loggedUserId = null;
    static public Long getLoggedUserId() { return loggedUserId.longValue();}

    static private Profile.ProfileType loggedUserType;
    static public Profile.ProfileType getLoggedUserType(){
        return loggedUserType;
    }

    private static String phone_number = null;
    public static String getPhoneNumber() { return phone_number; }


    abstract class LoginJsonServerAnswer extends JsonServerAnswer{
        class JsonToken{
            //public String token;
            public String phone_number;
            public Long id_profile;
            public int type;
        }
        public JsonToken object;
    }
    @Override
    protected void JsonAnswerHandler(JsonServerAnswer answ) {

        if (answ.isStatusOk()) {
            LoginJsonServerAnswer answ_ = (LoginJsonServerAnswer) answ;
            //TokenServerRequest.setToken(answ_.object.token);
            loggedUserId = answ_.object.id_profile;
            loggedUserType = answ_.object.type == 0 ? Profile.ProfileType.WITCHER : Profile.ProfileType.CUSTOMER;
            phone_number = answ_.object.phone_number;
        }
    }
}
