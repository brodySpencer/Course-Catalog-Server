package handlers;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.*;

public class Server {

    private Logger logger = Logger.getLogger("Server");
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    private void run(String portNumber) {

        logger.log(Level.FINER, "Server Object Initiated");

        try {
            server = HttpServer.create(new InetSocketAddress(Integer.decode(portNumber)),
                                       MAX_WAITING_CONNECTIONS);

        } catch (IOException exc) {
            logger.log(Level.SEVERE, "Problem with IO in the Server", exc);
            exc.printStackTrace();
        }

        server.setExecutor(null);

        server.createContext("/course", new CourseHandler());
        server.createContext("/catalog", new CatalogHandler());

        server.start();
        logger.finer("ServerStarted");
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run(args[0]);
    }

}
