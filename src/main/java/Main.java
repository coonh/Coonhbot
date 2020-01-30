import Bot.Coonhbot;
import FileLoader.FileLoader;
import TwitchBot.Channel;

public class Main {
    public static void main(String[] args){
        System.out.println(FileLoader.getInstance().readFile("settings.txt"));
        Coonhbot bot = new Coonhbot();
        bot.connect();
        Channel channel = bot.joinChannel("Coonh");
        bot.sendMessage("I bims der Coonh-Bot!", channel);
        bot.sendMessage("I bin jetzt connectet!",channel);
        bot.start();
    }
}
