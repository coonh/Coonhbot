package Bot;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import FileLoader.FileLoader;

public class CommandManager {

    private static CommandManager instance;
    private HashMap<String,Command> commands = new HashMap();

    private CommandManager(){
        System.out.println("::::: Loading Commands :::::");
        loadCommands();
        System.out.println("::::: Finished loading "+commands.size()+" commands! :::::");
    }

    private void loadCommands() {
        JSONObject js = new JSONObject(FileLoader.getInstance().readFile("commands.txt"));
        if(js.isEmpty()){
            FileLoader.getInstance().writeFile("commands.txt","{}");
            return;
        }
        for (Iterator<String> key = js.keys(); key.hasNext(); ) {
            String actKey = key.next();
            commands.put(actKey, JSONtoCommand(js.getJSONObject(actKey)));
            System.out.println("Commandname: " + actKey);
        }
    }

    public void saveCommands(){
        JSONObject allCommandsJSON = new JSONObject();
        for(Command c : commands.values()){
            JSONObject commandJson = commandToJSON(c);
            allCommandsJSON.put(commandJson.getString("name"),commandJson);
        }
        FileLoader.getInstance().writeFile("commands.txt",allCommandsJSON.toString());
    }

    private Command JSONtoCommand(JSONObject jsoncommand) {
        return new Command(jsoncommand.getString("name"),jsoncommand.getInt("cost"),
                jsoncommand.getBoolean("enabled"),jsoncommand.getInt("global_cooldown"),
                jsoncommand.getInt("user_cooldown"),jsoncommand.getString("response"));
    }

    private JSONObject commandToJSON(Command c){
        JSONObject commandAttributes = new JSONObject();
        commandAttributes.put("name",c.name).put("cost",c.cost).put("enabled", c.enabled)
                .put("global_cooldown",c.global_cooldown).put("user_cooldown",c.user_cooldown)
                .put("response",c.response);
        return commandAttributes;
    }

    public void addCommand(Command c){
        if(commands.containsKey(c.name)) commands.put(c.name,c);
        else{
            System.err.println("Command \""+c.name+"\" found" );
        }
    }

    public void removeCommand(String commandName){
        if(commands.containsKey(commandName)) commands.remove(commandName);
        else{
            System.err.println("No command with name \""+commandName+"\" found" );
        }
    }

    public static CommandManager getInstance() {
        if (CommandManager.instance == null) {
            CommandManager.instance = new CommandManager();
        }
        return CommandManager.instance;
    }

}
