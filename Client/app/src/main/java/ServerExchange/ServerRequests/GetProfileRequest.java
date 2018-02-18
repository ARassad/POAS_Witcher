package ServerExchange.ServerRequests;

import java.util.HashMap;


/**
 * Created by Дима on 16.02.2018.
 */

public class GetProfileRequest extends TokenServerRequest {

    private String GET_PROFILE_METHOD_NAME = "GetProfile";

    private long id;

    public GetProfileRequest(String address) {super(address);}

    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("id",String.valueOf(this.id));

        return new ServerMethod(GET_PROFILE_METHOD_NAME, params);
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonServerAnswer.class;
    }

    class ProfileJsonServerAnswer extends JsonServerAnswer{

        @Override
        public Object convert() {
            return null;
        }
    }

    public void getProfile(long id, IServerAnswerHandler onGetListHandler){
        this.id = id;
        startRequest(onGetListHandler);
    }
}
