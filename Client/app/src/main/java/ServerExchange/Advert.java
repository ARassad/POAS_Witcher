/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerExchange;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author dryush
 */
public class Advert implements ICommented {
    
    private long id;
    public long getId(){
        return id;
    }
    
    private String name;
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    
    private String info;
    public String getInfo(){
        return info;
    }
    public void setInfo(String info){
        this.info = info;
    }
    
    public final static int MAX_IMAGES = 10;
    private LinkedList <Bitmap> images;
    public ArrayList<Bitmap> getImages(){
        ArrayList<Bitmap> imgs = new ArrayList<>();
        for (Bitmap img : this.images) {
            imgs.add( (Bitmap) img.copy(img.getConfig(), false) );
        }
        return imgs;
    }
    public ArrayList<Bitmap> addImage(Bitmap img, int index){
        if (images.size() >= MAX_IMAGES){
            throw new RuntimeException("Не пихайте больше " + MAX_IMAGES + " картинок");
        }

        images.add(index, img.copy(img.getConfig(), false));

        return getImages();
    }

    public ArrayList<Bitmap> addImage(Bitmap img){

        return addImage(img, images.size());
        //this.images.addLast(img);
    }
    
    public void removeImage(int index){
    
    }
    
    private Location location;
    public Location getLocation(){
        return location;
    }
    public void setLocation(Location loc){
        this.location = loc;
    }
    public String getCity() {return this.location.getCity();}
    public String getKingdom() {return this.location.getKingdom();}

    Integer reward;
    public Integer getReward(){
        return reward;
    }
    public void setReward(int reward){
        this.reward = reward;
    }
    
    //Profile author;
    private long id_author;
    private String authorName;
    private Bitmap authorPhoto;
    public long getAuthorId(){
        return id_author;
    }
    public String getAuthorName() {return authorName; }
    public Bitmap getAuthorPhoto() {return authorPhoto; }

    private LinkedList<Long> idSubscribedWitchersList;
    public ArrayList<Long> getIdSubscribedWitchersList(){
        return new ArrayList<>(idSubscribedWitchersList);
    }
    public ArrayList<Long> addIdSubscribedWitcher( long id_witcher){
        idSubscribedWitchersList.add(id_witcher);
        return getIdSubscribedWitchersList();
    }
    
    //Profile executor;
    private Long id_executor;
    private String executorName;
    public void setExecutorId( long id_witcher){
        id_executor = id_witcher;
    }
    public Long getExecutorId(){
        return id_executor;
    }
    public String getExecutorName() {return executorName; }

    public static enum AdvertStatus{
        FREE,
        ASSIGNED_WITCHER,
        IN_PROCESS,
        COMPLETED,
        WITCHER_LEAVE,
        CUSTOMER_REFUSED;
        public static AdvertStatus fromInt(int i){
            switch (i){
                case 0: return FREE;
                case 1: return ASSIGNED_WITCHER;
                case 2: return IN_PROCESS;
                case 3: return COMPLETED;
                case 4: return WITCHER_LEAVE;
                case 5: return CUSTOMER_REFUSED;
                default: throw new RuntimeException("Number must be between 0 and 5 [0;5], not " + i);
            }
        }
        public int toInt(){
            switch (this){
                case FREE:              return 0;
                case ASSIGNED_WITCHER:  return 1;
                case IN_PROCESS:        return 2;
                case COMPLETED:         return 3;
                case WITCHER_LEAVE:     return 4;
                case CUSTOMER_REFUSED:  return 5;
                default: throw new RuntimeException("some trash, wo don`t know how you do it");
            }
        }

        public String toRuString() {
            switch (this) {
                case FREE:
                    return "Доступно";
                case ASSIGNED_WITCHER:
                    return "Ведьмак выбран";
                case IN_PROCESS:
                    return "Выполняется";
                case COMPLETED:
                    return "Выполнено";
                case WITCHER_LEAVE:
                    return "Ведьмак отказался";
                case CUSTOMER_REFUSED:
                    return "Заказчиак отказался";
                default:
                    throw new RuntimeException("some trash, wo don`t know how you do it");
            }
        }
    };
    
    private AdvertStatus status;
    public AdvertStatus getStatus(){
        return status;
    }
    
    private Date dateOfCreate;
    public Date getDateOfCreate() {
        return dateOfCreate;
    }

    public Advert(String name, String info, Location location, int reward, long id_author,
                  AdvertStatus status, Date dateOfCreate) {
        this.name = name;
        this.info = info;
        this.location = location;
        this.reward = reward;
        this.id_author = id_author;
        this.status = status;
        this.dateOfCreate = dateOfCreate;
    }
    
    private CommentsContainer comments;
    @Override
    public ArrayList<Comment> getComments() {
        return comments.getComments();
    }
    @Override
    public ArrayList<Comment> addComment(Comment com) {
        return comments.addComment(com);
    }

    public Advert(long id, String name, String info, LinkedList<Bitmap> images, Location location, int reward,
                  long id_author, String authorName, Bitmap authorPhoto ,LinkedList<Long> idSubscribedWitchersList,
                  Long id_executor, String executorName ,AdvertStatus status,
                  Date dateOfCreate, CommentsContainer comments) {

        this.id = id;
        this.name = name;
        this.info = info;
        this.images = images;
        this.location = location;
        this.reward = reward;
        this.id_author = id_author;
        this.authorName = authorName;
        this.authorPhoto = authorPhoto;
        this.idSubscribedWitchersList = idSubscribedWitchersList;
        this.id_executor = id_executor;
        this.executorName = executorName;
        this.status = status;
        this.dateOfCreate = dateOfCreate;
        this.comments = comments;
    }
    
    
    @Override
    public Object clone(){
        
        return new Advert(id, name, info, images, location, reward, id_author, authorName,authorPhoto,idSubscribedWitchersList,
            id_executor,executorName ,status, dateOfCreate, comments);
        
    }
}
