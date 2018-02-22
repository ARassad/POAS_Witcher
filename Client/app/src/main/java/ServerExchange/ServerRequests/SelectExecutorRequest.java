package ServerExchange.ServerRequests;

import java.io.IOException;
import java.util.HashMap;

import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dima on 19.02.2018.
 */

public class SelectExecutorRequest extends TokenServerRequest<Boolean> {


    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }

    private String SELECT_WITCHER_IN_CONTRACT_METHOD_NAME = "SelectWitcherInContract";

    private long id_witcher;
    private long id_contract;

    public SelectExecutorRequest(String address) {super(address);}
    public SelectExecutorRequest() {super();}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("id_witcher", id_witcher);
        params.put("id_contract", id_contract);

        return new ServerMethod(SELECT_WITCHER_IN_CONTRACT_METHOD_NAME, params);
    }

    class JsonSelectWitcherInContractServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {


        return JsonSelectWitcherInContractServerAnswer.class;
    }

    public void SelectWitcherInContract(long id_witcher, long id_contract, IServerAnswerHandler onSelectWitcherInContractHandler) throws IOException {
        this.id_witcher = id_witcher;
        this.id_contract = id_contract;
        startRequest(onSelectWitcherInContractHandler);
    }
}
