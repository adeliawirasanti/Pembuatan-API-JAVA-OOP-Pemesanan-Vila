package routes;

import controllers.BookingController;
import core.Request;
import core.Response;

public class VillaBookingRoutes {
    public static void handle(Request req, Response res, String path, String method) {
        String[] parts = path.split("/");
        if (parts.length != 4 || !parts[3].equals("bookings")) {
            res.setBody("{\"error\":\"Endpoint bookings tidak valid\"}");
            res.send(404);
            return;
        }

        try {
            int villaId = Integer.parseInt(parts[2]);

            if (method.equals("GET")) {
                BookingController.getBookingsByVillaId(req, res, villaId);
            } else {
                res.setBody("{\"error\":\"Metode tidak diizinkan untuk bookings\"}");
                res.send(405);
            }
        } catch (NumberFormatException e) {
            res.setBody("{\"error\":\"ID villa harus berupa angka\"}");
            res.send(400);
        }
    }
}
