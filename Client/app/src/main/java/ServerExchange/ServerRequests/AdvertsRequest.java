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


    private SortType sortType;
    private OrderType orderType;
    private FilterType filterType;
    private String parameters[];

    public AdvertsRequest(){

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

        HashMap<String, String> params = new HashMap<>();

        if (sortType != null){
            params.put("sort", SORT_NAMES.get(sortType));
            params.put("order", ORDER_NAMES.get(orderType));
        }
        else if (filterType != null){
            params.put("filter", FILTER_TYPE_NAMES.get(filterType));

            if (filterType == FilterType.BY_REWARD){
                params.put("min", this.parameters[0]);
                params.put("max", this.parameters[1]);
            }
            else if (filterType == FilterType.BY_LOCATE){
                params.put("kingdom", this.parameters[0]);
                if (this.parameters.length > 1){
                    params.put("town", this.parameters[1]);
                }
            }

        }

        this.sortType = null;
        this.orderType = null;
        this.filterType = null;
        this.parameters = null;

        return new ServerMethod(GETLIST_METHOD_NAME, params);
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return AdvertServerAnswer.class;
    }

    class AdvertServerAnswer extends JsonServerAnswer{

        @Override
        public LinkedList<Advert> convert() {
            return null;
        }
    }

    public void getSortedBy (SortType sort, OrderType order, IServerAnswerHandler onGetListHandler ){
        this.sortType = sort;
        this.orderType = order;
        startRequest(onGetListHandler);
    }

    public void getFilteredByReward (FilterType type, int min, int max, IServerAnswerHandler onGetListHandler){
        this.filterType = type;
        parameters[0] = String.valueOf(min);
        parameters[1] = String.valueOf(max);
        startRequest(onGetListHandler);
    }

    public void getFilteredByLocation (FilterType type, String kingdom, String town, IServerAnswerHandler onGetListHandler){
        this.filterType = type;
        parameters[0] = kingdom;
        if (town != null)
            parameters[1] = town;
        startRequest(onGetListHandler);
    }
}
