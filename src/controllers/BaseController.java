package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.Response;
import exceptions.BadRequestException;
import exceptions.NotFoundException;

import java.util.Map;

public class BaseController {
    protected static final ObjectMapper mapper = new ObjectMapper();

    protected static void sendJson(Response res, Object data, int status) {
        try {
            res.setBody(mapper.writeValueAsString(data));
            res.send(status);
        } catch (Exception e) {
            sendError(res, "Failed to process data.", 500);
        }
    }

    protected static void sendJsonWithMessage(Response res, String msg, Object data, int status) {
        try {
            String body = mapper.writeValueAsString(Map.of(
                    "message", msg,
                    "data", data
            ));
            res.setBody(body);
            res.send(status);
        } catch (Exception e) {
            sendError(res, "Failed to process data.", 500);
        }
    }

    protected static void sendMessage(Response res, String msg, int status) {
        res.setBody(String.format("{\"message\":\"%s\"}", msg));
        res.send(status);
    }

    protected static void sendError(Response res, String msg, int status) {
        res.setBody(String.format("{\"error\":\"%s\"}", msg));
        res.send(status);
    }

    protected static void handleException(Response res, Exception e) {
        if (e instanceof BadRequestException) {
            sendError(res, e.getMessage(), 400);
        } else if (e instanceof NotFoundException) {
            sendError(res, e.getMessage(), 404);
        } else {
            sendError(res, "Server error occurred.", 500);
        }
    }
}