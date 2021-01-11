package Server;

import Handler.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MainServer {

    private HttpServer server;

    private void startServer(int port) {

        InetSocketAddress serverAddress = new InetSocketAddress(port);
        try {
            server = HttpServer.create(serverAddress, 10);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);
        registerHandlers(server);
        server.start();
        System.out.println("Server listening on port " + port);
    }

    private void registerHandlers(HttpServer server) {

        server.createContext("/user/login", new LoginHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/", new FileHandler());
    }

    public static void main(String[] args) {
        int portNum = 8080;
        new MainServer().startServer(portNum);
    }



}
