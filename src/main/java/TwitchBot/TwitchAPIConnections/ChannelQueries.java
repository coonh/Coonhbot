package TwitchBot.TwitchAPIConnections;

import TwitchBot.Channel;
import TwitchBot.User;
import com.cavariux.twitchirc.Json.JsonObject;
import TwitchBot.TwitchAPIConnections.UserQueries;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

public class ChannelQueries {
    private static final String V5_API_BASE = "https://api.twitch.tv/kraken/channels/";

    public ChannelQueries() {
    }

    public static boolean isSubscriber(Channel channel, User user, String oAuthToken, String clientId) {
        int[] ids = UserQueries.getUserId(clientId, new String[]{user.toString(), channel.toString().substring(1)});
        int userId = ids[0];
        int channelId = ids[1];
        String requestUri = "https://api.twitch.tv/kraken/channels/" + channelId + "/subscriptions/" + userId;

        try {
            URL request = new URL(requestUri);
            URLConnection conn = request.openConnection();
            conn.setRequestProperty("Authorization", "OAuth " + oAuthToken);
            conn.setRequestProperty("Client-ID", clientId);
            conn.setRequestProperty("Accept", "application/vnd.twitchtv.v5+json");
            Reader response = new InputStreamReader(conn.getInputStream());
            JsonObject responseObj = JsonObject.readFrom(response);
            return responseObj.names().contains("_id");
        } catch (FileNotFoundException var12) {
            return false;
        } catch (IOException var13) {
            var13.printStackTrace();
            return false;
        }
    }
}

