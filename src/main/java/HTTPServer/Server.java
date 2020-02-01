package HTTPServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import commandModule.CommandManager;

public class Server {

    private static Server instance;

    public static Server getInstance(){
        if (Server.instance == null) {
            Server.instance = new Server();
        }
        return Server.instance;

    }

    public void start(){
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.createContext("/test", new MyHandler());
        server.createContext("/addcommand", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String body = new BufferedReader(new InputStreamReader(t.getRequestBody())).lines().collect(Collectors.joining("\n"));
            System.out.println(body);
            String response = "This is the response";
            String context = t.getHttpContext().getPath();
            switch (context){
                case "/addcommand":
                    CommandManager.getInstance().addCommand(body);
            }
            t.getResponseHeaders().add("Access-Control-Allow-Origin","*");
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
