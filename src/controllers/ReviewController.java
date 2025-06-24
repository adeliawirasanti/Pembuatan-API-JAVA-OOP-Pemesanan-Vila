package controllers;

import core.Request;
import core.Response;
import exceptions.NotFoundException;
import models.Booking;
import models.Review;
import queries.BookingQuery;
import queries.ReviewQuery;
import utils.AuthUtil;
import utils.EntityValidator;

import java.util.List;
import java.util.Map;

public class ReviewController extends BaseController {

    // === GET ===
    public static void getReviewsByVillaId(Request req, Response res, int villaId) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            EntityValidator.checkVillaExists(villaId);
            List<Review> reviews = ReviewQuery.getReviewsByVillaId(villaId);
            sendJson(res, reviews, 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === POST ===
    public static void createReviewForBooking(Request req, Response res, int customerId, int bookingId) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Booking b = BookingQuery.getBookingById(bookingId);
            if (b == null || b.getCustomer() != customerId) {
                throw new NotFoundException("Booking not found or not owned by this customer.");
            }

            Map<String, Object> body = req.getJSON();
            int star = (int) body.get("star");
            String title = body.get("title").toString();
            String content = body.get("content").toString();

            Review r = new Review(bookingId, star, title, content);
            ReviewQuery.insertReview(r);

            sendMessage(res, "Review was successfully added.", 201);
        } catch (Exception e) {
            handleException(res, e);
        }
    }
}
