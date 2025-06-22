package utils;

import exceptions.BadRequestException;
import models.Voucher;

import java.time.LocalDateTime;

public class VoucherValidator {
    public static void validate(Voucher voucher) {
        if (voucher.getCode() == null || voucher.getCode().trim().isEmpty()) {
            throw new BadRequestException("Kode voucher tidak boleh kosong.");
        }
        if (voucher.getCode().length() > 50) {
            throw new BadRequestException("Kode voucher tidak boleh lebih dari 50 karakter.");
        }

        if (voucher.getDescription() == null || voucher.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Deskripsi voucher tidak boleh kosong.");
        }
        if (voucher.getDescription().length() < 5) {
            throw new BadRequestException("Deskripsi voucher minimal 5 karakter.");
        }

        if (voucher.getDiscount() <= 0) {
            throw new BadRequestException("Diskon harus lebih dari 0.");
        }
        if (voucher.getDiscount() > 100) {
            throw new BadRequestException("Diskon tidak boleh lebih dari 100%.");
        }

        if (voucher.getStartDate() == null || voucher.getStartDate().trim().isEmpty()) {
            throw new BadRequestException("Tanggal mulai tidak boleh kosong.");
        }

        if (voucher.getEndDate() == null || voucher.getEndDate().trim().isEmpty()) {
            throw new BadRequestException("Tanggal akhir tidak boleh kosong.");
        }

        try {
            LocalDateTime start = LocalDateTime.parse(voucher.getStartDate().replace(" ", "T"));
            LocalDateTime end = LocalDateTime.parse(voucher.getEndDate().replace(" ", "T"));
            if (start.isAfter(end)) {
                throw new BadRequestException("Tanggal mulai harus lebih awal dari tanggal akhir.");
            }
        } catch (Exception e) {
            throw new BadRequestException("Format tanggal tidak valid. Gunakan format yyyy-MM-dd HH:mm:ss.");
        }
    }
}
