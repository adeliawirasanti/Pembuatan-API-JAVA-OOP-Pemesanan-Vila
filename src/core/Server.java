package core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import routes.VillaRoutes;
import routes.CustomerRoutes;
import routes.CustomerBookingRoutes;
import routes.CustomerReviewRoutes;
import routes.VoucherRoutes;

import java.net.InetSocketAddress;

public class Server {
    private HttpServer server;

    private class RequestHandler implements HttpHandler {
        public void handle(HttpExchange exchange) {
            String path = exchange.getRequestURI().getPath();

            try {
                if (path.matches("^/customers/\\d+/bookings/\\d+/reviews$")) {
                    CustomerReviewRoutes.handle(exchange);
                } else if (path.matches("^/customers/\\d+/reviews$")) {
                    CustomerReviewRoutes.handle(exchange);
                } else if (path.matches("^/customers/\\d+/bookings$")) {
                    CustomerBookingRoutes.handle(exchange);
                } else if (path.startsWith("/customers")) {
                    CustomerRoutes.handle(exchange);
                } else if (path.startsWith("/villas")) {
                    VillaRoutes.handle(exchange);
                } else if (path.startsWith("/vouchers")) {
                    VoucherRoutes.handle(exchange);
                } else {
                    exchange.sendResponseHeaders(404, 0);
                    exchange.getResponseBody().write("{\"error\":\"Endpoint not found\"}".getBytes());
                    exchange.getResponseBody().close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    exchange.sendResponseHeaders(500, 0);
                    exchange.getResponseBody().write("{\"error\":\"Internal server error\"}".getBytes());
                    exchange.getResponseBody().close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public Server(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 128);
        server.createContext("/", new RequestHandler());
        server.start();
        System.out.printf("Listening on port: %d...\n", port);
    }
}
