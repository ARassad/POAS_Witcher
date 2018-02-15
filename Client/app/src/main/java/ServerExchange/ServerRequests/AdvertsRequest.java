package ServerExchange.ServerRequests;

import java.util.HashMap;
import java.util.LinkedList;

import ServerExchange.Advert;
import ServerExchange.AdvertsList;

/**
 * Created by Dryush on 13.02.2018.
 */

class AdvertsRequest extends ServerRequest < LinkedList<Advert>> {


    private String GETLIST_METHOD_NAME = "Get_list_contract";
    private enum FilterType{
        BY_LOCATE,
        BY_REWARD;
    };

    private enum OrderType{
        IN_ASCENDING,
        IN_DESCENDING;
    };

    private enum SortType{
        BY_REWARD,
        BY_ALPHABET,
        BY_DATE,
        BY_LOCATE;
    }


    private HashMap<FilterType, String> FILTER_TYPE_NAMES;
    private HashMap<OrderType, String> ORDER_NAMES;
    private HashMap<SortType, String> SORT_NAMES;

    public AdvertsRequest(String serverAdress) {
        super(serverAdress);

        FILTER_TYPE_NAMES.put(FilterType.BY_LOCATE, "locate");
        FILTER_TYPE_NAMES.put(FilterType.BY_REWARD, "bounty");

        ORDER_NAMES.put(OrderType.IN_ASCENDING, "asc");
        ORDER_NAMES.put(OrderType.IN_DESCENDING,"desc");

        SORT_NAMES.put(SortType.BY_ALPHABET,    "alph");
        SORT_NAMES.put(SortType.BY_DATE,        "lastupdate");
        //SORT_NAMES.put(SortType.BY_REWARD,      "bounty");
        SORT_NAMES.put(SortType.BY_LOCATE,      "locate");
    }


    @Override
    protected ServerMethod getMethod() {
        return null;
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return null;
    }


}
