package ServerExchange.ServerRequests;

import java.util.HashMap;

import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dryush on 04.03.2018.
 */

public class ExitProfileRequest extends TokenServerRequest<Boolean> {


    @Override
    protected RequestType getRequestType(){
        return RequestType.GET;
    }

    private String METHOD_NAME = "ExitProfile";
    @Override
    protected ServerMethod getMethod() {
        return new ServerMethod(METHOD_NAME, new HashMap<String,Object>());
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


    public void exit(IServerAnswerHandler handler){
        startRequest(handler);
    }
}
