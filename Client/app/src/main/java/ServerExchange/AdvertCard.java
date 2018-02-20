/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerExchange;

import java.util.Date;

/**
 *
 * @author dryush
 */
public class AdvertCard {
 
    Advert.AdvertStatus status;
    Advert.AdvertStatus getStatus(){
        return status;
    }
    
    private long advertId;
    public long getAdvertId() {
        return advertId;
    }

    private String header;
    public String gerAdvertHeader(){
        return header;
    }

    private long authorId;
    public long getAuthorId(){
        return authorId;
    }

    private long executorId;
    public long getExecutorId(){
        return executorId;
    }

    private Date lastStatusUpdate;
    public Date getLastStatusUpdate(){
        return lastStatusUpdate;
    }

    public AdvertCard(long advertId, String header, long authorId, long executorId, Date lastStatusUpdate, Advert.AdvertStatus status){
        this.advertId = advertId;
        this.header = header;
        this.authorId = authorId;
        this.executorId = executorId;
        this.lastStatusUpdate = lastStatusUpdate;
        this.status = status;
    }


}
