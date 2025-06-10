package routes;

import com.sun.net.httpserver.HttpExchange;
import controllers.VillaController;
import core.Request;
import core.Response;

import java.util.HashMap;
import java.util.Map;

public class VillaRoutes {
    public static void handle(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        String method = req.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();

        try {
            // GET /villas
            if (path.equals("/villas") && method.equals("GET") && httpExchange.getRequestURI().getQuery() == null) {
                VillaController.getAllVillas(req, res);
                return;
            }

            // GET /villas?ci_date=...&co_date=...
            if (path.equals("/villas") && method.equals("GET") && httpExchange.getRequestURI().getQuery() != null) {
                Map<String, String> queryMap = new HashMap<>();
                String[] queryParams = httpExchange.getRequestURI().getQuery().split("&");
                for (String param : queryParams) {
                    String[] pair = param.split("=");
                    if (pair.length == 2) queryMap.put(pair[0], pair[1]);
                }
                if (queryMap.containsKey("ci_date") && queryMap.containsKey("co_date")) {
                    VillaController.getAvailableVillas(req, res, queryMap.get("ci_date"), queryMap.get("co_date"));
                    return;
                }
            }

            // POST /villas
            if (path.equals("/villas") && method.equals("POST")) {
                VillaController.createVilla(req, res);
                return;
            }

            // GET /villas/{id}, PUT, DELETE
            if (path.matches("/villas/\\d+")) {
                int id = Integer.parseInt(path.split("/")[2]);
                switch (method) {
                    case "GET":
                        VillaController.getVillaById(req, res, id);
                        return;
                    case "PUT":
                        VillaController.updateVilla(req, res, id);
                        return;
                    case "DELETE":
                        VillaController.deleteVilla(req, res, id);
                        return;
                }
            }
        } catch (Exception e) {
            res.setBody("{\"error\":\"Internal core.Server Error\"}");
            res.send(500);
        }
    }
}
