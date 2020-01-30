import Bot.Coonhbot;
import Bot.Settings;
import TwitchBot.TwitchBot;

import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args){
        Settings.getInstance();
        Coonhbot bot = new Coonhbot();
        consoleScanner(bot);
        bot.saveCurrency();
        bot.start();
    }

    private static void consoleScanner(TwitchBot bot){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()->{
            String line;
            Scanner in = new Scanner(System.in);
            while((line=in.next()) != null){
                switch (line){
                    case "quit":
                    case "stop":
                    case "exit":
                        bot.stopWithMessage("Coonhbot is shutting down now! Se ya later alligator!");
                        System.exit(0);
                    default:
                        System.out.println("Don't know: " + line);
                }
            }
        });
    }
}

