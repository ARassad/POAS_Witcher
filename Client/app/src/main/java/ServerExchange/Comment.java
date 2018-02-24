/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerExchange;

import android.graphics.Bitmap;

import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author dryush
 */
public class Comment implements Comparable<Comment>{

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

    private Bitmap authorAvatar;
    public Bitmap getAuthorAvatar(){
        return authorAvatar;
    }

    private String authorName;
    public String getAuthorNmae(){
        return authorName;
    }


    public Comment(long id, String text, long id_author, String authorName, Date dateOfCreate, Bitmap photo) {
        this.id = id;
        this.text = text;
        this.authorAvatar = photo;
        this.authorName = authorName;
        //this.author = author;
        this.id_author = id_author;
        this.dateOfCreate = dateOfCreate;
    }

    public Comment( String text, long id_author, String authorName, Date dateOfCreate, Bitmap photo) {
        this.id = -1;
        this.text = text;
        this.authorAvatar = photo;
        this.authorName = authorName;
        //this.author = author;
        this.id_author = id_author;
        this.dateOfCreate = dateOfCreate;
    }

    public int compareTo(Comment com){
        return this.dateOfCreate.compareTo(com.getDateOfCreate());
    }

    public static Comparator<Comment> DateComparator = new Comparator <Comment>(){

        public int compare(Comment com1, Comment com2){
            return com1.dateOfCreate.compareTo(com2.getDateOfCreate());
        }
    };

}
