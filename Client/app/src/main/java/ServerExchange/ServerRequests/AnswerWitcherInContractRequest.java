package ServerExchange.ServerRequests;


import java.io.IOException;
import java.util.HashMap;

import ServerExchange.Advert;

/**
 * Created by Dima on 19.02.2018.
 */

public class AnswerWitcherInContractRequest extends TokenServerRequest<Boolean> {

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }
	
	private String ANSWER_WITCHER_IN_CONTRACT_METHOD_NAME = "AnswerWitcherInContract";

    private int status;
    private long id_contract;

    public AnswerWitcherInContractRequest(String address) {super(address);}
    public AnswerWitcherInContractRequest() {super();}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("status", String.valueOf(status));
        params.put("id_contract", String.valueOf(id_contract));

        return new ServerMethod(ANSWER_WITCHER_IN_CONTRACT_METHOD_NAME, params);
    }

    class JsonAnswerWitcherInContractServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return status.equals("OK");
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonAnswerWitcherInContractServerAnswer.class;
    }

    public void acceptToAdvert(long id_contract, IServerAnswerHandler onAnswerWitcherInContractHandler) {
        int newStatus = Advert.AdvertStatus.IN_PROCESS.toInt();
        AnswerWitcherInContract(newStatus,id_contract, onAnswerWitcherInContractHandler);
    }

    public void discardAdvert(long id_contract, IServerAnswerHandler onAnswerWitcherInContractHandler)  {
        int newStatus = Advert.AdvertStatus.FREE.toInt();
        AnswerWitcherInContract(newStatus,id_contract, onAnswerWitcherInContractHandler);
    }

    private void AnswerWitcherInContract(int status, long id_contract, IServerAnswerHandler onAnswerWitcherInContractHandler) {
        this.status = status;
        this.id_contract = id_contract;
        startRequest(onAnswerWitcherInContractHandler);
    }
}


