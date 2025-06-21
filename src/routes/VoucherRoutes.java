package routes;

import com.sun.net.httpserver.HttpExchange;
import controllers.VoucherController;
import core.Request;
import core.Response;

public class VoucherRoutes {
    public static void handle(HttpExchange exchange) throws Exception {
        Request req = new Request(exchange);
        Response res = new Response(exchange);

        String method = req.getRequestMethod();

        if (method.equalsIgnoreCase("GET")) {
            VoucherController.index(req, res);
        } else if (method.equalsIgnoreCase("POST")) {
            VoucherController.store(req, res);
        } else {
            res.setBody("{\"error\":\"Method not allowed\"}");
            res.send(405);
        }
    }
}
