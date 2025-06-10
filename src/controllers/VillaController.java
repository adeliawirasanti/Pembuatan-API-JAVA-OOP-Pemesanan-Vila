package controllers;

import models.Villas;
import queries.VillaQuery;
import core.Request;
import core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class VillaController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getAllVillas(Request req, Response res) {
        List<Villas> villas = VillaQuery.getAllVillas();
        try {
            String json = mapper.writeValueAsString(villas);
            res.setBody(json);
            res.send(200);
        } catch (IOException e) {
            res.setBody("{\"error\":\"Failed to convert data\"}");
            res.send(500);
        }
    }

    public static void createVilla(Request req, Response res) {
        try {
            Villas villa = mapper.readValue(req.getBody(), Villas.class);

            if (villa.getName() == null || villa.getDescription() == null || villa.getAddress() == null) {
                res.setBody("{\"error\":\"Data tidak lengkap\"}");
                res.send(400);
                return;
            }

            boolean success = VillaQuery.insertVilla(villa);
            if (success) {
                res.setBody("{\"message\":\"Villa ditambahkan\"}");
                res.send(201);
            } else {
                res.setBody("{\"error\":\"Gagal menambahkan villa\"}");
                res.send(500);
            }
        } catch (IOException e) {
            res.setBody("{\"error\":\"Format data salah\"}");
            res.send(400);
        }
    }

    public static void updateVilla(Request req, Response res, int id) {
        try {
            Villas villa = mapper.readValue(req.getBody(), Villas.class);
            villa.setId(id);

            boolean updated = VillaQuery.updateVilla(villa);
            if (updated) {
                res.setBody("{\"message\":\"Villa berhasil diperbarui\"}");
                res.send(200);
            } else {
                res.setBody("{\"error\":\"Villa tidak ditemukan\"}");
                res.send(404);
            }
        } catch (IOException e) {
            res.setBody("{\"error\":\"Format data salah\"}");
            res.send(400);
        }
    }

    public static void deleteVilla(Request req, Response res, int id) {
        boolean deleted = VillaQuery.deleteVilla(id);
        if (deleted) {
            res.setBody("{\"message\":\"Villa berhasil dihapus\"}");
            res.send(200);
        } else {
            res.setBody("{\"error\":\"Villa tidak ditemukan\"}");
            res.send(404);
        }
    }

    public static void getVillaById(Request req, Response res, int id) {
        Villas villa = VillaQuery.getVillaById(id);
        try {
            if (villa != null) {
                String json = mapper.writeValueAsString(villa);
                res.setBody(json);
                res.send(200);
            } else {
                res.setBody("{\"error\":\"Villa tidak ditemukan\"}");
                res.send(404);
            }
        } catch (IOException e) {
            res.setBody("{\"error\":\"Gagal mengubah data\"}");
            res.send(500);
        }
    }

    public static void getAvailableVillas(Request req, Response res, String ci, String co) {
        List<Villas> villas = VillaQuery.getAvailableVillas(ci, co);
        try {
            String json = mapper.writeValueAsString(villas);
            res.setBody(json);
            res.send(200);
        } catch (IOException e) {
            res.setBody("{\"error\":\"Gagal memuat data\"}");
            res.send(500);
        }
    }
}
