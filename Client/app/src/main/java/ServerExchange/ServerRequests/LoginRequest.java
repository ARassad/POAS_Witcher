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


    abstract class LoginJsonServerAnswer extends JsonServerAnswer{
        class JsonToken{
            public String token;
            public Long id_profile;
        }
        public JsonToken object;
    }
    @Override
    protected void JsonAnswerHandler(JsonServerAnswer answ) {

        if (answ.isStatusOk()) {
            LoginJsonServerAnswer answ_ = (LoginJsonServerAnswer) answ;
            TokenServerRequest.setToken(answ_.object.token);
            loggedUserId = answ_.object.id_profile;
        }
    }
}
