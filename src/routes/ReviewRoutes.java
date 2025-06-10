package routes;

import com.sun.net.httpserver.HttpExchange;
import controllers.ReviewController;
import core.Request;
import core.Response;

public class ReviewRoutes {
    public static void handle(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        String path = httpExchange.getRequestURI().getPath();

        if (path.matches("/villas/\\d+/reviews")) {
            int villaId = Integer.parseInt(path.split("/")[2]);
            ReviewController.getReviewsByVilla(req, res);
        }
    }
}
