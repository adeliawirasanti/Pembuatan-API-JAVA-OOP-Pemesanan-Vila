package core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import routes.VillaRoutes;
import routes.CustomerRoutes;
import routes.CustomerBookingRoutes;
import routes.CustomerReviewRoutes;

import java.net.InetSocketAddress;

public class Server {
    private HttpServer server;

    private class RequestHandler implements HttpHandler {
        public void handle(HttpExchange exchange) {
            String path = exchange.getRequestURI().getPath();

            try {
                // Endpoint nested review: POST /customers/{id}/bookings/{id}/reviews
                if (path.matches("^/customers/\\d+/bookings/\\d+/reviews$")) {
                    CustomerReviewRoutes.handle(exchange);
                }

                // Endpoint nested review (GET): /customers/{id}/reviews
                else if (path.matches("^/customers/\\d+/reviews$")) {
                    CustomerReviewRoutes.handle(exchange);
                }

                // Endpoint nested bookings: /customers/{id}/bookings
                else if (path.matches("^/customers/\\d+/bookings$")) {
                    CustomerBookingRoutes.handle(exchange);
                }

                // Endpoint utama customer
                else if (path.startsWith("/customers")) {
                    CustomerRoutes.handle(exchange);
                }

                // Endpoint utama villa
                else if (path.startsWith("/villas")) {
                    VillaRoutes.handle(exchange);
                }

                // Jika tidak cocok dengan endpoint manapun
                else {
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
        System.out.println("Listening on port: " + port + "...");
    }
}
