/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwitcherclientpart;

import java.util.Date;

/**
 *
 * @author dryush
 */
public class Comment {
    String text;
    String getText(){
        return text;
    }
    
    Profile author;
    Profile getAuthor(){
        return author;
    }
    
    Date dateOfCreate;
    Date getDateOfCreate(){
        return dateOfCreate;
    }

    public Comment(String text, Profile author, Date dateOfCreate) {
        this.text = text;
        this.author = author;
        this.dateOfCreate = dateOfCreate;
    }
    
}
