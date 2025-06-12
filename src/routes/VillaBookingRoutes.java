package routes;

import controllers.BookingController;
import core.Request;
import core.Response;

public class VillaBookingRoutes {
    public static void handle(Request req, Response res, String path, String method) {
        String[] parts = path.split("/");
        if (parts.length != 4 || !parts[3].equals("bookings")) {
            res.setBody("{\"error\":\"Invalid bookings endpoint\"}");
            res.send(404);
            return;
        }

        try {
            int villaId = Integer.parseInt(parts[2]);

            if (method.equals("GET")) {
                BookingController.getBookingsByVillaId(req, res, villaId);
                return;
            }

            res.setBody("{\"error\":\"Bookings endpoint not found\"}");
            res.send(404);
        } catch (NumberFormatException e) {
            res.setBody("{\"error\":\"Invalid ID format\"}");
            res.send(400);
        }
    }
}
