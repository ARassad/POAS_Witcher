package ServerExchange.ServerRequests;

import java.io.IOException;
import java.util.HashMap;

import ServerExchange.Password;
import ServerExchange.Profile;

/**
 * Created by Dryush on 13.02.2018.
 */

public class AuthorizationRequest extends LoginRequest<Boolean>{

    public AuthorizationRequest() {
        super();
    }

    public AuthorizationRequest(String address){
        super(address);
    }


    private String login;
    private String password;

    private final String AUTH_METHOD_NAME = "Auth";
    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);

        return new ServerMethod(AUTH_METHOD_NAME, params);
    }

    class AuthJsonServeAnswer extends LoginJsonServerAnswer{

        @Override
        public Boolean convert() {
            return this.status.equals("OK");
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return AuthJsonServeAnswer.class;
    }

    public void login(String login, String password, IServerAnswerHandler onLoginHandler){
        this.login = login;
        this.password = Password.encode(password);
        startRequest(onLoginHandler);
    }

}
