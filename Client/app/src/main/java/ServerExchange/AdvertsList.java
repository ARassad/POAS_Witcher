/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerExchange;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author Acer
 */
public class AdvertsList {
    
    private LinkedList<Advert> advertsList; 
    
    public Advert getAdvert(long id){
        boolean isFound = false;
        ListIterator <Advert> iAdvert = advertsList.listIterator();
        for (; iAdvert.hasNext() && !isFound; ){
            isFound = iAdvert.next().getId() == id;
        }
        
        return iAdvert.previous();
    }
    
    public void addAdvert(Advert _advert){
        advertsList.add(_advert);
    }   
    
    public static enum SortType{
        BY_DATE,
        BY_REWARD,
        BY_NAME,
        BY_LOCATION
    }

    public static enum FilterType{
        BY_REWARD,
        BY_LOCATION
    }

    // Cортировка
    public ArrayList<Advert> getSortedBy(SortType type){
        ArrayList<Advert> sortedAdverts = new ArrayList();
        
        return sortedAdverts;
    }
    
    // Фильтрация
    
    // Фильтр - свои объявления
    public ArrayList<Advert> getUserAdverts(SortType type, Profile profile){
       LinkedList <Advert> userAdverts = new LinkedList<>();
       if (profile.getType() == Profile.ProfileType.CUSTOMER){
            for (Advert advert : userAdverts){
                if (advert.getAuthor().equals(profile)){
                    userAdverts.addLast((Advert) advert.clone());
                }
            }
       }
       else {
           for (Advert advert : userAdverts){
                if (advert.getSubscribedWitchers().contains(profile)){
                    userAdverts.addLast((Advert) advert.clone());
                }
            }
       }
       
       return new ArrayList<>(userAdverts);
    }
    
    // Фильтр - статус объявления
    public ArrayList<Advert> getFilteredByStatus(Advert.AdvertStatus status){
        ArrayList<Advert> userAdverts = new ArrayList();
       for (Advert advert : userAdverts){
           if (advert.getStatus().equals(status)){
               userAdverts.add((Advert)advert.clone());
           }
       }
       return userAdverts;
    }
    
    
    
}
