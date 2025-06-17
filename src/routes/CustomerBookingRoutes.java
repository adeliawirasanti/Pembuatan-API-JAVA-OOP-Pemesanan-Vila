package routes;

import com.sun.net.httpserver.HttpExchange;
import controllers.CustomerController;
import core.Request;
import core.Response;

public class CustomerBookingRoutes {
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
        if (path.matches("/customers/\\d+/bookings")) {
            int customerId = Integer.parseInt(path.split("/")[2]);
            CustomerController.getBookingsByCustomerId(req, res, customerId);
        } else {
            res.setBody("{\"message\":\"Endpoint GET bookings customer tidak ditemukan.\"}");
            res.send(404);
        }
    }

    private static void handlePost(Request req, Response res, String path) {
        if (path.matches("/customers/\\d+/bookings")) {
            int customerId = Integer.parseInt(path.split("/")[2]);
            CustomerController.createBookingForCustomer(req, res, customerId);
        } else {
            res.setBody("{\"error\":\"Endpoint POST tidak ditemukan\"}");
            res.send(404);
        }
    }
}

