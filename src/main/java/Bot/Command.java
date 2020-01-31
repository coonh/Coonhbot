package Bot;

public class Command {
    public String name;
    public   int cost;
    public boolean enabled;
    public int global_cooldown; //in minutes
    public   int user_cooldown; //TODO will be added after global cooldown works
    public String response;

    /**
     * Later we will need mor options for this class, so also sound files f.ex.
     *
     * Also special commands that handle scriptlike behavior or needs extra logic need to be possible
     */

    public Command(String name,int cost,boolean enabled, int global_cooldown, int user_cooldown, String response){
        this.name=name;
        this.cost=cost;
        this.enabled=enabled;
        this.global_cooldown=global_cooldown;
        this.user_cooldown=user_cooldown;
        this.response=response;
    }


}
