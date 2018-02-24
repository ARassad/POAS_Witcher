package ServerExchange.ServerRequests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import ServerExchange.LocationsList;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dryush on 20.02.2018.
 */

public class GetLocationsListRequest extends TokenServerRequest<TreeMap<String, TreeSet<LocationsList.City>>> {
    @Override
    protected RequestType getRequestType() {
        return RequestType.GET;
    }

    private static String METHOD_NAME = "GetTowns";

    @Override
    protected ServerMethod getMethod() {
        return new ServerMethod( METHOD_NAME, new HashMap<String,Object>());
    }

    public class LocationsJsonAnswer extends JsonServerAnswer{

        public long count_kingdoms;
        public class JsonTownsList{
            public class JsonTown{
                public long id_town;
                public String name_town;
            }
            public long count_town;
            public HashMap<String, JsonTown> town;
        }
        public HashMap<String, JsonTownsList> kingdoms;

        @Override
        public TreeMap<String, TreeSet<LocationsList.City>> convert() {
            TreeMap<String, TreeSet<LocationsList.City>> locs = new TreeMap<>();

            for (Map.Entry< String, JsonTownsList> kt: kingdoms.entrySet()){
                JsonTownsList towns = kt.getValue();
                TreeSet<LocationsList.City> citySet = new TreeSet<>();
                for (Map.Entry<String, JsonTownsList.JsonTown> town : towns.town.entrySet()){
                    citySet.add( new LocationsList.City(town.getValue().id_town, town.getValue().name_town));
                }
                locs.put(kt.getKey(), citySet);
            }

            return locs;
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return LocationsJsonAnswer.class;
    }

    public void getLocations(IServerAnswerHandler onAnswer){
        startRequest(onAnswer);
    }
}
