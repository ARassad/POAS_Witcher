package ServerExchange.ServerRequests;


import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Dima on 20.02.2018.
 */

public class AddCommentContractRequest extends TokenServerRequest {

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }

    private String ADD_COMMENT_CONTRACT_METHOD_NAME = "AddCommentContract";

    private String text;
    private long profile_id;

    public AddCommentContractRequest(String address) {super(address);}
    public AddCommentContractRequest() {super();}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("text", text);
        params.put("id", profile_id);

        return new ServerMethod(ADD_COMMENT_CONTRACT_METHOD_NAME, params);
    }

    class JsonAddCommentContractServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {


        return AddCommentContractRequest.JsonAddCommentContractServerAnswer.class;
    }

    public void addCommentContract(String text, long id, IServerAnswerHandler onAddCommentContractHandler) throws IOException {
        this.text = text;
        this.profile_id = id;
        startRequest(onAddCommentContractHandler);
    }
}
