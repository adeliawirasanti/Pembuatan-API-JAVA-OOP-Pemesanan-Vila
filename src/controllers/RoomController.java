package controllers;

import models.Rooms;
import queries.RoomQuery;
import core.Request;
import core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class RoomController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getRoomsByVilla(Request req, Response res, int villaId) {
        List<Rooms> rooms = RoomQuery.getRoomsByVillaId(villaId);
        try {
            String json = mapper.writeValueAsString(rooms);
            res.setBody(json);
            res.send(200);
        } catch (IOException e) {
            res.setBody("{\"error\":\"Gagal mengubah data\"}");
            res.send(500);
        }
    }

    public static void createRoom(Request req, Response res, int villaId) {
        try {
            Rooms room = mapper.readValue(req.getBody(), Rooms.class);
            room.setVilla(villaId);

            boolean success = RoomQuery.insertRoom(room);
            if (success) {
                res.setBody("{\"message\":\"Room berhasil ditambahkan\"}");
                res.send(201);
            } else {
                res.setBody("{\"error\":\"Gagal menambahkan room\"}");
                res.send(500);
            }
        } catch (IOException e) {
            res.setBody("{\"error\":\"Format data salah\"}");
            res.send(400);
        }
    }

    public static void deleteRoom(Request req, Response res, int roomId) {
        boolean success = RoomQuery.deleteRoom(roomId);
        if (success) {
            res.setBody("{\"message\":\"Room berhasil dihapus\"}");
            res.send(200);
        } else {
            res.setBody("{\"error\":\"Room tidak ditemukan\"}");
            res.send(404);
        }
    }

    public static void updateRoom(Request req, Response res, int villaId, int roomId) {
        try {
            Rooms room = mapper.readValue(req.getBody(), Rooms.class);
            room.setId(roomId);
            room.setVilla(villaId);

            boolean success = RoomQuery.updateRoom(room);
            if (success) {
                res.setBody("{\"message\":\"Room berhasil diperbarui\"}");
                res.send(200);
            } else {
                res.setBody("{\"error\":\"Room tidak ditemukan\"}");
                res.send(404);
            }
        } catch (IOException e) {
            res.setBody("{\"error\":\"Format data salah\"}");
            res.send(400);
        }
    }
}
