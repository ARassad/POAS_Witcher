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


    static private String debugToken = "b5ad4e9f2bdedb278dd91ce0e8043fce";
    static private String token = debugToken;
    protected HashMap<String,Object> basicMethodParams(){
        HashMap<String, Object> params = super.basicMethodParams();
        params.put("token", token);
        return params;
    }

    @Override
    abstract protected ServerMethod getMethod();

    static void setToken(String newToken){
        token = newToken;
    }

}
