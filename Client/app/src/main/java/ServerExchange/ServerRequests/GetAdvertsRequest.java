package ServerExchange.ServerRequests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ServerExchange.Advert;
import ServerExchange.Location;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dryush on 13.02.2018.
 */

public class GetAdvertsRequest extends ServerRequest < LinkedList<Advert>> {

    public GetAdvertsRequest() {super();
        sortType = DEFAULT_SORT_TYPE;
        orderType = DEFAULT_ORDER_TYPE;
    }
    public GetAdvertsRequest(String address) {
        super(address);
        sortType = DEFAULT_SORT_TYPE;
        orderType = DEFAULT_ORDER_TYPE;
    }

    @Override
    public RequestType getRequestType(){
        return RequestType.GET;
    }

    private String GETLIST_METHOD_NAME = "Get_list_contract";
    private enum FilterType{
        BY_LOCATE,
        BY_REWARD;
        String toServerParam(){
            switch (this){
                case BY_LOCATE: return "locate";
                case BY_REWARD: return "bounte";
                default: throw new RuntimeException("Добавь новый тип в метод GetAdvertsRequest.FilterType.toServerParams");
            }
        }
    };

    private enum OrderType{
        IN_ASCENDING,
        IN_DESCENDING;
        String toServerParam(){
            switch (this){
                case IN_ASCENDING: return "asc";
                case IN_DESCENDING: return "desc";
                default: throw new RuntimeException("Добавь новый тип в метод GetAdvertsRequest.OrderType.toServerParams");
            }
        }
    };

    public enum SortType{
        //BY_REWARD,
        BY_ALPHABET,
        BY_DATE,
        BY_LOCATE;
        String toServerParams() {
            switch (this) {
                case BY_LOCATE: return "locate";
                case BY_DATE:   return "lastupdate";
                case BY_ALPHABET: return "alph";
                default: throw new RuntimeException("Добавь новый тип в метод GetAdvertsRequest.SortType.toServerParams");
            }
        }
    }




    private SortType sortType;
    private SortType DEFAULT_SORT_TYPE = SortType.BY_ALPHABET;
    private OrderType orderType;
    private OrderType DEFAULT_ORDER_TYPE = OrderType.IN_ASCENDING;
    private FilterType filterType;
    private int minmax[] = new int[2];
    private Location loc;




    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();

        if (sortType != null){
            params.put("sort", sortType.toServerParams());
        }

        if (orderType != null){
            params.put("order", orderType.toServerParam());
        }
        if (filterType != null){
            params.put("filter", filterType.toServerParam());

            if (filterType == FilterType.BY_REWARD){
                params.put("min", this.minmax[0]);
                params.put("max", this.minmax[1]);
            }
            else if (filterType == FilterType.BY_LOCATE){
                params.put("kingdom", this.loc.getKingdom());
                params.put("town", this.loc.getCity());
            }

        }

        this.sortType = DEFAULT_SORT_TYPE;
        this.orderType = DEFAULT_ORDER_TYPE;
        this.filterType = null;
        this.minmax = null;
        this.loc = null;

        return new ServerMethod(GETLIST_METHOD_NAME, params);
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return AdvertServerAnswer.class;
    }

    class AdvertServerAnswer extends JsonServerAnswer{
        class JsonObj {
            class OneContractJson {
                int bounty;
                String header;
                long id;
                long id_client;
                long id_list_comments; //TODO: Узнать, что это, хотя не важно
                long id_list_photos; //В целом нужно ли?
                long id_task_located; // TODO: попросить локации строкой
                long id_witcher; //Иcполнитель
                long last_update;
                long last_update_status;
                int status;
                String text;
            }
            HashMap<String,OneContractJson> contracts;
        }
        JsonObj object;


        @Override
        public LinkedList<Advert> convert() {
            LinkedList<Advert> adverts = new LinkedList<>();
            for (Map.Entry<String, JsonObj.OneContractJson> key_contract : object.contracts.entrySet()){
                JsonObj.OneContractJson contract = key_contract.getValue();
                java.util.Date update_time = new java.util.Date(contract.last_update * 1000);
                Advert.AdvertStatus st = Advert.AdvertStatus.fromInt(contract.status);
                Advert advert = new Advert(contract.id, contract.header, contract.text, null, new Location(null, null), contract.bounty, contract.id_client, null, contract.id_witcher, st, update_time, null);
                adverts.addLast(advert);
            }
            return adverts;
        }
    }

    public void getSortedBy (SortType sort, OrderType order, IServerAnswerHandler onGetListHandler ){
        this.sortType = sort;
        this.orderType = order;
        startRequest(onGetListHandler);
    }

    public void getSortedBy (SortType sort, IServerAnswerHandler onGetListHandler ){
        this.sortType = sort;
        startRequest(onGetListHandler);
    }

    public void getFilteredByReward (FilterType type, int min, int max, IServerAnswerHandler onGetListHandler){
        this.filterType = type;
        minmax[0] = min;
        minmax[1] = max;
        startRequest(onGetListHandler);
    }

    public void getFilteredByReward (FilterType type, int min, int max, SortType sort, IServerAnswerHandler onGetListHandler){
        this.filterType = type;
        this.sortType = sort;
        minmax[0] = min;
        minmax[1] = max;
        startRequest(onGetListHandler);
    }

    public void getFilteredByReward (FilterType type, int min, int max, SortType sort, OrderType order, IServerAnswerHandler onGetListHandler){
        this.filterType = type;
        this.sortType = sort;
        this.orderType = order;
        minmax[0] = min;
        minmax[1] = max;
        startRequest(onGetListHandler);
    }

    public void getFilteredByLocation (FilterType type, SortType sort, String kingdom, String town, IServerAnswerHandler onGetListHandler){
        this.sortType = sort;
        this.filterType = type;
        loc = new Location(kingdom, town);
        startRequest(onGetListHandler);
    }

    public void getFilteredByLocation (FilterType type, SortType sort, OrderType order, String kingdom, String town, IServerAnswerHandler onGetListHandler){
        this.sortType = sort;
        this.filterType = type;
        loc = new Location(kingdom, town);
        startRequest(onGetListHandler);
    }
}
