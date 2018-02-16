package ServerExchange.ServerRequests;

import java.util.HashMap;

import ServerExchange.AdvertsList;

import static ServerExchange.AdvertsList.FilterType.BY_LOCATION;
import static ServerExchange.AdvertsList.SortType.BY_DATE;
import static ServerExchange.AdvertsList.SortType.BY_NAME;

/**
 * Created by Дима on 15.02.2018.
 */

public class GetListOfContractsRequest extends ServerRequest<Boolean> {

    public GetListOfContractsRequest(String serverAddress, String login, String password, AdvertsList.SortType type, String params[]) {
        super(serverAddress);
        this.login = login;
        this.password = password;
        this.sortType = type;
        this.parameters = params;
    }

    public GetListOfContractsRequest(String serverAddress, String login, String password, AdvertsList.FilterType type, String params[]) {
        super(serverAddress);
        this.login = login;
        this.password = password;
        this.filterType = type;
        this.parameters = params;
    }

    private String login;
    private String password;
    private AdvertsList.SortType sortType;
    private AdvertsList.FilterType filterType;
    private String parameters[];

    private final String GET_LIST_METHOD_NAME = "Get_list_contract";
    @Override
    protected ServerMethod getMethod() {

        HashMap<String, String> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);

        if (sortType == BY_DATE){
            params.put("sort", "lastupdate");
            params.put("sortype", this.parameters[0]);
        }
        else if (sortType == BY_NAME){
            params.put("sort", "alph");
            params.put("sortype", this.parameters[0]);
        }
        else if (sortType == BY_LOCATION){
            params.put("sort", "alph");
            params.put("sortype", this.parameters[0]);
        }
        else if (filterType == BY_LOCATION){
            params.put("filter", "locate");
            params.put("sortype", this.parameters[0]);
            params.put("sortype", this.parameters[0]);
        }

        return new ServerMethod(GET_LIST_METHOD_NAME, params);
    }

    class GetListJsonServeAnswer extends JsonServerAnswer {

        @Override
        public Boolean convert() {
            return this.status.equals("OK");
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return JsonServerAnswer.class;
    }

}
