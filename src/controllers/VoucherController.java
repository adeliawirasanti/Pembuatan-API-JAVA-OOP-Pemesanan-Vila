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
            if (voucher == null) throw new NotFoundException("The voucher was not found.");
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
                throw new BadRequestException("The voucher code already exists.");
            }

            VoucherQuery.insert(voucher);
            sendMessage(res, "Voucher created successfully.", 201);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === PUT ===

    public static void updateVoucher(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Voucher existing = VoucherQuery.findById(id);
            if (existing == null) throw new NotFoundException("The voucher was not found.");

            Voucher voucher = mapper.readValue(req.getBody(), Voucher.class);
            voucher.setId(id);
            VoucherValidator.validate(voucher);

            VoucherQuery.update(id, voucher);
            sendMessage(res, "Voucher successfully updated.", 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === DELETE ===

    public static void deleteVoucher(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Voucher voucher = VoucherQuery.findById(id);
            if (voucher == null) throw new NotFoundException("The voucher was not found.");

            VoucherQuery.delete(id);
            sendMessage(res, "The voucher was successfully deleted.", 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }
}
