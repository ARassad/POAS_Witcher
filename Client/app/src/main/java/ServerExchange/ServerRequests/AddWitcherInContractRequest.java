package ServerExchange.ServerRequests;


import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Dima on 20.02.2018.
 */

public class AddWitcherInContractRequest extends TokenServerRequest<Boolean> {

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }

    private String ADD_WITCHER_IN_CONTRACT_METHOD_NAME = "AddWitcherInContract";

    private long id_contract;

    public AddWitcherInContractRequest(String address) {super(address);}
    public AddWitcherInContractRequest() {super();}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("id_contract", id_contract);

        return new ServerMethod(ADD_WITCHER_IN_CONTRACT_METHOD_NAME, params);
    }

    class JsonAddWitcherInContractServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {


        return AddWitcherInContractRequest.JsonAddWitcherInContractServerAnswer.class;
    }

    public void addWitcherInContract(long id_contract, IServerAnswerHandler onAddWitcherInContractHandler) throws IOException {
        this.id_contract = id_contract;
        startRequest(onAddWitcherInContractHandler);
    }
}
