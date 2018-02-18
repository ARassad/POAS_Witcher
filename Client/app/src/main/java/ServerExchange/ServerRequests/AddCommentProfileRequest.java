package ServerExchange.ServerRequests;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by Дима on 18.02.2018.
 */

public class AddCommentProfileRequest extends TokenServerRequest {

    private String ADD_COMMENT_PROFILE_METHOD_NAME = "AddCommentProfile";

    private String text;
    private int order;
    private long id;

    public AddCommentProfileRequest(String address) {super(address);}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("text", text);
        params.put("order", String.valueOf(order));
        params.put("id", String.valueOf(id));

        return new ServerMethod(ADD_COMMENT_PROFILE_METHOD_NAME, params);
    }

    class JsonAddCommentProfileServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return status.equals("OK");
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {


        return JsonServerAnswer.class;
    }

    public void addCommentProfile(String text, int order, long id, IServerAnswerHandler onAddCommentProfileHandler) throws IOException {
        this.text = text;
        this.order = order;
        this.id = id;
        startRequest(onAddCommentProfileHandler);
    }

}
