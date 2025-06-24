package routes;

import com.sun.net.httpserver.HttpExchange;
import controllers.CustomerController;
import core.Request;
import core.Response;

public class CustomerBookingRoutes {
    public static void handle(HttpExchange exchange) {
        Request req = new Request(exchange);
        Response res = new Response(exchange);
        String method = req.getRequestMethod();
        String path = req.getPath();
        String[] parts = path.split("/");

        try {
            if (parts.length == 4 && parts[1].equals("customers") && parts[3].equals("bookings")) {
                int customerId = Integer.parseInt(parts[2]);

                switch (method) {
                    case "GET":
                        CustomerController.getBookingsByCustomerId(req, res, customerId);
                        return;
                    case "POST":
                        CustomerController.createBookingForCustomer(req, res, customerId);
                        return;
                    default:
                        res.setBody("{\"error\":\"Metode tidak diizinkan: " + method + "\"}");
                        res.send(405);
                        return;
                }
            }

            res.setBody("{\"error\":\"Endpoint bookings customer tidak ditemukan.\"}");
            res.send(404);

        } catch (NumberFormatException e) {
            res.setBody("{\"error\":\"ID customer harus berupa angka.\"}");
            res.send(400);
        } catch (Exception e) {
            res.setBody("{\"error\":\"Terjadi kesalahan pada server.\"}");
            res.send(500);
        }
    }
}
