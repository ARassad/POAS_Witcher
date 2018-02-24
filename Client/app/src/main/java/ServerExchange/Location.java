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
    private String kingdom;
    public String getKingdom(){
        return kingdom;
    }
    
    private String city;
    public String getCity(){
        return city;
    }

    private Long id;
    public long getId(){
        return id;
    }

    public Location(long id, String kingdom, String city){
        this.id = id;
        this.kingdom = kingdom;
        this.city = city;
    }


    public Location( String kingdom, String city){
        this.kingdom = kingdom;
        this.city = city;
    }

    public String toString(){
        return kingdom + ", " + city;
    }
}
