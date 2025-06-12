package core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import routes.VillaRoutes;

import java.net.InetSocketAddress;

public class Server {
    private HttpServer server;

    private class RequestHandler implements HttpHandler {
        public void handle(HttpExchange exchange) {
            VillaRoutes.handle(exchange);
        }
    }

    public Server(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 128);
        server.createContext("/", new RequestHandler());
        server.start();
    }
}