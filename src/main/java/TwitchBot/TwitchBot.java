package TwitchBot;

import TwitchBot.Channel;
import TwitchBot.User;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TwitchBot{
    private String whispers_ip = "";
    private int whispers_port = 443;
    private boolean wen = true;
    private String user;
    private String oauth_key;
    private BufferedWriter writer;
    private BufferedReader reader;
    private ArrayList<String> channels = new ArrayList();
    private String version = "v1.0-Beta";
    private boolean stopped = true;
    private String commandTrigger = "!";
    private String clientID = "";
    private static final Logger LOGGER = Logger.getLogger(TwitchBot.class.getName());

    public final List<Channel> getChannels() {
        return Channel.getChannels();
    }

    public TwitchBot() {
    }

    public void connect() {
        this.wen = false;
        this.connect("irc.twitch.tv", 6667);
    }

    public void connect(String ip, int port) {
        if (!this.isRunning()) {
            try {
                label51: {
                    if (this.user != null && this.user != "") {
                        if (this.oauth_key != null && this.oauth_key != "") {
                            Socket socket = new Socket(ip, port);
                            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            this.writer.write("PASS " + this.oauth_key + "\r\n");
                            this.writer.write("NICK " + this.user + "\r\n");
                            this.writer.write("USER " + this.getVersion() + " \r\n");
                            this.writer.write("CAP REQ :twitch.tv/commands \r\n");
                            this.writer.write("CAP REQ :twitch.tv/membership \r\n");
                            this.writer.flush();
                            String line = "";

                            while((line = this.reader.readLine()) != null) {
                                if (line.indexOf("004") >= 0) {
                                    LOGGER.log(Level.INFO, "Connected >> " + this.user + " ~ irc.twitch.tv");
                                    return;
                                }

                                LOGGER.log(Level.INFO, line);
                            }
                            break label51;
                        }

                        LOGGER.log(Level.SEVERE, "Please select a valid Oauth_Key");
                        System.exit(2);
                        return;
                    }

                    LOGGER.log(Level.SEVERE, "Please select a valid Username");
                    System.exit(1);
                    return;
                }
            } catch (IOException var5) {
                var5.printStackTrace();
            }

        }
    }

    protected void onSub(User user, Channel channel, String message) {
    }

    public final void setUsername(String username) {
        this.user = username;
    }

    public final void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public final String getClientID() {
        if (this.clientID != null) {
            return this.clientID;
        } else {
            LOGGER.log(Level.INFO, "You need to give a clientID to use the TwitchAPI");
            return "";
        }
    }

    public final void setOauth_Key(String oauth_key) {
        this.oauth_key = oauth_key;
    }

    protected void onMessage(User user, Channel channel, String message) {
    }

    protected void onCommand(User user, Channel channel, String command) {
    }

    protected void onHost(User hoster, Channel hosted) {
    }

    public void sendRawMessage(Object message) {
        try {
            this.writer.write(message + " \r\n");
            this.writer.flush();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        LOGGER.log(Level.INFO, message.toString());
    }

    public void sendMessage(Object message, Channel channel) {
        try {
            this.writer.write("PRIVMSG " + channel + " :" + message.toString() + "\r\n");
            this.writer.flush();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        LOGGER.log(Level.INFO, "> MSG " + channel + " : " + message.toString());
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public void setNick(String newNick) {
        try {
            this.writer.write("NICK " + newNick + "\r\n");
        } catch (IOException var3) {
        }

    }

    public final Channel joinChannel(String channel) {
        Channel cnl = Channel.getChannel(channel.toLowerCase(), this);
        this.sendRawMessage("JOIN " + cnl.toString().toLowerCase() + "\r\n");
        this.channels.add(cnl.toString());
        LOGGER.log(Level.INFO, "> JOIN " + cnl);
        return cnl;
    }

    public final void partChannel(String channel) {
        Channel cnl = Channel.getChannel(channel.toLowerCase(), this);
        this.sendRawMessage("PART " + cnl);
        this.channels.remove(cnl);
        LOGGER.log(Level.INFO, "> PART " + channel);
    }

    public final BufferedWriter getWriter() {
        return this.writer;
    }

    public final void start() {
        if (!this.isRunning()) {
            String line = "";
            this.stopped = false;

            try {
                while((line = this.reader.readLine()) != null && !this.stopped) {
                    if (line.toLowerCase().startsWith("ping")) {
                        LOGGER.log(Level.INFO, "> PING");
                        LOGGER.log(Level.INFO, "< PONG " + line.substring(5));
                        this.writer.write("PONG " + line.substring(5) + "\r\n");
                        this.writer.flush();
                    } else {
                        String[] p;
                        User wsp_user;
                        if (line.contains("PRIVMSG")) {
                            p = line.split("!");
                            wsp_user = User.getUser(p[0].substring(1, p[0].length()));
                            p = line.split(" ");
                            Channel msg_channel = Channel.getChannel(p[2], this);
                            String msg_msg = line.substring(p[0].length() + p[1].length() + p[2].length() + 4, line.length());
                            LOGGER.log(Level.INFO, "> " + msg_channel + " | " + wsp_user + " >> " + msg_msg);
                            if (msg_msg.startsWith(this.commandTrigger)) {
                                this.onCommand(wsp_user, msg_channel, msg_msg.substring(1));
                            }

                            if (wsp_user.toString().equals("jtv") && msg_msg.contains("now hosting")) {
                                String hoster = msg_msg.split(" ")[0];
                                this.onHost(User.getUser(hoster), msg_channel);
                            }

                            this.onMessage(wsp_user, msg_channel, msg_msg);
                        } else {
                            String[] pd;
                            if (line.contains(" JOIN ")) {
                                p = line.split(" ");
                                pd = line.split("!");
                                if (p[1].equals("JOIN")) {
                                    this.userJoins(User.getUser(pd[0].substring(1)), Channel.getChannel(p[2], this));
                                }
                            } else if (line.contains(" PART ")) {
                                p = line.split(" ");
                                pd = line.split("!");
                                if (p[1].equals("PART")) {
                                    this.userParts(User.getUser(pd[0].substring(1)), Channel.getChannel(p[2], this));
                                }
                            } else if (line.contains(" WHISPER ")) {
                                p = line.split(":");
                                wsp_user = User.getUser(p[1].split("!")[0]);
                                String message = p[2];
                                this.onWhisper(wsp_user, message);
                            } else if (!line.startsWith(":tmi.twitch.tv ROOMSTATE")) {
                                if (line.startsWith(":tmi.twitch.tv NOTICE")) {
                                    p = line.split(" ");
                                    if (line.contains("This room is now in slow mode. You may send messages every")) {
                                        LOGGER.log(Level.INFO, "> Chat is now in slow mode. You can send messages every " + p[15] + " sec(s)!");
                                    } else if (line.contains("subscribers-only mode")) {
                                        if (line.contains("This room is no longer")) {
                                            LOGGER.log(Level.INFO, "> The room is no longer Subscribers Only!");
                                        } else {
                                            LOGGER.log(Level.INFO, "> The room has been set to Subscribers Only!");
                                        }
                                    } else {
                                        LOGGER.log(Level.INFO, line);
                                    }
                                } else if (line.startsWith(":jtv MODE ")) {
                                    p = line.split(" ");
                                    if (p[3].equals("+o")) {
                                        LOGGER.log(Level.INFO, "> +o " + p[4]);
                                    } else {
                                        LOGGER.log(Level.INFO, "> -o " + p[4]);
                                    }
                                } else if (line.toLowerCase().contains("disconnected")) {
                                    LOGGER.log(Level.INFO, line);
                                    this.connect();
                                } else {
                                    LOGGER.log(Level.INFO, "> " + line);
                                }
                            }
                        }
                    }
                }
            } catch (IOException var7) {
                var7.printStackTrace();
            }

        }
    }

    public void stop() {
        this.stopped = true;
        this.sendRawMessage("Stopping");
    }

    public void stopWithMessage(String message) {
        this.stopped = true;
        Iterator var2 = this.channels.iterator();

        while(var2.hasNext()) {
            String cnl = (String)var2.next();
            this.sendMessage(message, Channel.getChannel(cnl, this));
        }

    }

    public boolean isRunning() {
        return !this.stopped;
    }

    protected void userJoins(User user, Channel channel) {
    }

    protected void userParts(User user, Channel channel) {
    }

    public void whisper(User user, String message) {
        if (!this.channels.isEmpty()) {
            this.sendMessage("/w " + user + " " + message, Channel.getChannel((String)this.channels.get(0), this));
        } else if (!this.wen) {
            LOGGER.log(Level.INFO, "You have to be either connected to at least one channel or join another Server to be able to whisper!");
        } else {
            this.sendRawMessage("PRIVMSG #jtv :/w " + user + " " + message);
        }

    }

    protected void onWhisper(User user, String message) {
    }

    protected final void setWhispersIp(String ip) {
        if (!ip.contains(":")) {
            LOGGER.log(Level.INFO, "Invaid ip!");
        } else {
            String[] args = ip.split(":");
            this.whispers_ip = args[0];
            this.whispers_port = Integer.parseInt(args[1]);
        }
    }

    protected final void setWhispersIp(String ip, int port) {
        this.whispers_ip = ip;
        this.whispers_port = port;
    }

    public void setCommandTrigger(String trigger) {
        this.commandTrigger = trigger;
    }

    public final String getVersion() {
        return "TwitchIRC " + this.version;
    }
}
