package ServerExchange.ServerRequests;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ServerExchange.ProfilePart;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Дима on 20.02.2018.
 */

public class GetWitcherDesiredContractRequest extends TokenServerRequest<ArrayList<ProfilePart>> {

    @Override
    protected RequestType getRequestType(){ return RequestType.GET; }

    private String GET_WITCHER_DESIRED_CONTRACT_METHOD_NAME = "GetWitcherDesiredContract";

    private long id_contract;

    public GetWitcherDesiredContractRequest(String serverAddress) {
        super(serverAddress);
    }
    public GetWitcherDesiredContractRequest() {
        super();
    }

    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("id",id_contract);

        return new ServerMethod(GET_WITCHER_DESIRED_CONTRACT_METHOD_NAME, params);
    }

    class JsonGetWithcerDesiredContractServerAnswer extends JsonServerAnswer{
        public class JsonObj {
            public class JsonWitchers {
                public long count;

                public class JsonWitcherInfo {
                    public long id;
                    public String name;
                    ProfilePart convert(){
                        return new ProfilePart(id,name);
                    }
                }

                public HashMap<String, JsonWitcherInfo> witcher;
            }

            public JsonWitchers witchers;
        }
        public JsonObj object;

        @Override
        public ArrayList<ProfilePart> convert() {
            LinkedList<ProfilePart> prfls = new LinkedList<>();
            if (object.witchers != null){
                if (object.witchers.witcher != null) {
                    for (Map.Entry<String, JsonObj.JsonWitchers.JsonWitcherInfo> winfo : object.witchers.witcher.entrySet()) {
                        prfls.addLast(winfo.getValue().convert());
                    }
                }
            }
            return new ArrayList<>(prfls);
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {

        return JsonGetWithcerDesiredContractServerAnswer.class;
    }

    public void getDesired (long id_contract, IServerAnswerHandler onGetWithcerDesiredContractHandler) {
        this.id_contract = id_contract;
        startRequest(onGetWithcerDesiredContractHandler);
    }
}
