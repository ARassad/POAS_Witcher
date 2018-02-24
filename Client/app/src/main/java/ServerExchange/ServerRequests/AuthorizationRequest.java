package ServerExchange.ServerRequests;

import java.util.HashMap;

import ServerExchange.Password;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dryush on 13.02.2018.
 */

public class AuthorizationRequest extends LoginRequest<Boolean>{

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }
	
	public AuthorizationRequest() {
        super();
    }

    public AuthorizationRequest(String address){
        super(address);
    }


    private String login;
    private String password;
    private String fcmToken;

    private final String AUTH_METHOD_NAME = "Auth";
    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);
        params.put("fcm_token", fcmToken);

        return new ServerMethod(AUTH_METHOD_NAME, params);
    }

    class AuthJsonServeAnswer extends LoginJsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return AuthJsonServeAnswer.class;
    }

    public void login(String login, String password, String fcmToken, IServerAnswerHandler onLoginHandler){
        this.login = login;
        this.password = Password.encode(password);
        this.fcmToken = fcmToken;
        startRequest(onLoginHandler);
    }

}
