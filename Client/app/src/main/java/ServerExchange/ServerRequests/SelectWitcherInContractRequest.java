package ServerExchange.ServerRequests;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Dima on 19.02.2018.
 */

public class SelectWitcherInContractRequest extends TokenServerRequest {

    private String SELECT_WITCHER_IN_CONTRACT_METHOD_NAME = "SelectWitcherInContract";

    private long id_witcher;
    private long id_contract;

    public SelectWitcherInContractRequest(String address) {super(address);}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("id_witcher", String.valueOf(id_witcher));
        params.put("id_contract", String.valueOf(id_contract));

        return new ServerMethod(SELECT_WITCHER_IN_CONTRACT_METHOD_NAME, params);
    }

    class JsonSelectWitcherInContractServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return status.equals("OK");
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {


        return JsonServerAnswer.class;
    }

    public void SelectWitcherInContract(long id_witcher, long id_contract, IServerAnswerHandler onSelectWitcherInContractHandler) throws IOException {
        this.id_witcher = id_witcher;
        this.id_contract = id_contract;
        startRequest(onSelectWitcherInContractHandler);
    }
}
