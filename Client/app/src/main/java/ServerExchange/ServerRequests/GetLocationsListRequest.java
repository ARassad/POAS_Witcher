package ServerExchange.ServerRequests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import ServerExchange.Location;
import ServerExchange.LocationsList;

/**
 * Created by Dryush on 20.02.2018.
 */

public class GetLocationsListRequest extends TokenServerRequest< HashMap<String, HashSet<LocationsList.City>>> {
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
        public HashMap<String, HashSet<LocationsList.City>> convert() {
            HashMap<String, HashSet<LocationsList.City>> locs = new HashMap<>();

            for (Map.Entry< String, JsonTownsList> kt: kingdoms.entrySet()){
                JsonTownsList towns = kt.getValue();
                HashSet<LocationsList.City> citySet = new HashSet<>();
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
