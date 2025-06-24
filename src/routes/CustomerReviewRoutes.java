package routes;

import com.sun.net.httpserver.HttpExchange;
import controllers.CustomerController;
import controllers.ReviewController;
import core.Request;
import core.Response;

public class CustomerReviewRoutes {
    public static void handle(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        String method = req.getRequestMethod();
        String path = req.getPath();
        String[] parts = path.split("/", -1); // penting untuk trailing slash

        try {
            switch (method) {
                case "GET":
                    handleGet(req, res, parts);
                    break;
                case "POST":
                    handlePost(req, res, parts);
                    break;
                default:
                    res.setBody("{\"error\":\"Methods are not allowed: " + method + "\"}");
                    res.send(405);
            }
        } catch (Exception e) {
            res.setBody("{\"error\":\"A server error occurred: " + e.getMessage() + "\"}");
            res.send(500);
        }
    }

    private static void handleGet(Request req, Response res, String[] parts) {
        if (parts.length == 4 && parts[1].equals("customers") && parts[3].equals("reviews")) {
            try {
                int customerId = Integer.parseInt(parts[2]);
                CustomerController.getReviewsByCustomerId(req, res, customerId);
            } catch (NumberFormatException e) {
                res.setBody("{\"error\":\"Invalid ID format.\"}");
                res.send(400);
            }
        } else {
            res.setBody("{\"error\":\"The GET reviews customer endpoint was not found.\"}");
            res.send(404);
        }
    }

    private static void handlePost(Request req, Response res, String[] parts) {
        if (parts.length == 6 && parts[1].equals("customers") && parts[3].equals("bookings") && parts[5].equals("reviews")) {
            try {
                int customerId = Integer.parseInt(parts[2]);
                int bookingId = Integer.parseInt(parts[4]);
                ReviewController.createReviewForBooking(req, res, customerId, bookingId);
            } catch (NumberFormatException e) {
                res.setBody("{\"error\":\"Invalid ID format.\"}");
                res.send(400);
            }
        } else {
            res.setBody("{\"error\":\"The POST reviews customer endpoint was not found.\"}");
            res.send(404);
        }
    }
}
