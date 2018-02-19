package ServerExchange.ServerRequests;


import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Dima on 19.02.2018.
 */

public class RefuseContractRequest extends TokenServerRequest {

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }
	
	private String REFUSE_CONTRACT_METHOD_NAME = "RefuseContract";

    private long id_contract;

    public RefuseContractRequest(String address) {super(address);}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("id_contract", String.valueOf(id_contract));

        return new ServerMethod(REFUSE_CONTRACT_METHOD_NAME, params);
    }

    class JsonRefuseContractServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return status.equals("OK");
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {


        return JsonServerAnswer.class;
    }

    public void RefuseContract(long id_contract, IServerAnswerHandler onRefuseContractHandler) throws IOException {
        this.id_contract = id_contract;
        startRequest(onRefuseContractHandler);
    }
}
