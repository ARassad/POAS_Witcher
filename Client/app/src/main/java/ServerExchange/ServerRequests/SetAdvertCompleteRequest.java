package ServerExchange.ServerRequests;

import java.util.HashMap;

import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;
import ServerExchange.ServerRequests.ServerRequest;
import ServerExchange.ServerRequests.TokenServerRequest;

/**
 * Created by Acer on 01.03.2018.
 */

public class SetAdvertCompleteRequest extends TokenServerRequest<Boolean> {

    @Override
    protected RequestType getRequestType(){ return RequestType.GET; }
    private Long advertId;

    final static String METHOD_NAME = "ContractComplited";
    @Override
    protected ServerMethod getMethod() {
        HashMap<String, Object> params = new HashMap<>();

        params.put("id", advertId);

        return new ServerMethod(METHOD_NAME, params);
    }

    private class JsonAnswer extends JsonServerAnswer{


        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonAnswer.class;
    }

    public void setComplete(long id,IServerAnswerHandler onSeted){
        this.advertId = id;
        startRequest(onSeted);
    }
}
