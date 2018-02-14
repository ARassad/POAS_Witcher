package ServerExchange.ServerRequests;

import java.io.IOException;

import ServerExchange.Profile;

/**
 * Created by Dryush on 13.02.2018.
 */

public class AuthorizationProcess extends ServerRequest{

    public AuthorizationProcess(String serverAdress) {
        super(serverAdress);
    }

    class AuthServerAnswer{
        AuthServerAnswer(){}

    }


    private final String AUTH_METHOD_NAME = "Auth";
    private final String REGISTR_METHOD_NAME = "Reg";


    public Profile tryAuthorization(String login, String password) throws IOException {

        String names[] = new String[2];
        names[0] = "login";
        names[1] = "password";

        String params[] = new String[2];
        params[0] = login;
        params[1] = password;

        AuthServerAnswer answer = new AuthServerAnswer();


        super.doRequest(AUTH_METHOD_NAME, names, params, answer);

        return null; // TODO return something else
    }


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


        super.doRequest(REGISTR_METHOD_NAME, names, params, answer);

        return false; // TODO return something else
    }
}
