package ServerExchange;

/**
 * Created by Dryush on 20.02.2018.
 */

public class ProfilePart {
    private long id;
    public long getId(){return id;};
    private String name;
    public String getName(){return name;}
    public ProfilePart(long id, String name){
        this.id = id;
        this.name = name;
    }
}
