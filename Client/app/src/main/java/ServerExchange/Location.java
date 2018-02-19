/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerExchange;

/**
 *
 * @author dryush
 */
public class Location {
    String kingdom;
    public String getKingdom(){
        return kingdom;
    }
    
    String city;
    public String getCity(){
        return city;
    }

    public Location(String kingdom, String city){
        this.kingdom = kingdom;
        this.city = city;
    }

}
