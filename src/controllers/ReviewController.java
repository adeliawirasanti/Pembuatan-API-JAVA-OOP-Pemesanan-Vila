package controllers;

import models.Review;
import queries.ReviewQuery;
import core.Request;
import core.Response;
import models.Booking;
import queries.BookingQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.EntityValidator;
import exceptions.NotFoundException;
import java.io.IOException;
import java.util.List;

public class ReviewController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getReviewsByVillaId(Request req, Response res, int villaId) {
        try {
            EntityValidator.checkVillaExists(villaId);
            List<Review> reviews = ReviewQuery.getReviewsByVillaId(villaId);
            res.setBody(mapper.writeValueAsString(reviews));
            res.send(200);
        } catch (NotFoundException | IOException e) {
            res.setBody(String.format("{\"error\":\"%s\"}", e.getMessage()));
            res.send(404);
        }
    }
    public static void createReviewForBooking(Request req, Response res, int customerId, int bookingId) {
        try {
            Booking b = BookingQuery.getBookingById(bookingId);
            if (b == null || b.getCustomer() != customerId) {
                res.setBody("{\"error\":\"Booking tidak ditemukan atau tidak dimiliki customer ini\"}");
                res.send(404);
                return;
            }

            var body = req.getJSON();
            int star = (int) body.get("star");
            String title = body.get("title").toString();
            String content = body.get("content").toString();

            Review r = new Review(bookingId, star, title, content);
            ReviewQuery.insertReview(r);

            res.setBody("{\"message\":\"Review berhasil ditambahkan\"}");
            res.send(201);
        } catch (Exception e) {
            res.setBody("{\"error\":\"Input tidak valid: " + e.getMessage() + "\"}");
            res.send(400);
        }
    }
}
