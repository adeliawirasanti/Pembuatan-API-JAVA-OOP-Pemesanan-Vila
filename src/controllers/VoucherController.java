package controllers;

import core.Request;
import core.Response;
import models.Voucher;
import queries.VoucherQuery;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class VoucherController {

    public static void index(Request req, Response res) {
        try {
            List<Voucher> vouchers = VoucherQuery.getAll();
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < vouchers.size(); i++) {
                Voucher v = vouchers.get(i);
                sb.append(String.format(
                        "{\"id\":%d,\"code\":\"%s\",\"description\":\"%s\",\"discount\":%.2f,\"start_date\":\"%s\",\"end_date\":\"%s\"}",
                        v.getId(), v.getCode(), v.getDescription(), v.getDiscount(), v.getStart_date(), v.getEnd_date()));
                if (i != vouchers.size() - 1) sb.append(",");
            }
            sb.append("]");
            res.setBody(sb.toString());
            res.send(200);
        } catch (SQLException e) {
            res.setBody("{\"error\":\"Database error\"}");
            res.send(500);
        }
    }

    public static void store(Request req, Response res) {
        try {
            Map<String, Object> body = req.getJSON();

            if (!body.containsKey("code") || !body.containsKey("discount") ||
                    !body.containsKey("start_date") || !body.containsKey("end_date")) {
                res.setBody("{\"error\":\"Missing required fields\"}");
                res.send(400);
                return;
            }

            String code = body.get("code").toString().trim();
            String description = body.get("description") != null ? body.get("description").toString().trim() : "";
            double discount = Double.parseDouble(body.get("discount").toString());
            String start_date = body.get("start_date").toString().trim();
            String end_date = body.get("end_date").toString().trim();

            if (code.isEmpty()) {
                res.setBody("{\"error\":\"Code cannot be empty\"}");
                res.send(400);
                return;
            }

            if (discount <= 0 || discount > 100) {
                res.setBody("{\"error\":\"Discount must be between 0 and 100\"}");
                res.send(400);
                return;
            }

            if (!start_date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}") ||
                    !end_date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                res.setBody("{\"error\":\"Invalid date format. Use YYYY-MM-DD hh:mm:ss\"}");
                res.send(400);
                return;
            }

            if (start_date.compareTo(end_date) > 0) {
                res.setBody("{\"error\":\"start_date must be before end_date\"}");
                res.send(400);
                return;
            }

            Voucher v = new Voucher();
            v.setCode(code);
            v.setDescription(description);
            v.setDiscount(discount);
            v.setStart_date(start_date);
            v.setEnd_date(end_date);

            VoucherQuery.insert(v);
            res.setBody("{\"message\":\"Voucher created\"}");
            res.send(201);
        } catch (SQLException e) {
            res.setBody("{\"error\":\"Database error\"}");
            res.send(500);
        } catch (Exception e) {
            res.setBody("{\"error\":\"Invalid input\"}");
            res.send(400);
        }
    }
}
