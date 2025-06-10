package routes;

import com.sun.net.httpserver.HttpExchange;
import controllers.RoomController;
import core.Request;
import core.Response;

public class RoomRoutes {
    public static void handle(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        String method = req.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();

        if (path.matches("/villas/\\d+/rooms")) {
            int villaId = Integer.parseInt(path.split("/")[2]);
            if (method.equals("GET")) RoomController.getRoomsByVilla(req, res, villaId);
            if (method.equals("POST")) RoomController.createRoom(req, res, villaId);
            return;
        }

        if (path.matches("/villas/\\d+/rooms/\\d+")) {
            int villaId = Integer.parseInt(path.split("/")[2]);
            int roomId = Integer.parseInt(path.split("/")[4]);
            if (method.equals("PUT")) RoomController.updateRoom(req, res, villaId, roomId);
            if (method.equals("DELETE")) RoomController.deleteRoom(req, res, roomId);
        }
    }
}