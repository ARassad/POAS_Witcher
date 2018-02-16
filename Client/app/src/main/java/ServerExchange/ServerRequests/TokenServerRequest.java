package ServerExchange.ServerRequests;

import java.util.HashMap;

/**
 * Created by Dryush on 15.02.2018.
 */

public abstract class TokenServerRequest<AnswerType> extends ServerRequest<AnswerType> {
    public TokenServerRequest() {super();}
    public TokenServerRequest(String serverAddress) {
        super(serverAddress);
    }

    static private String token;

    protected HashMap<String,String> basicMethodParams(){
        HashMap<String,String> params = super.basicMethodParams();
        params.put("token", token);
        return params;
    }

    @Override
    abstract protected ServerMethod getMethod();

    static void setToken(String newToken){
        token = newToken;
    }

}
