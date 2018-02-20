package ServerExchange.ServerRequests;

import java.util.HashMap;

/**
 * Created by Dryush on 20.02.2018.
 */

public class RemoveAdvertRequest extends TokenServerRequest<Boolean> {
    @Override
    public RequestType getRequestType(){ return RequestType.GET;}


    private static String METHOD_NAME = "DeleteAdvert";

    private int id;

    @Override
    protected ServerMethod getMethod() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return new ServerMethod(METHOD_NAME, params);
    }

    class RemoveRequestJsonAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }
    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return RemoveRequestJsonAnswer.class;
    }

    public void removeAdvert(int id, IServerAnswerHandler onRemovedHandler){
        this.id = id;
        startRequest(onRemovedHandler);
    }
}
