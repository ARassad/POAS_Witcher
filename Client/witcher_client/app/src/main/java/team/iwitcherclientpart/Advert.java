/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwitcherclientpart;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author dryush
 */
public class Advert implements ICommented{
    
    long id;
    public long getId(){
        return id;
    }
    
    String name;
    public String getName(){
        return name;
    }
    void setName(String name){
        this.name = name;
    }
    
    String info;
    public String getInfo(){
        return info;
    }
    void setInfo(String info){
        this.info = info;
    }
    
    public final static int MAX_IMAGES = 10;
    LinkedList <BufferedImage> images;
    public ArrayList<BufferedImage> getImages(){
        
        throw new RuntimeException("Пока не запилили _/\\о/\\_");
        //return null;
        //return new ArrayList<>(images)
        //TODO clone image
    }
    public void addImage(BufferedImage img){
        if (images.size() >= MAX_IMAGES){
            throw new RuntimeException("Не пихайте больше " + MAX_IMAGES + " картинок");
        }
        throw new RuntimeException("Пока не запилили _/\\о/\\_");
        
        //TODO clone image
        //this.images.addLast(img);
    }
    
    void removeImage(int index){
    
    }
    
    Location location;
    public Location getLovation(){
        return location;
    }
    void setLocation(Location loc){
        this.location = loc;
    }
    
    int reward;
    public int getReward(){
        return reward;
    }
    public void setReward(int reward){
        this.reward = reward;
    }
    
    Profile author;
    public Profile getAuthor(){
        return author;
    }
    
    LinkedList<Profile> subscribedWitchers;
    public ArrayList<Profile> getSubscribedWitchers(){
        return new ArrayList<>(subscribedWitchers);
    }
    public ArrayList<Profile> addSubscribedWitcher( Profile witcher){
    //    if ( witcher.getType() != Profile.WITCHER){
    //        throw RuntimeException("Попытка подписать на объявление не Ведьмака") 
    //    }
        return getSubscribedWitchers();
    }
    
    Profile executor;
    public void setExecutor( Profile witcher){
    //    if ( witcher.getType() != Profile.WITCHER){
    //        throw RuntimeException("Попытка назначить исполнителем не Ведьмака") 
    //    }
        if ( ! subscribedWitchers.contains(witcher)){
            throw new RuntimeException("Попытка назначить на исполнение Ведьмака, не отликнувшегося на это объявлние");
        }
        
        executor = witcher;
        
    }

    
    public static enum AdvertStatus{
        FREE,
        ASSIGNED_WITCHER,
        IN_PROCESS,
        COMPLETED,
        WITCHER_LEAVE,
        CUSTOMER_REFUSED;
    };
    
    AdvertStatus status;
    public AdvertStatus getStatus(){
        return status;
    }
    
    Date dateOfCreate;
    public Date getDateOfCreate() {
        return dateOfCreate;
    }

    public Advert(String name, String info, Location location, int reward, Profile author, Profile executor, AdvertStatus status, Date dateOfCreate) {
        this.name = name;
        this.info = info;
        this.location = location;
        this.reward = reward;
        this.author = author;
        this.status = AdvertStatus.FREE;
        this.dateOfCreate = dateOfCreate;
    }
    
    CommentsContainer comments;
    @Override
    public ArrayList<Comment> getComments() {
        return comments.getComments();
    }
    @Override
    public ArrayList<Comment> addComment(Comment com) {
        return comments.addComment(com);
    }

    Advert(long id, String name, String info, LinkedList<BufferedImage> images, Location location, int reward, Profile author, LinkedList<Profile> subscribedWitchers, Profile executor, AdvertStatus status, Date dateOfCreate, CommentsContainer comments) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.images = images;
        this.location = location;
        this.reward = reward;
        this.author = author;
        this.subscribedWitchers = subscribedWitchers;
        this.executor = executor;
        this.status = status;
        this.dateOfCreate = dateOfCreate;
        this.comments = comments;
    }
    
    
    @Override
    public Object clone(){
        
    return new Advert(id, name, info, images, location, reward, author, subscribedWitchers, executor, status, dateOfCreate, comments);
        
    }
}
