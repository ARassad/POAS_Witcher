/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwitcherclientpart;

/**
 *
 * @author dryush
 */
public class AdvertCard {
 
    Advert.AdvertStatus status;
    Advert.AdvertStatus getStatus(){
        return status;
    }
    
    Advert advert;
    public Advert getAdvert() {
        return advert;
    }

    public AdvertCard(Advert.AdvertStatus status, Advert advert) {
        this.status = status;
        this.advert = advert;
    }
    
}
