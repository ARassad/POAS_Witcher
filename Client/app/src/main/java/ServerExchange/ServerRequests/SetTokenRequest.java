package ServerExchange.ServerRequests;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dryush on 04.03.2018.
 */

public class SetTokenRequest extends ServerRequest<Boolean> {

    protected RequestType getRequestType(){
        return RequestType.POST;
    }

    FirebaseAuth firebaseAuth = null;

    private String METHOD_NAME = "SetToken";
    private String phoneNumber;
    private String token;
    @Override
    protected ServerMethod getMethod() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("phone_number", phoneNumber);
        params.put("token", token);
        return new ServerMethod(METHOD_NAME, params);
    }

    protected  class JsonAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonAnswer.class;
    }

    protected void set(String phoneNumber, String token, IServerAnswerHandler handler){
        this.phoneNumber = phoneNumber;
        this.token = token;
        startRequest( handler);
    }

    public void set( IServerAnswerHandler handler){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        set(firebaseUser.getPhoneNumber(), firebaseUser.getUid(),handler);
    }
}
