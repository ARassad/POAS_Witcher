package ServerExchange;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ServerExchange.ServerRequests.GetLocationsListRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dryush on 20.02.2018.
 */

public class LocationsList {

    public static class City implements Comparable{
        private long id;
        public long getId(){
            return id;
        }
        private String name;
        public String getName(){
            return name;
        }
        public City(long id, String name){
            this.id = id;
            this.name = name;
        }

        @Override
        public int compareTo(@NonNull Object o) {
            City c = (City) o;
            return name.compareTo(c.name);
        }

        public int hashCode(){
            return name.hashCode();
        }

        public String toString(){
            return name;
        }
    }
    private static TreeMap<String, TreeSet<City>> locs = new TreeMap<>();

    static class Filler implements IServerAnswerHandler< TreeMap<String, TreeSet<City>> >{

        @Override
        public void handle(TreeMap<String, TreeSet<City>> answ) {
            locs = answ;

        }

        @Override
        public void errorHandle(String errorMessage) {
            //TODO: Добавить обработчик ошибок
        }

        @Override
        public void exceptionHandle(Exception excp) {
            //TODO: Добавить обработчик ошибок
        }
    }

    static Filler filler = new Filler();
    static GetLocationsListRequest request = new GetLocationsListRequest();
    public static void refillFromServer(){
        request.getLocations(filler);
    }

    private LocationsList(){
        if (locs.size() == 0){
            refillFromServer();
        }
    }

    private static LocationsList inst = null;
    public static LocationsList getInstance(){
        if (inst == null){
            inst = new LocationsList();
        }
        return inst;
    }

    public LinkedList<City> getCities(String kingdom){
        return new LinkedList<>(locs.get(kingdom));
    }

    public LinkedList<String> getCitiesStr(String kingdom){
        LinkedList<String> strs = new LinkedList<>();
        for ( City city : getCities(kingdom)){
            strs.addLast( city.name);
        }
        return strs;
    }

    public Pair<long[] ,String[]> getCitiesIdsAndNames(String kingdom){
        Set<City> cities = locs.get(kingdom);
        int size = cities.size();
        long[] ids = new long[size];
        String[] names = new String[size];
        int i = 0;
        Iterator<City> it = cities.iterator();
        for (; i < size && it.hasNext(); i++){
            City city = it.next();
            ids[i] = city.id;
            names[i] = city.name;
        }
        return new Pair<>(ids, names);
    }

    public Set<String> getKingdoms(){
        Set<String> kingdms = (Set<String>) locs.descendingKeySet();
        return kingdms;
    }

    public Long getId(String kingdom, String city){

        TreeSet<City> cities = this.locs.get(kingdom);
        Boolean isFound = false;
        City curCity = null;
        for (Iterator<City> iCity = cities.iterator(); iCity.hasNext() && !isFound;){
            curCity = iCity.next();
            isFound = curCity.name.equals(city);
        }
        return curCity != null ? curCity.id : null;
    }
}
