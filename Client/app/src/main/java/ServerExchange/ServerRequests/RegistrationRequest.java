package ServerExchange.ServerRequests;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import ServerExchange.Password;
import ServerExchange.Profile;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dryush on 15.02.2018.
 */

public class RegistrationRequest extends ServerRequest<Boolean>{

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }
	
	public RegistrationRequest() {
        super();
    }

    public RegistrationRequest(String address) {
        super(address);
    }

    private final String REGISTR_METHOD_NAME = "Reg";

    private FirebaseAuth firebaseAuth;

    private String login;
    private String password;
    private int isWitcher;
    private String phoneNumber;

    @Override
    protected ServerMethod getMethod() {
        HashMap<String, Object>  params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);
        params.put("isWitcher", isWitcher);
        params.put("phone_number", phoneNumber);

        ServerMethod sm = new ServerMethod(REGISTR_METHOD_NAME, params);
        return sm;
    }

    class JsonRegServerAnswer extends JsonServerAnswer{


        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonRegServerAnswer.class;
    }



    public void registration(String login, String password, Profile.ProfileType type, IServerAnswerHandler onLoginHandler) {

        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        this.login = login;
        this.password = Password.encode(password);
        this.isWitcher = type == Profile.ProfileType.WITCHER ? 1 : 0;
        this.phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        this.startRequest(onLoginHandler);

    }
}
