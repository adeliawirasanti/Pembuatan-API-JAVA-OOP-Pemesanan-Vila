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
        String path = req.getPath(); // misalnya: /vouchers/2

        // Cek apakah GET /vouchers/{id}
        if (method.equalsIgnoreCase("GET") && path.matches("^/vouchers/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            req.setParam("id", String.valueOf(id));
            VoucherController.show(req, res);
        }

        // Cek GET /vouchers
        else if (method.equalsIgnoreCase("GET") && path.equals("/vouchers")) {
            VoucherController.index(req, res);
        }

        // Cek POST /vouchers
        else if (method.equalsIgnoreCase("POST") && path.equals("/vouchers")) {
            VoucherController.store(req, res);
        }

        // Jika tidak sesuai
        else {
            res.setBody("{\"error\":\"Method not allowed\"}");
            res.send(405);
        }
    }
}