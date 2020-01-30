package TwitchBot;

import TwitchBot.Channel;
import TwitchBot.TwitchBot;
import java.util.HashMap;

public class User {
    private String user;
    private static HashMap<String, User> users = new HashMap();

    /** @deprecated */
    public User(String user) {
        this.user = user;
        users.put(user, this);
    }

    public static final User getUser(String ign) {
        return users.containsKey(ign) ? (User)users.get(ign) : new User(ign);
    }

    public String toString() {
        return this.user;
    }

    public final boolean isMod(Channel channel) {
        return channel.isMod(this);
    }

    public final void ban(Channel channel) {
        channel.ban(this);
    }

    public final void unBan(Channel channel) {
        channel.unBan(this);
    }

    public final void timeout(Channel channel, int time) {
        channel.timeOut(this, time);
    }

    public final void hostthisUser(Channel channel, TwitchBot bot) {
        channel.host(Channel.getChannel(this.user, bot));
    }

    public final boolean isFollowing(Channel channel) {
        return channel.isFollowing(this);
    }

    public final boolean isSubscribed(Channel channel, String oauth_token) {
        return channel.isSubscribed(this, oauth_token);
    }
}