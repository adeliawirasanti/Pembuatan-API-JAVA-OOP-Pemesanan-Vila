package routes;

import com.sun.net.httpserver.HttpExchange;
import core.Request;
import core.Response;
import controllers.VillaController;

public class VillaRoutes {
    public static void handle(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        String method = req.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();

        try {
            // Routing untuk nested endpoint
            if (path.matches("^/villas/\\d+/rooms.*")) {
                VillaRoomRoutes.handle(req, res, path, method);
                return;
            }
            if (path.matches("^/villas/\\d+/bookings$")) {
                VillaBookingRoutes.handle(req, res, path, method);
                return;
            }
            if (path.matches("^/villas/\\d+/reviews$")) {
                VillaReviewRoutes.handle(req, res, path, method);
                return;
            }

            // Routing utama /villas
            switch (method) {
                case "GET":
                    handleGet(req, res, path, httpExchange.getRequestURI().getQuery());
                    break;
                case "POST":
                    if (path.equals("/villas")) {
                        VillaController.createVilla(req, res);
                    } else {
                        res.setBody("{\"error\":\"POST endpoint not found\"}");
                        res.send(404);
                    }
                    break;
                case "PUT":
                    if (path.matches("^/villas/\\d+$")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        VillaController.updateVilla(req, res, id);
                    } else {
                        res.setBody("{\"error\":\"PUT endpoint not found\"}");
                        res.send(404);
                    }
                    break;
                case "DELETE":
                    if (path.matches("^/villas/\\d+$")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        VillaController.deleteVilla(req, res, id);
                    } else {
                        res.setBody("{\"error\":\"DELETE endpoint not found\"}");
                        res.send(404);
                    }
                    break;
                default:
                    res.setBody("{\"error\":\"Method Not Allowed\"}");
                    res.send(405);
                    break;
            }
        } catch (Exception e) {
            res.setBody("{\"error\":\"Internal Server Error\"}");
            res.send(500);
        }
    }

    private static void handleGet(Request req, Response res, String path, String query) {
        try {
            if (path.equals("/villas")) {
                if (query == null) {
                    VillaController.getAllVillas(req, res);
                    return;
                }

                // Parsing query string
                String[] params = query.split("&");
                String ci = null, co = null;
                for (String p : params) {
                    if (p.startsWith("ci_date=")) ci = p.substring(8);
                    if (p.startsWith("co_date=")) co = p.substring(8);
                }

                if (ci != null && co != null) {
                    VillaController.getAvailableVillas(req, res, ci, co);
                    return;
                }
            }

            if (path.matches("^/villas/\\d+$")) {
                int id = Integer.parseInt(path.split("/")[2]);
                VillaController.getVillaById(req, res, id);
                return;
            }

            res.setBody("{\"error\":\"GET endpoint not found\"}");
            res.send(404);
        } catch (Exception e) {
            res.setBody("{\"error\":\"Internal Error\"}");
            res.send(500);
        }
    }
}