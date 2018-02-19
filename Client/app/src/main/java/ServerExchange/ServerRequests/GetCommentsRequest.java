package ServerExchange.ServerRequests;

import java.util.HashMap;

/**
 * Created by Dryush on 16.02.2018.
 */

public class GetCommentsRequest extends TokenServerRequest {

    @Override
    protected RequestType getRequestType(){ return RequestType.GET; }

    String METHOD_NAME = "get_comments_list";
    String id;

    @Override
    protected ServerMethod getMethod() {
        HashMap<String,String> params = new HashMap<>();
        params.put("id", id);
        return new ServerMethod(METHOD_NAME, params);
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return null;
    }


}
