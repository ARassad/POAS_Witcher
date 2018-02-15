package ServerExchange.ServerRequests;

import android.media.session.MediaSession;

/**
 * Created by Dryush on 15.02.2018.
 */

public abstract class LoginRequest<AnswerType> extends ServerRequest<AnswerType> {
    public LoginRequest(String serverAddress) {
        super(serverAddress);
    }

    abstract class LoginJsonServerAnswer extends JsonServerAnswer{
        public String token;
    }
    @Override
    protected void JsonAnswerHandler(JsonServerAnswer answ) {
        LoginJsonServerAnswer answ_ = (LoginJsonServerAnswer) answ;

        TokenServerRequest.setToken( answ_.token);
    }


}
