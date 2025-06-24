package routes;

import com.sun.net.httpserver.HttpExchange;
import core.Request;
import core.Response;
import controllers.VillaController;

public class VillaRoutes {
    public static void handle(HttpExchange exchange) {
        Request req = new Request(exchange);
        Response res = new Response(exchange);
        String method = req.getRequestMethod();
        String path = req.getPath();
        String[] parts = path.split("/");

        try {
            if (parts.length >= 4 && parts[1].equals("villas")) {
                if (parts[3].equals("rooms")) {
                    VillaRoomRoutes.handle(req, res, path, method);
                    return;
                }
                if (parts[3].equals("bookings")) {
                    VillaBookingRoutes.handle(req, res, path, method);
                    return;
                }
                if (parts[3].equals("reviews")) {
                    VillaReviewRoutes.handle(req, res, path, method);
                    return;
                }
            }

            switch (method) {
                case "GET":
                    handleGet(req, res, path, exchange.getRequestURI().getQuery());
                    break;

                case "POST":
                    if (path.equals("/villas")) {
                        VillaController.createVilla(req, res);
                    } else {
                        res.setBody("{\"error\":\"POST endpoint not found.\"}");
                        res.send(404);
                    }
                    break;

                case "PUT":
                    if (path.matches("^/villas/\\d+$")) {
                        int id = Integer.parseInt(parts[2]);
                        VillaController.updateVilla(req, res, id);
                    } else {
                        res.setBody("{\"error\":\"PUT endpoint not found.\"}");
                        res.send(404);
                    }
                    break;

                case "DELETE":
                    if (path.matches("^/villas/\\d+$")) {
                        int id = Integer.parseInt(parts[2]);
                        VillaController.deleteVilla(req, res, id);
                    } else {
                        res.setBody("{\"error\":\"DELETE endpoint not found.\"}");
                        res.send(404);
                    }
                    break;

                default:
                    res.setBody("{\"error\":\"Methods are not allowed.\"}");
                    res.send(405);
            }

        } catch (Exception e) {
            res.setBody("{\"error\":\"A server error occurred.\"}");
            res.send(500);
        }
    }

    private static void handleGet(Request req, Response res, String path, String query) {
        try {
            if (path.equals("/villas")) {
                if (query == null) {
                    VillaController.getAllVillas(req, res);
                } else {
                    String ci = null, co = null;
                    for (String param : query.split("&")) {
                        if (param.startsWith("ci_date=")) ci = param.substring(8);
                        if (param.startsWith("co_date=")) co = param.substring(8);
                    }

                    if (ci != null && co != null) {
                        VillaController.getAvailableVillas(req, res, ci, co);
                    } else {
                        res.setBody("{\"error\":\"Query tidak lengkap\"}");
                        res.send(400);
                    }
                }
                return;
            }

            if (path.matches("^/villas/\\d+$")) {
                int id = Integer.parseInt(path.split("/")[2]);
                VillaController.getVillaById(req, res, id);
                return;
            }

            res.setBody("{\"error\":\"GET endpoint not found.\"}");
            res.send(404);
        } catch (Exception e) {
            res.setBody("{\"error\":\"Internal error when processing GET.\"}");
            res.send(500);
        }
    }
}
