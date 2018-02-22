package ServerExchange;

import android.support.annotation.NonNull;

import java.util.LinkedList;
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

    public TreeSet<String> getKingdoms(){
        TreeSet<String> kingdms = (TreeSet<String>) locs.descendingKeySet();
        return kingdms;
    }
}
