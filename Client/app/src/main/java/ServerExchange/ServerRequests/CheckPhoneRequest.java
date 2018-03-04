package ServerExchange.ServerRequests;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;
import ServerExchange.ServerRequests.ServerRequest;

/**
 * Created by Dryush on 04.03.2018.
 */

public class CheckPhoneRequest extends ServerRequest<Boolean> {

    @Override
    protected RequestType getRequestType(){
        return RequestType.POST;
    }

    FirebaseAuth firebaseAuth = null;

    private String METHOD_NAME = "CheckPhone";
    private String phoneNumber;
    @Override
    protected ServerMethod getMethod() {
        HashMap<String,Object> params = new HashMap<>();
        params.put("phone_number", phoneNumber);
        return new ServerMethod(METHOD_NAME, params);
    }

    protected class JSonAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JSonAnswer.class;
    }

    public void check(String phoneNumber, IServerAnswerHandler handler){
        this.phoneNumber = phoneNumber;
        this.startRequest(handler);
    }

    /*
    public void check(IServerAnswerHandler handler){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        this.check(firebaseAuth.getCurrentUser().getPhoneNumber(), handler);
    }
*/
}
