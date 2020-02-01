package Bot;

import FileLoader.FileLoader;
import TwitchBot.TwitchBot;
import TwitchBot.Channel;
import TwitchBot.User;
import org.json.JSONObject;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Coonhbot extends TwitchBot {

    private Channel channel;

    public Coonhbot(){
        this.setUsername(Settings.getInstance().getSetting("botname"));
        this.setOauth_Key(Settings.getInstance().getSetting("OAuth-Token"));
        this.setClientID(Settings.getInstance().getSetting("Client-ID"));

        this.connect();

        channel = this.joinChannel(Settings.getInstance().getSetting("streamername"));
    }
    @Override
    public void onMessage(User user, Channel channel, String message)
    {
        if (message.toLowerCase().contains("hallo coonhbot")){
            String answer = "Hallo "+ user +"! Schön, dass du hier bist.";
            sendMessage(answer,channel);

        }
    }

    /**
     * Gets the current viewers every time interval and saves the new currency amounts
     */
    public void saveCurrency(){
        JSONObject currencyData = new JSONObject(FileLoader.getInstance().readFile("currency.txt"));
        for(User user : channel.getViewers()){
            if(!currencyData.has(user.toString())){
                currencyData.put(user.toString(),10);
            }else{
                currencyData.put(user.toString(),currencyData.getInt(user.toString())+10+"\n");
            }
        }
        FileLoader.getInstance().writeFile("currency.txt",currencyData.toString());
    }

    @Override
    public void onCommand(User user, Channel channel, String command)
    {
        //TODO Handle events that get triggered by ! in chat
    }
}
