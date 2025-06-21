package controllers;

import core.Request;
import core.Response;
import models.Voucher;
import queries.VoucherQuery;
import utils.VoucherValidator;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class VoucherController {

    public static void index(Request req, Response res) {
        try {
            List<Voucher> vouchers = VoucherQuery.getAll();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < vouchers.size(); i++) {
                Voucher v = vouchers.get(i);
                sb.append(String.format(
                        "{\"id\":%d,\"code\":\"%s\",\"description\":\"%s\",\"discount\":%.2f,\"start_date\":\"%s\",\"end_date\":\"%s\"}",
                        v.getId(), v.getCode(), v.getDescription(), v.getDiscount(),
                        v.getStart_date(), v.getEnd_date()));
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

            // Cek field wajib
            if (!body.containsKey("code") || !body.containsKey("discount") ||
                    !body.containsKey("start_date") || !body.containsKey("end_date")) {
                res.setBody("{\"error\":\"Missing required fields\"}");
                res.send(400);
                return;
            }

            // Parsing dan mapping input
            String code = body.get("code").toString().trim();
            String description = body.get("description") != null ? body.get("description").toString().trim() : "";
            double discount = Double.parseDouble(body.get("discount").toString());
            String start_date = body.get("start_date").toString().trim();
            String end_date = body.get("end_date").toString().trim();

            // Buat objek voucher dari input
            Voucher v = new Voucher();
            v.setCode(code);
            v.setDescription(description);
            v.setDiscount(discount);
            v.setStart_date(start_date);
            v.setEnd_date(end_date);

            // Validasi via util
            String validationError = VoucherValidator.validate(v);
            if (validationError != null) {
                res.setBody("{\"error\":\"" + validationError + "\"}");
                res.send(400);
                return;
            }

            // Cek kode voucher unik
            if (VoucherQuery.existsByCode(code)) {
                res.setBody("{\"error\":\"Voucher code already exists\"}");
                res.send(400);
                return;
            }

            // Simpan data
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

    public static void show(Request req, Response res) {
        try {
            int id = Integer.parseInt(req.getParam("id"));
            Voucher v = VoucherQuery.findById(id);
            if (v == null) {
                res.setBody("{\"error\":\"Voucher not found\"}");
                res.send(404);
                return;
            }

            String json = String.format(
                    "{\"id\":%d,\"code\":\"%s\",\"description\":\"%s\",\"discount\":%.2f,\"start_date\":\"%s\",\"end_date\":\"%s\"}",
                    v.getId(), v.getCode(), v.getDescription(), v.getDiscount(),
                    v.getStart_date(), v.getEnd_date()
            );
            res.setBody(json);
            res.send(200);
        } catch (NumberFormatException e) {
            res.setBody("{\"error\":\"Invalid voucher ID\"}");
            res.send(400);
        } catch (Exception e) {
            res.setBody("{\"error\":\"Server error\"}");
            res.send(500);
        }
    }
}
