package controllers;

import core.Request;
import core.Response;
import exceptions.NotFoundException;
import models.Room;
import queries.RoomQuery;
import utils.EntityValidator;
import utils.RoomValidator;

import java.util.List;

public class RoomController extends BaseController {

    // === GET Methods ===

    public static void getRoomsByVillaId(Request req, Response res, int villaId) {
        try {
            EntityValidator.checkVillaExists(villaId);
            List<Room> rooms = RoomQuery.getRoomsByVillaId(villaId);
            sendJsonWithMessage(res, "Rooms retrieved successfully.", rooms, 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === POST Methods ===

    public static void createRoom(Request req, Response res, int villaId) {
        try {
            EntityValidator.checkVillaExists(villaId);
            Room room = mapper.readValue(req.getBody(), Room.class);
            room.setVilla(villaId);
            RoomValidator.validate(room);

            boolean success = RoomQuery.insertRoom(room);
            if (success) {
                sendMessage(res, "Room successfully added.", 201);
            } else {
                throw new RuntimeException("Failed to add room.");
            }
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === PUT Methods ===

    public static void updateRoom(Request req, Response res, int villaId, int roomId) {
        try {
            EntityValidator.checkVillaExists(villaId);
            Room room = mapper.readValue(req.getBody(), Room.class);
            room.setId(roomId);
            room.setVilla(villaId);
            RoomValidator.validate(room);

            if (!RoomQuery.updateRoom(room)) {
                throw new NotFoundException("Room was not found.");
            }

            sendMessage(res, "Room successfully updated.", 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === DELETE Methods ===

    public static void deleteRoom(Request req, Response res, int villaId, int roomId) {
        try {
            EntityValidator.checkVillaExists(villaId);
            boolean found = RoomQuery.getRoomsByVillaId(villaId).stream()
                    .anyMatch(r -> r.getId() == roomId);
            if (!found) throw new NotFoundException("Room is not found in this villa.");

            if (!RoomQuery.deleteRoom(roomId)) {
                throw new NotFoundException("Failed to delete room.");
            }

            sendMessage(res, "Room was successfully deleted.", 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }
}
