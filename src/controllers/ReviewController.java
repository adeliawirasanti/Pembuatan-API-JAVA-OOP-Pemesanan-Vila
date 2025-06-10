package controllers;

import models.Reviews;
import queries.ReviewQuery;
import core.Request;
import core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class ReviewController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getReviewsByVilla(Request req, Response res) {
        List<Reviews> reviews = ReviewQuery.getReviewsByVilla();
        try {
            String json = mapper.writeValueAsString(reviews);
            res.setBody(json);
            res.send(200);
        } catch (IOException e) {
            res.setBody("{\"error\":\"Gagal mengubah data\"}");
            res.send(500);
        }
    }
}
