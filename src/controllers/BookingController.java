package controllers;

import models.Bookings;
import queries.BookingQuery;
import core.Request;
import core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class BookingController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getBookingsByVilla(Request req, Response res, int villaId) {
        List<Bookings> bookings = BookingQuery.getBookingsByVillaId(villaId);
        try {
            String json = mapper.writeValueAsString(bookings);
            res.setBody(json);
            res.send(200);
        } catch (IOException e) {
            res.setBody("{\"error\":\"Gagal mengubah data\"}");
            res.send(500);
        }
    }
}
