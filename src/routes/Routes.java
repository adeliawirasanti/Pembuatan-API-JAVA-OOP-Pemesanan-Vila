package routes;

import com.sun.net.httpserver.HttpExchange;
import routes.*;

public class Routes {
    public static void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();

        if (path.contains("/rooms")) {
            RoomRoutes.handle(exchange);
        } else if (path.contains("/bookings")) {
            BookingRoutes.handle(exchange);
        } else if (path.contains("/reviews")) {
            ReviewRoutes.handle(exchange);
        } else if (path.startsWith("/villas")) {
            VillaRoutes.handle(exchange);
        }
    }
}