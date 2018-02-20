package ServerExchange.ServerRequests;

import android.media.session.MediaSession;

/**
 * Created by Dryush on 15.02.2018.
 */

public abstract class LoginRequest<AnswerType> extends ServerRequest<AnswerType> {
    public LoginRequest(String serverAddress) {
        super(serverAddress);
    }
    public LoginRequest() {super();}

    abstract class LoginJsonServerAnswer extends JsonServerAnswer{
        class JsonToken{
            public String token;
        }
        JsonToken object;
    }
    @Override
    protected void JsonAnswerHandler(JsonServerAnswer answ) {
        LoginJsonServerAnswer answ_ = (LoginJsonServerAnswer) answ;

        TokenServerRequest.setToken( answ_.object.token);
    }


}
