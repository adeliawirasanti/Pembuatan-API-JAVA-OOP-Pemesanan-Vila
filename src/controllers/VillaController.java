package controllers;

import core.Request;
import core.Response;
import exceptions.NotFoundException;
import models.Villa;
import queries.VillaQuery;
import utils.AuthUtil;
import utils.EntityValidator;
import utils.VillaValidator;

public class VillaController extends BaseController {

    // === GET Methods ===
    public static void getAllVillas(Request req, Response res) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        sendJson(res, VillaQuery.getAllVillas(), 200);
    }

    public static void getVillaById(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Villa villa = EntityValidator.checkVillaExists(id);
            sendJson(res, villa, 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    public static void getAvailableVillas(Request req, Response res, String ci, String co) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        if (ci == null || co == null || ci.isBlank() || co.isBlank()) {
            sendError(res, "Check-in and check-out dates are required.", 400);
            return;
        }

        sendJson(res, VillaQuery.getAvailableVillas(ci, co), 200);
    }

    // === POST Method ===
    public static void createVilla(Request req, Response res) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Villa villa = mapper.readValue(req.getBody(), Villa.class);
            VillaValidator.validate(villa);

            if (VillaQuery.insertVilla(villa)) {
                sendMessage(res, "The villa was successfully added.", 201);
            } else {
                throw new RuntimeException("Failed to add villas.");
            }
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === PUT Method ===
    public static void updateVilla(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            EntityValidator.checkVillaExists(id);
            Villa villa = mapper.readValue(req.getBody(), Villa.class);
            villa.setId(id);
            VillaValidator.validate(villa);

            if (VillaQuery.updateVilla(villa)) {
                sendMessage(res, "The villa was successfully updated.", 200);
            } else {
                throw new NotFoundException("Villa with ID " + id + " was not found.");
            }
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === DELETE Method ===
    public static void deleteVilla(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            if (VillaQuery.deleteVilla(id)) {
                sendMessage(res, "The villa was successfully deleted.", 200);
            } else {
                throw new NotFoundException("The villa was not found.");
            }
        } catch (Exception e) {
            handleException(res, e);
        }
    }
}
