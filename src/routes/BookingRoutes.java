package routes;

import com.sun.net.httpserver.HttpExchange;
import controllers.BookingController;
import core.Request;
import core.Response;

public class BookingRoutes {
    public static void handle(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        String path = httpExchange.getRequestURI().getPath();

        if (path.matches("/villas/\\d+/bookings")) {
            int villaId = Integer.parseInt(path.split("/")[2]);
            BookingController.getBookingsByVilla(req, res, villaId);
        }
    }
}