package routes;

import controllers.ReviewController;
import core.Request;
import core.Response;

public class VillaReviewRoutes {
    public static void handle(Request req, Response res, String path, String method) {
        String[] parts = path.split("/");
        if (parts.length != 4 || !parts[3].equals("reviews")) {
            res.setBody("{\"error\":\"Invalid reviews endpoint\"}");
            res.send(404);
            return;
        }

        try {
            int villaId = Integer.parseInt(parts[2]);

            if (method.equals("GET")) {
                ReviewController.getReviewsByVillaId(req, res, villaId);
                return;
            }

            res.setBody("{\"error\":\"Reviews endpoint not found\"}");
            res.send(404);
        } catch (NumberFormatException e) {
            res.setBody("{\"error\":\"Invalid ID format\"}");
            res.send(400);
        }
    }
}
