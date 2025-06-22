package controllers;

import models.Booking;
import queries.BookingQuery;
import core.Request;
import core.Response;
import utils.AuthUtil;
import utils.EntityValidator;
import exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class BookingController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getBookingsByVillaId(Request req, Response res, int villaId) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            EntityValidator.checkVillaExists(villaId);
            List<Booking> bookings = BookingQuery.getBookingsByVillaId(villaId);
            res.setBody(mapper.writeValueAsString(bookings));
            res.send(200);
        } catch (NotFoundException | IOException e) {
            res.setBody(String.format("{\"error\":\"%s\"}", e.getMessage()));
            res.send(404);
        }
    }
}
