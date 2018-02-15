package ServerExchange.ServerRequests;

import java.io.IOException;
import java.util.HashMap;

import ServerExchange.Profile;

/**
 * Created by Dryush on 13.02.2018.
 */

public class AuthorizationRequest extends LoginRequest<Boolean>{

    public AuthorizationRequest(String serverAddress, String login, String password) {
        super(serverAddress);
        this.login = login;
        this.password = password;

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

    class AuthServerAnswer{
        AuthServerAnswer(){}

    }


    private final String REGISTR_METHOD_NAME = "Reg";



    class regServerAnswer{
        regServerAnswer(){}

    }
    public boolean tryRegistration(String login, String password, Profile.ProfileType type) throws IOException {
        String names[] = new String[3];
        names[0] = "login";
        names[1] = "password";
        names[2] = "witcher";

        String params[] = new String[3];
        params[0] = login;
        params[1] = password;
        params[2] = type == Profile.ProfileType.WITCHER ? "True" : "False";

        AuthServerAnswer answer = new AuthServerAnswer();


        return false; // TODO return something else
    }
}
