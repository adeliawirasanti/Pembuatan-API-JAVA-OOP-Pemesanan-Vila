package routes;

import com.sun.net.httpserver.HttpExchange;
import controllers.CustomerController;
import core.Request;
import core.Response;

public class CustomerReviewRoutes {
    public static void handle(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        String method = req.getRequestMethod();
        String path = req.getPath();

        try {
            switch (method) {
                case "GET":
                    handleGet(req, res, path);
                    break;
                case "POST":
                    handlePost(req, res, path);
                    break;
                default:
                    res.setBody("{\"error\":\"Metode tidak diizinkan: " + method + "\"}");
                    res.send(405);
            }
        } catch (Exception e) {
            res.setBody("{\"error\":\"Terjadi kesalahan pada server: " + e.getMessage() + "\"}");
            res.send(500);
        }
    }

    private static void handleGet(Request req, Response res, String path) {
        // GET /customers/{customerId}/reviews → Ambil semua review milik customer
        if (path.matches("^/customers/\\d+/reviews$")) {
            String[] parts = path.split("/");
            if (parts.length >= 4) {
                int customerId = Integer.parseInt(parts[2]);
                CustomerController.getReviewsByCustomerId(req, res, customerId);
            } else {
                res.setBody("{\"error\":\"Format path tidak valid.\"}");
                res.send(400);
            }
        } else {
            res.setBody("{\"error\":\"Endpoint GET reviews customer tidak ditemukan.\"}");
            res.send(404);
        }
    }

    private static void handlePost(Request req, Response res, String path) {
        // POST /customers/{customerId}/bookings/{bookingId}/reviews → Customer memberi review
        if (path.matches("^/customers/\\d+/bookings/\\d+/reviews$")) {
            String[] parts = path.split("/");
            if (parts.length >= 6) {
                int customerId = Integer.parseInt(parts[2]);
                int bookingId = Integer.parseInt(parts[4]);
                CustomerController.createReviewForBooking(req, res, customerId, bookingId);
            } else {
                res.setBody("{\"error\":\"Format path tidak valid.\"}");
                res.send(400);
            }
        } else {
            res.setBody("{\"error\":\"Endpoint POST reviews customer tidak ditemukan.\"}");
            res.send(404);
        }
    }
}
