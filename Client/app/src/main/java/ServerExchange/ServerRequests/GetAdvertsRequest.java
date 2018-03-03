package ServerExchange.ServerRequests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import ServerExchange.Advert;
import ServerExchange.Location;
import ServerExchange.LocationsList;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dryush on 13.02.2018.
 */

public class GetAdvertsRequest extends TokenServerRequest < ArrayList<Advert>> {

    public GetAdvertsRequest() {
        super();
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

    private String METHOD_NAME ;
    private final String GET_FREE_ADVERTS_LIST = "Get_list_contract";
    private final String GET_WITCHER_ADVERTS_LIST = "GetContractWitcher";
    private final String GET_CLIENT_ADVERTS_LIST=   "GetContractClient";


    public enum FilterType{
        BY_LOCATE,
        BY_REWARD;
        public String toServerParam(){
            switch (this){
                case BY_LOCATE: return "locate";
                case BY_REWARD: return "bounty";
                default: throw new RuntimeException("Добавь новый тип в метод GetAdvertsRequest.FilterType.toServerParams");
            }
        }
        public static  FilterType fromInt(int type){
            switch (type){
                case 0 : return BY_REWARD;
                case 1 : return BY_LOCATE;
                default: throw new RuntimeException("не знаю такого фильтра");
            }
        }
    };

    public enum OrderType{
        IN_ASCENDING,
        IN_DESCENDING;
        public String toServerParam(){
            switch (this){
                case IN_ASCENDING: return "asc";
                case IN_DESCENDING: return "desc";
                default: throw new RuntimeException("Добавь новый тип в метод GetAdvertsRequest.OrderType.toServerParams");
            }
        }
        public static OrderType fromInt(int type){
            switch (type){
                case 0 : return  IN_ASCENDING;
                case 1 : return  IN_DESCENDING;
                default: throw  new RuntimeException("Не знаю такого порядка");
            }
        }
    };

    public enum SortType{
        //BY_REWARD,
        BY_ALPHABET,
        BY_DATE,
        BY_LOCATE;
        public String toServerParams() {
            switch (this) {
                case BY_LOCATE: return "locate";
                case BY_DATE:   return "lastupdate";
                case BY_ALPHABET: return "alph";
                default: throw new RuntimeException("Добавь новый тип в метод GetAdvertsRequest.SortType.toServerParams");
            }
        }
        public static SortType fromInt(int type){
            switch (type){
                case 0 : return BY_ALPHABET;
                case 1 : return BY_LOCATE;
                case 2 : return BY_DATE;
                default: throw  new RuntimeException("Не знаю такого типа сортировки");
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
    private Advert.AdvertStatus status;



    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();

        if (sortType != null){
            params.put("sort", sortType.toServerParams());
        }

        if (orderType != null){
            params.put("sortype", orderType.toServerParam());
        }
        if (filterType != null){
            params.put("filter", filterType.toServerParam());

            if (filterType == FilterType.BY_REWARD){
                params.put("min", this.minmax[0]);
                params.put("max", this.minmax[1]);
            }
            else if (filterType == FilterType.BY_LOCATE){
                if ( this.loc.getKingdom() != null) {
                    params.put("kingdom", this.loc.getKingdom());
                }
                if ( this.loc.getCity() != null) {
                    params.put("town", this.loc.getCity());
                }
            }

        }

        if (status != null){
            params.put("status", status.toInt());
        }

        this.sortType = DEFAULT_SORT_TYPE;
        this.orderType = DEFAULT_ORDER_TYPE;
        this.filterType = null;
        this.minmax = new int[2];
        this.loc = null;
        this.status = null;

        return new ServerMethod(METHOD_NAME, params);
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
            TreeMap<String,OneContractJson> contracts;
        }
        JsonObj object;


        @Override
        public ArrayList<Advert> convert() {
            ArrayList<Advert> adverts = new ArrayList<>();
            if (object != null && object.contracts!= null) {
                int size = object.contracts.size();
                adverts = new ArrayList<Advert>(Arrays.asList(new Advert[size]));

                LocationsList locsList = LocationsList.getInstance();

                for (Map.Entry<String, JsonObj.OneContractJson> key_contract : object.contracts.entrySet()) {
                    JsonObj.OneContractJson contract = key_contract.getValue();
                    java.util.Date update_time = new java.util.Date(contract.last_update * 1000);
                    Advert.AdvertStatus st = Advert.AdvertStatus.fromInt(contract.status);
                    Location loc = locsList.getById(contract.id_task_located);
                    Advert advert = new Advert(contract.id, contract.header, contract.text, null, loc, contract.bounty, contract.id_client, "",null, null, contract.id_witcher, "", st, update_time, null);
                    adverts.set(Integer.parseInt(key_contract.getKey()), advert );
                }
            }
            return adverts;
        }
    }

    public void getFreeSortedBy (SortType sort, OrderType order, IServerAnswerHandler onGetListHandler ){
        METHOD_NAME = GET_FREE_ADVERTS_LIST;
        this.sortType = sort;
        this.orderType = order;
        startRequest(onGetListHandler);
    }

    public void getFreeSortedBy (SortType sort, IServerAnswerHandler onGetListHandler ){
        METHOD_NAME = GET_FREE_ADVERTS_LIST;
        this.sortType = sort;
        startRequest(onGetListHandler);
    }

    public void getFreeFilteredByReward (FilterType type, int min, int max, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_FREE_ADVERTS_LIST;
        this.filterType = type;
        minmax[0] = min;
        minmax[1] = max;
        startRequest(onGetListHandler);
    }

    public void getFreeFilteredByReward (FilterType type, int min, int max, SortType sort, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_FREE_ADVERTS_LIST;
        this.filterType = type;
        this.sortType = sort;
        minmax[0] = min;
        minmax[1] = max;
        startRequest(onGetListHandler);
    }

    public void getFreeFilteredByReward (FilterType type, int min, int max, SortType sort, OrderType order, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_FREE_ADVERTS_LIST;
        this.filterType = type;
        this.sortType = sort;
        this.orderType = order;
        minmax[0] = min;
        minmax[1] = max;
        startRequest(onGetListHandler);
    }

    public void getFreeFilteredByLocation (FilterType type, SortType sort, String kingdom, String town, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_FREE_ADVERTS_LIST;
        this.sortType = sort;
        this.filterType = type;
        loc = new Location(kingdom, town);
        startRequest(onGetListHandler);
    }

    public void getFreeFilteredByLocation (FilterType type, SortType sort, OrderType order, String kingdom, String town, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_FREE_ADVERTS_LIST;
        this.sortType = sort;
        this.filterType = type;
        loc = new Location(kingdom, town);
        startRequest(onGetListHandler);
    }

    public void getWitcherSortedBy (Advert.AdvertStatus status, SortType sort, OrderType order, IServerAnswerHandler onGetListHandler ){
        METHOD_NAME = GET_WITCHER_ADVERTS_LIST;
        this.sortType = sort;
        this.orderType = order;
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getWitcherSortedBy (Advert.AdvertStatus status, SortType sort, IServerAnswerHandler onGetListHandler ){
        METHOD_NAME = GET_WITCHER_ADVERTS_LIST;
        this.sortType = sort;
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getWitcherFilteredByReward ( Advert.AdvertStatus status, FilterType type, int min, int max, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_WITCHER_ADVERTS_LIST;
        this.filterType = type;
        minmax[0] = min;
        minmax[1] = max;
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getWitcherFilteredByReward ( Advert.AdvertStatus status, FilterType type, int min, int max, SortType sort, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_WITCHER_ADVERTS_LIST;
        this.filterType = type;
        this.sortType = sort;
        minmax[0] = min;
        minmax[1] = max;
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getWitcherFilteredByReward ( Advert.AdvertStatus status, FilterType type, int min, int max, SortType sort, OrderType order, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_WITCHER_ADVERTS_LIST;
        this.filterType = type;
        this.sortType = sort;
        this.orderType = order;
        minmax[0] = min;
        minmax[1] = max;
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getWitcherFilteredByLocation ( Advert.AdvertStatus status, FilterType type, SortType sort, String kingdom, String town, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_WITCHER_ADVERTS_LIST;
        this.sortType = sort;
        this.filterType = type;
        loc = new Location(kingdom, town);
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getWitcherFilteredByLocation ( Advert.AdvertStatus status, FilterType type, SortType sort, OrderType order, String kingdom, String town, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_WITCHER_ADVERTS_LIST;
        this.sortType = sort;
        this.filterType = type;
        loc = new Location(kingdom, town);
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getClientSortedBy (Advert.AdvertStatus status, SortType sort, OrderType order, IServerAnswerHandler onGetListHandler ){
        METHOD_NAME = GET_CLIENT_ADVERTS_LIST;
        this.sortType = sort;
        this.orderType = order;
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getClientSortedBy ( Advert.AdvertStatus status, SortType sort, IServerAnswerHandler onGetListHandler ){
        METHOD_NAME = GET_CLIENT_ADVERTS_LIST;
        this.sortType = sort;
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getClientFilteredByReward ( Advert.AdvertStatus status, FilterType type, int min, int max, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_CLIENT_ADVERTS_LIST;
        this.filterType = type;
        minmax[0] = min;
        minmax[1] = max;
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getClientFilteredByReward ( Advert.AdvertStatus status, FilterType type, int min, int max, SortType sort, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_CLIENT_ADVERTS_LIST;
        this.filterType = type;
        this.sortType = sort;
        minmax[0] = min;
        minmax[1] = max;
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getClientFilteredByReward ( Advert.AdvertStatus status, FilterType type, int min, int max, SortType sort, OrderType order, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_CLIENT_ADVERTS_LIST;
        this.filterType = type;
        this.sortType = sort;
        this.orderType = order;
        minmax[0] = min;
        minmax[1] = max;
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getClientFilteredByLocation ( Advert.AdvertStatus status, FilterType type, SortType sort, String kingdom, String town, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_CLIENT_ADVERTS_LIST;
        this.sortType = sort;
        this.filterType = type;
        loc = new Location(kingdom, town);
        this.status = status;
        startRequest(onGetListHandler);
    }

    public void getClientFilteredByLocation ( Advert.AdvertStatus status, FilterType type, SortType sort, OrderType order, String kingdom, String town, IServerAnswerHandler onGetListHandler){
        METHOD_NAME = GET_CLIENT_ADVERTS_LIST;
        this.sortType = sort;
        this.filterType = type;
        loc = new Location(kingdom, town);
        this.status = status;
        startRequest(onGetListHandler);
    }
}
