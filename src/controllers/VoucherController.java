package controllers;

import core.Request;
import core.Response;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import models.Voucher;
import queries.VoucherQuery;
import utils.AuthUtil;
import utils.VoucherValidator;

import java.util.List;

public class VoucherController extends BaseController {

    // === GET ===

    public static void getAllVouchers(Request req, Response res) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            List<Voucher> vouchers = VoucherQuery.getAll();
            sendJson(res, vouchers, 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    public static void getVoucherById(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Voucher voucher = VoucherQuery.findById(id);
            if (voucher == null) throw new NotFoundException("Voucher tidak ditemukan.");
            sendJson(res, voucher, 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === POST ===

    public static void createVoucher(Request req, Response res) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Voucher voucher = mapper.readValue(req.getBody(), Voucher.class);
            VoucherValidator.validate(voucher);

            if (VoucherQuery.existsByCode(voucher.getCode())) {
                throw new BadRequestException("Kode voucher sudah ada.");
            }

            VoucherQuery.insert(voucher);
            sendMessage(res, "Voucher berhasil dibuat.", 201);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === PUT ===

    public static void updateVoucher(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Voucher existing = VoucherQuery.findById(id);
            if (existing == null) throw new NotFoundException("Voucher tidak ditemukan.");

            Voucher voucher = mapper.readValue(req.getBody(), Voucher.class);
            voucher.setId(id);
            VoucherValidator.validate(voucher);

            VoucherQuery.update(id, voucher);
            sendMessage(res, "Voucher berhasil diperbarui.", 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === DELETE ===

    public static void deleteVoucher(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Voucher voucher = VoucherQuery.findById(id);
            if (voucher == null) throw new NotFoundException("Voucher tidak ditemukan.");

            VoucherQuery.delete(id);
            sendMessage(res, "Voucher berhasil dihapus.", 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }
}
