package ServerExchange.ServerRequests;

import java.io.IOException;
import java.util.HashMap;

import ServerExchange.Profile;

/**
 * Created by Dryush on 15.02.2018.
 */

public class RegistrationRequest extends ServerRequest<Boolean>{

    public RegistrationRequest() {
        super();
    }

    public RegistrationRequest(String address) {
        super(address);
    }

    private final String REGISTR_METHOD_NAME = "Reg";

    private String login;
    private String password;
    private String isWitcher;

    @Override
    protected ServerMethod getMethod() {
        HashMap<String, String>  params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);

        ServerMethod sm = new ServerMethod(REGISTR_METHOD_NAME, params);
        return sm;
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonServerAnswer.class;
    }



    public void registration(String login, String password, Profile.ProfileType type, IServerAnswerHandler onRegisteredHandler) throws IOException {

        this.login = login;
        this.password = password;
        this.isWitcher = type == Profile.ProfileType.WITCHER ? "1" : "0";
        this.startRequest(onRegisteredHandler);

    }
}
