package ServerExchange.ServerRequests;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Дима on 20.02.2018.
 */

public class GetWithcerDesiredContractRequest extends TokenServerRequest {

    @Override
    protected RequestType getRequestType(){ return RequestType.GET; }

    private String GET_WITCHER_DESIRED_CONTRACT_METHOD_NAME = "GetWithcerDesiredContract";

    private long id_contract;

    public GetWithcerDesiredContractRequest(String serverAddress) {
        super(serverAddress);
    }

    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("id",id_contract);

        return new ServerMethod(GET_WITCHER_DESIRED_CONTRACT_METHOD_NAME, params);
    }

    class JsonGetWithcerDesiredContractServerAnswer extends JsonServerAnswer{

        public ArrayList<Integer> idWitchersList;
        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {

        return JsonGetWithcerDesiredContractServerAnswer.class;
    }

    public void addCommentContract(long id_contract, IServerAnswerHandler onGetWithcerDesiredContractHandler) throws IOException {
        this.id_contract = id_contract;
        startRequest(onGetWithcerDesiredContractHandler);
    }
}
