package routes;

import controllers.RoomController;
import core.Request;
import core.Response;

public class VillaRoomRoutes {
    public static void handle(Request req, Response res, String path, String method) {
        String[] parts = path.split("/");
        if (parts.length < 4 || !parts[3].equals("rooms")) {
            res.setBody("{\"error\":\"Endpoint rooms tidak valid\"}");
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

            res.setBody("{\"error\":\"Method atau endpoint rooms tidak valid\"}");
            res.send(404);
        } catch (NumberFormatException e) {
            res.setBody("{\"error\":\"ID villa/room harus berupa angka\"}");
            res.send(400);
        }
    }
}
