package controllers;

import models.Voucher;
import queries.VoucherQuery;
import utils.VoucherValidator;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import core.Request;
import core.Response;
import utils.AuthUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class VoucherController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getAllVouchers(Request req, Response res) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            List<Voucher> list = VoucherQuery.getAll();
            res.setBody(mapper.writeValueAsString(list));
            res.send(200);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal mengambil data voucher"));
            res.send(500);
        }
    }

    public static void getVoucherById(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Voucher v = VoucherQuery.findById(id);
            if (v == null) throw new NotFoundException("Voucher tidak ditemukan");

            res.setBody(mapper.writeValueAsString(v));
            res.send(200);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal mengambil data voucher"));
            res.send(500);
        }
    }

    public static void createVoucher(Request req, Response res) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Voucher v = mapper.readValue(req.getBody(), Voucher.class);
            VoucherValidator.validate(v);

            if (VoucherQuery.existsByCode(v.getCode())) {
                throw new BadRequestException("Kode voucher sudah ada");
            }

            VoucherQuery.insert(v);
            res.setBody(jsonMessage("Voucher berhasil dibuat"));
            res.send(201);
        } catch (BadRequestException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(400);
        } catch (IOException e) {
            res.setBody(jsonError("Format JSON salah"));
            res.send(400);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal menyimpan voucher"));
            res.send(500);
        }
    }

    public static void updateVoucher(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Voucher existing = VoucherQuery.findById(id);
            if (existing == null) throw new NotFoundException("Voucher tidak ditemukan");

            Voucher v = mapper.readValue(req.getBody(), Voucher.class);
            v.setId(id);
            VoucherValidator.validate(v);

            VoucherQuery.update(id, v);
            res.setBody(jsonMessage("Voucher berhasil diperbarui"));
            res.send(200);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (BadRequestException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(400);
        } catch (IOException e) {
            res.setBody(jsonError("Format JSON salah"));
            res.send(400);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal memperbarui voucher"));
            res.send(500);
        }
    }

    public static void deleteVoucher(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Voucher v = VoucherQuery.findById(id);
            if (v == null) throw new NotFoundException("Voucher tidak ditemukan");

            VoucherQuery.delete(id);
            res.setBody(jsonMessage("Voucher berhasil dihapus"));
            res.send(200);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal menghapus voucher"));
            res.send(500);
        }
    }

    private static String jsonMessage(String msg) {
        return String.format("{\"message\":\"%s\"}", msg);
    }

    private static String jsonError(String msg) {
        return String.format("{\"error\":\"%s\"}", msg);
    }
}
