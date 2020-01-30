package Bot;

import FileLoader.FileLoader;
import TwitchBot.TwitchBot;
import TwitchBot.Channel;
import TwitchBot.User;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;

public class Coonhbot extends TwitchBot {

    public Coonhbot(){
        JSONObject js = new JSONObject(FileLoader.getInstance().readFile("settings.txt"));
        this.setUsername(js.getString("botname"));
        this.setOauth_Key(js.getString("OAuth-Token"));
        this.setClientID(js.getString("Client-ID"));
    }
    @Override
    public void onMessage(User user, Channel channel, String message)
    {
        if (message.toLowerCase().contains("hallo coonhbot")){
            String answer = "Hallo "+ user +"! Schön, dass du hier bist.";
            System.out.println("Hallo "+ user +"! Schön, dass du hier bist.");
            System.out.println("Hier: "+answer);
            sendMessage(answer,channel);

        }
    }

    @Override
    public void onCommand(User user, Channel channel, String command)
    {

    }

    public static String convertFromUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
}
