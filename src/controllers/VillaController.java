package controllers;

import models.Villas;
import queries.VillaQuery;
import utils.VillaValidator;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import core.Request;
import core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.EntityValidator;
import java.io.IOException;
import java.util.List;

public class VillaController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getAllVillas(Request req, Response res) {
        try {
            List<Villas> villas = VillaQuery.getAllVillas();
            res.setBody(mapper.writeValueAsString(villas));
            res.send(200);
        } catch (IOException e) {
            res.setBody(jsonError("Gagal mengubah data"));
            res.send(500);
        }
    }

    public static void getVillaById(Request req, Response res, int id) {
        try {
            Villas villa = EntityValidator.checkVillaExists(id);
            res.setBody(mapper.writeValueAsString(villa));
            res.send(200);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (IOException e) {
            res.setBody(jsonError("Gagal mengubah data"));
            res.send(500);
        }
    }

    public static void createVilla(Request req, Response res) {
        try {
            Villas villa = mapper.readValue(req.getBody(), Villas.class);
            VillaValidator.validate(villa);
            if (VillaQuery.insertVilla(villa)) {
                res.setBody(jsonMessage("Villa ditambahkan"));
                res.send(201);
            } else {
                throw new RuntimeException("Gagal menambahkan villa");
            }
        } catch (BadRequestException | IOException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(400);
        } catch (Exception e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(500);
        }
    }

    public static void updateVilla(Request req, Response res, int id) {
        try {
            EntityValidator.checkVillaExists(id);

            Villas villa = mapper.readValue(req.getBody(), Villas.class);
            villa.setId(id);
            VillaValidator.validate(villa);

            if (VillaQuery.updateVilla(villa)) {
                res.setBody("{\"message\":\"Villa diperbarui\"}");
                res.send(200);
            } else {
                throw new NotFoundException("Villa dengan ID " + id + " tidak ditemukan");
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

    public static void deleteVilla(Request req, Response res, int id) {
        try {
            if (VillaQuery.deleteVilla(id)) {
                res.setBody(jsonMessage("Villa dihapus"));
                res.send(200);
            } else {
                throw new NotFoundException("Villa tidak ditemukan");
            }
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        }
    }

    public static void getAvailableVillas(Request req, Response res, String ci, String co) {
        try {
            if (ci == null || co == null || ci.isBlank() || co.isBlank()) {
                throw new BadRequestException("Tanggal check-in dan check-out wajib diisi.");
            }
            List<Villas> villas = VillaQuery.getAvailableVillas(ci, co);
            res.setBody(mapper.writeValueAsString(villas));
            res.send(200);
        } catch (BadRequestException | IOException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(400);
        }
    }

    private static String jsonMessage(String msg) {
        return String.format("{\"message\":\"%s\"}", msg);
    }

    private static String jsonError(String msg) {
        return String.format("{\"error\":\"%s\"}", msg);
    }
}