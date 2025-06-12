package routes;

import controllers.RoomController;
import core.Request;
import core.Response;

public class VillaRoomRoutes {
    public static void handle(Request req, Response res, String path, String method) {
        String[] parts = path.split("/");
        if (parts.length < 4) {
            res.setBody("{\"error\":\"Invalid rooms endpoint\"}");
            res.send(404);
            return;
        }

        try {
            int villaId = Integer.parseInt(parts[2]);

            if (method.equals("GET") && parts.length == 4) {
                RoomController.getRoomsByVillaId(req, res, villaId);
                return;
            }

            if (method.equals("POST") && parts.length == 4) {
                RoomController.createRoom(req, res, villaId);
                return;
            }

            if (method.equals("PUT") && parts.length == 5) {
                int roomId = Integer.parseInt(parts[4]);
                RoomController.updateRoom(req, res, villaId, roomId);
                return;
            }

            if (method.equals("DELETE") && parts.length == 5) {
                int roomId = Integer.parseInt(parts[4]);
                RoomController.deleteRoom(req, res, villaId, roomId);
                return;
            }

            res.setBody("{\"error\":\"Rooms endpoint not found\"}");
            res.send(404);
        } catch (NumberFormatException e) {
            res.setBody("{\"error\":\"Invalid ID format\"}");
            res.send(400);
        }
    }
}
