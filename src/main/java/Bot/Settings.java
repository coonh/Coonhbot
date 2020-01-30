package Bot;

import FileLoader.FileLoader;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class Settings {

    private static Settings instance;
    private HashMap<String,String> settings = new HashMap();

    private Settings(){
        System.out.println("::::: Setting will be loaded :::::");
        loadSettings();
        System.out.println("::::: Finished loading "+settings.size()+" Settings :::::");
    }

    private void loadSettings() {
        JSONObject js = new JSONObject(FileLoader.getInstance().readFile("settings.txt"));
        if(js.isEmpty()){
            FileLoader.getInstance().writeFile("settings.txt","{\n" +
                    "    botname:\"<name_of_ur_bot>\",\n" +
                    "    streamername:\"<name_of_ur_streamacc>\",\n" +
                    "    OAuth-Token:\"<oauth_token from https://twitchapps.com/tmi/>\",\n" +
                    "    Client-ID:\"<your_client_id see https://dev.twitch.tv/docs/v5>\"\n" +
                    "}");
            System.err.println("Change the settings in Data/settings.txt");
        }
        for (Iterator<String> key = js.keys(); key.hasNext(); ) {
            String actKey = key.next();
            settings.put(actKey,js.getString(actKey));
            System.out.println("Key: "+actKey+" Value: "+settings.get(actKey));
    }
    }

    public void saveSettings(){
        //TODO save setting everytime the bot cloeses or settingsg are changed
    }

    public static Settings getInstance() {
        if (Settings.instance == null) {
            Settings.instance = new Settings();
        }
        return Settings.instance;
    }

    public String getSetting(String settingKey){
        return settings.get(settingKey);
    }

}
