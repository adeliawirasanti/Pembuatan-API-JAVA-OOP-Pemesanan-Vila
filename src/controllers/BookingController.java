package controllers;

import core.Request;
import core.Response;
import models.Booking;
import queries.BookingQuery;
import utils.AuthUtil;
import utils.EntityValidator;

import java.util.List;

public class BookingController extends BaseController {

    // === GET Methods ===

    public static void getBookingsByVillaId(Request req, Response res, int villaId) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            EntityValidator.checkVillaExists(villaId);
            List<Booking> bookings = BookingQuery.getBookingsByVillaId(villaId);
            sendJsonWithMessage(res, "Bookings retrieved successfully.", bookings, 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }
}
