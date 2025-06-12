package controllers;

import models.Reviews;
import queries.ReviewQuery;
import core.Request;
import core.Response;
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
            List<Reviews> reviews = ReviewQuery.getReviewsByVillaId(villaId);
            res.setBody(mapper.writeValueAsString(reviews));
            res.send(200);
        } catch (NotFoundException | IOException e) {
            res.setBody(String.format("{\"error\":\"%s\"}", e.getMessage()));
            res.send(404);
        }
    }
}
