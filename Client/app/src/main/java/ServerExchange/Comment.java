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
public class Comment {

    private long id;

    private String text;
    public String getText(){
        return text;
    }
    
    //Profile author;
    private long id_author;
    public long getAuthorId(){ return id_author; }
    
    private Date dateOfCreate;
    public Date getDateOfCreate(){
        return dateOfCreate;
    }

    public Comment(long id, String text, long id_author, Date dateOfCreate) {
        this.id = id;
        this.text = text;
        //this.author = author;
        this.id_author = id_author;
        this.dateOfCreate = dateOfCreate;
    }
    
}
