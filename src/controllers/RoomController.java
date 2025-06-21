package controllers;

import models.Room;
import queries.RoomQuery;
import core.Request;
import core.Response;
import utils.RoomValidator;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.EntityValidator;
import java.io.IOException;
import java.util.List;

public class RoomController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getRoomsByVillaId(Request req, Response res, int villaId) {
        try {
            EntityValidator.checkVillaExists(villaId);
            List<Room> rooms = RoomQuery.getRoomsByVillaId(villaId);
            res.setBody(mapper.writeValueAsString(rooms));
            res.send(200);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (IOException e) {
            res.setBody(jsonError("Gagal mengubah data"));
            res.send(500);
        }
    }

    public static void createRoom(Request req, Response res, int villaId) {
        try {
            EntityValidator.checkVillaExists(villaId);

            Room room = mapper.readValue(req.getBody(), Room.class);
            room.setVilla(villaId);
            RoomValidator.validate(room);

            if (RoomQuery.insertRoom(room)) {
                res.setBody("{\"message\":\"Room berhasil ditambahkan\"}");
                res.send(201);
            } else {
                throw new RuntimeException("Gagal menambahkan room");
            }
        } catch (BadRequestException e) {
            res.setBody("{\"error\":\"" + e.getMessage() + "\"}");
            res.send(400);
        } catch (NotFoundException e) {
            res.setBody("{\"error\":\"" + e.getMessage() + "\"}");
            res.send(404);
        } catch (IOException e) {
            res.setBody("{\"error\":\"Format JSON salah\"}");
            res.send(400);
        } catch (Exception e) {
            res.setBody("{\"error\":\"" + e.getMessage() + "\"}");
            res.send(500);
        }
    }

    public static void updateRoom(Request req, Response res, int villaId, int roomId) {
        try {
            EntityValidator.checkVillaExists(villaId);

            Room room = mapper.readValue(req.getBody(), Room.class);
            room.setId(roomId);
            room.setVilla(villaId);
            RoomValidator.validate(room);

            if (RoomQuery.updateRoom(room)) {
                res.setBody("{\"message\":\"Room berhasil diperbarui\"}");
                res.send(200);
            } else {
                throw new NotFoundException("Room tidak ditemukan");
            }
        } catch (BadRequestException e) {
            res.setBody("{\"error\":\"" + e.getMessage() + "\"}");
            res.send(400);
        } catch (NotFoundException e) {
            res.setBody("{\"error\":\"" + e.getMessage() + "\"}");
            res.send(404);
        } catch (IOException e) {
            res.setBody("{\"error\":\"Format JSON salah\"}");
            res.send(400);
        }
    }

    public static void deleteRoom(Request req, Response res, int villaId, int roomId) {
        try {
            EntityValidator.checkVillaExists(villaId);

            boolean found = RoomQuery.getRoomsByVillaId(villaId)
                    .stream()
                    .anyMatch(r -> r.getId() == roomId);
            if (!found) throw new NotFoundException("Room tidak ditemukan di vila ini");

            if (!RoomQuery.deleteRoom(roomId))
                throw new NotFoundException("Gagal menghapus room");

            res.setBody(jsonMessage("Room berhasil dihapus"));
            res.send(200);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        }
    }

    private static String jsonMessage(String msg) {
        return String.format("{\"message\":\"%s\"}", msg);
    }

    private static String jsonError(String msg) {
        return String.format("{\"error\":\"%s\"}", msg);
    }
}
