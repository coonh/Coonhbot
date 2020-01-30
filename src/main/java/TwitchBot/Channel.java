package TwitchBot;

import TwitchBot.User;
import TwitchBot.TwitchBot;
import com.cavariux.twitchirc.Json.JsonArray;
import com.cavariux.twitchirc.Json.JsonObject;
import com.cavariux.twitchirc.Json.JsonValue;
import TwitchBot.TwitchAPIConnections.ChannelQueries;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Channel {
    private String urln = "http://tmi.twitch.tv/group/user/$channel$/chatters";
    private String channel;
    private TwitchBot bot;
    private static HashMap<String, Channel> channels = new HashMap();

    public static List<Channel> getChannels() {
        return new ArrayList(channels.values());
    }

    private Channel(String channel, TwitchBot bot) {
        this.bot = bot;
        this.channel = channel;
        channels.put(channel, this);
    }

    public static final Channel getChannel(String channel, TwitchBot bot) {
        if (!channel.startsWith("#")) {
            channel = "#" + channel;
        }

        return channels.containsKey(channel) ? (Channel)channels.get(channel) : new Channel(channel, bot);
    }

    public final String toString() {
        return this.channel;
    }

    public final void ban(User user) {
        this.bot.sendMessage("/ban " + user, this);
    }

    public final void timeOut(User user, int time) {
        this.bot.sendMessage("/timeout " + user + " " + time, this);
    }

    public final void unBan(User user) {
        this.bot.sendMessage("/unban " + user, this);
    }

    public final void slowMode(int sec) {
        this.bot.sendMessage("/slow " + sec, this);
    }

    public final void slowOff() {
        this.bot.sendMessage("/slowoff", this);
    }

    public final void subscribersOnly() {
        this.bot.sendMessage("/subscribers", this);
    }

    public final void suscribersOnlyOff() {
        this.bot.sendMessage("/subscribersoff", this);
    }

    public final void clearChat() {
        this.bot.sendMessage("/clear", this);
    }

    public final void r9kbeta() {
        this.bot.sendMessage("/r9kbeta", this);
    }

    public final void r9kbetaOff() {
        this.bot.sendMessage("/r9kbetaoff", this);
    }

    public final void commercial() {
        this.bot.sendMessage("/commercial", this);
    }

    public final void host(Channel channel) {
        this.bot.sendMessage("/host " + channel, this);
    }

    public final void unhost() {
        this.bot.sendMessage("/unhost", this);
    }

    public final List<User> getViewers() {
        try {
            URL url = new URL(this.urln.replace("$channel$", this.channel.toString().substring(1)));
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = "";

            for(String str = ""; (str = br.readLine()) != null; inputLine = inputLine + str) {
            }

            br.close();
            JsonObject jsonObj = JsonObject.readFrom(inputLine);
            JsonArray array = jsonObj.get("chatters").asObject().get("viewers").asArray();
            JsonArray array2 = jsonObj.get("chatters").asObject().get("moderators").asArray();
            List<User> viewers = new ArrayList();
            Iterator var10 = array.iterator();

            JsonValue value;
            while(var10.hasNext()) {
                value = (JsonValue)var10.next();
                viewers.add(User.getUser(value.toString().substring(1, value.toString().length() - 1)));
            }

            var10 = array2.iterator();

            while(var10.hasNext()) {
                value = (JsonValue)var10.next();
                viewers.add(User.getUser(value.toString().substring(1, value.toString().length() - 1)));
            }

            return viewers;
        } catch (IOException var12) {
            var12.printStackTrace();
            return null;
        }
    }

    public final List<User> getMods() {
        try {
            URL url = new URL(this.urln.replace("$channel$", this.channel.toString().substring(1)));
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = "";

            for(String str = ""; (str = br.readLine()) != null; inputLine = inputLine + str) {
            }

            br.close();
            JsonObject jsonObj = JsonObject.readFrom(inputLine);
            JsonArray array2 = jsonObj.get("chatters").asObject().get("moderators").asArray();
            List<User> mods = new ArrayList();
            Iterator var9 = array2.iterator();

            while(var9.hasNext()) {
                JsonValue value = (JsonValue)var9.next();
                mods.add(User.getUser(value.toString().substring(1, value.toString().length() - 1)));
            }

            return mods;
        } catch (IOException var11) {
            var11.printStackTrace();
            return null;
        }
    }

    public final boolean isMod(User user) {
        return this.getMods().contains(user);
    }

    public final boolean isFollowing(User user) {
        try {
            URL url = new URL("https://api.twitch.tv/kraken/users/" + user.toString().toLowerCase() + "/follows/channels/" + this.channel.substring(1).toLowerCase());
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Client-ID", this.bot.getClientID());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject jsonObj = JsonObject.readFrom(br.readLine());
            String str = jsonObj.get("channel").asObject().get("status").toString();
            return str.equals("null");
        } catch (IOException var7) {
            return false;
        }
    }

    public final boolean isSubscribed(User user, String oauth_token) {
        return ChannelQueries.isSubscriber(this, user, oauth_token, this.bot.getClientID());
    }

    public final boolean isLive() {
        return isLive(this, this.bot);
    }

    public final String getGame() {
        if (!this.isLive()) {
            return "";
        } else {
            try {
                URL url = new URL("https://api.twitch.tv/kraken/streams/" + this.channel.substring(1));
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("Client-ID", this.bot.getClientID());
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String strs = br.readLine();
                JsonObject jsonObj = JsonObject.readFrom(strs);
                String str = jsonObj.get("stream").asObject().get("game").asString();
                return str;
            } catch (IOException var7) {
                var7.printStackTrace();
                return null;
            }
        }
    }

    public final String getTitle() {
        if (!this.isLive()) {
            return "";
        } else {
            try {
                URL url = new URL("https://api.twitch.tv/kraken/streams/" + this.channel.substring(1));
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("Client-ID", this.bot.getClientID());
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                JsonObject jsonObj = JsonObject.readFrom(br.readLine());
                String str = jsonObj.get("stream").asObject().get("channel").asObject().get("status").asString();
                return str;
            } catch (IOException var6) {
                return null;
            }
        }
    }

    public final int getViewersNum() {
        if (!this.isLive()) {
            return 0;
        } else {
            try {
                URL url = new URL("https://api.twitch.tv/kraken/streams/" + this.channel.substring(1));
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("Client-ID", this.bot.getClientID());
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                JsonObject jsonObj = JsonObject.readFrom(br.readLine());
                int i = jsonObj.get("stream").asObject().get("viewers").asInt();
                return i;
            } catch (IOException var6) {
                return 0;
            }
        }
    }

    public final String getLanguange() {
        try {
            URL url = new URL("https://api.twitch.tv/kraken/streams/" + this.channel.substring(1));
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Client-ID", this.bot.getClientID());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject jsonObj = JsonObject.readFrom(br.readLine());
            String str = jsonObj.get("stream").asObject().get("channel").asObject().get("language").asString();
            return str;
        } catch (IOException var6) {
            return null;
        }
    }

    public final int getFollowersNum() {
        try {
            URL url = new URL("https://api.twitch.tv/kraken/channels/" + this.channel.substring(1) + "/follows");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Client-ID", this.bot.getClientID());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = "";

            for(String str = ""; (str = br.readLine()) != null; inputLine = inputLine + str) {
            }

            br.close();
            JsonObject jsonObj = JsonObject.readFrom(inputLine);
            int stri = jsonObj.get("_total").asInt();
            return stri;
        } catch (IOException var8) {
            System.out.println("Errur:");
            System.out.println(var8.getMessage());
            return 0;
        }
    }

    public final int getTotalViews() {
        try {
            URL url = new URL("https://api.twitch.tv/kraken/streams/" + this.channel.substring(1));
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Client-ID", this.bot.getClientID());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject jsonObj = JsonObject.readFrom(br.readLine());
            int str = jsonObj.get("stream").asObject().get("channel").asObject().get("views").asInt();
            return str;
        } catch (IOException var6) {
            return 0;
        }
    }

    public static final boolean isLive(Channel channel, TwitchBot bot) {
        try {
            URL url = new URL("https://api.twitch.tv/kraken/streams/" + channel.toString().substring(1));
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Client-ID", bot.getClientID());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject jsonObj = JsonObject.readFrom(br.readLine());
            String str = jsonObj.get("stream").toString();
            return !str.equals("null");
        } catch (IOException var7) {
            return false;
        }
    }
}