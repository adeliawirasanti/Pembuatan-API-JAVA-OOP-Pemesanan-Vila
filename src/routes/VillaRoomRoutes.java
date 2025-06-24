package routes;

import controllers.RoomController;
import core.Request;
import core.Response;

public class VillaRoomRoutes {
    public static void handle(Request req, Response res, String path, String method) {
        String[] parts = path.split("/");
        if (parts.length < 4 || !parts[3].equals("rooms")) {
            res.setBody("{\"error\":\"Invalid endpoint rooms.\"}");
            res.send(404);
            return;
        }

        try {
            int villaId = Integer.parseInt(parts[2]);

            switch (method) {
                case "GET":
                    RoomController.getRoomsByVillaId(req, res, villaId);
                    return;
                case "POST":
                    RoomController.createRoom(req, res, villaId);
                    return;
                case "PUT":
                    if (parts.length == 5) {
                        int roomId = Integer.parseInt(parts[4]);
                        RoomController.updateRoom(req, res, villaId, roomId);
                        return;
                    }
                    break;
                case "DELETE":
                    if (parts.length == 5) {
                        int roomId = Integer.parseInt(parts[4]);
                        RoomController.deleteRoom(req, res, villaId, roomId);
                        return;
                    }
                    break;
            }

            res.setBody("{\"error\":\"Method or endpoint rooms are invalid.\"}");
            res.send(404);
        } catch (NumberFormatException e) {
            res.setBody("{\"error\":\"Villa/room ID must be a number.\"}");
            res.send(400);
        }
    }
}
