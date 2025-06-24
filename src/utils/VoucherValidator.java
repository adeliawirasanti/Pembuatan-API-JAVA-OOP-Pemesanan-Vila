package utils;

import exceptions.BadRequestException;
import models.Voucher;

import java.time.LocalDateTime;

public class VoucherValidator {
    public static void validate(Voucher voucher) {
        if (voucher.getCode() == null || voucher.getCode().trim().isEmpty()) {
            throw new BadRequestException("The voucher code cannot be empty.");
        }
        if (voucher.getCode().length() > 50) {
            throw new BadRequestException("The voucher code cannot exceed 50 characters.");
        }

        if (voucher.getDescription() == null || voucher.getDescription().trim().isEmpty()) {
            throw new BadRequestException("The voucher description cannot be empty.");
        }
        if (voucher.getDescription().length() < 5) {
            throw new BadRequestException("Voucher description must be at least 5 characters.");
        }

        if (voucher.getDiscount() <= 0) {
            throw new BadRequestException("The discount must be more than 0.");
        }
        if (voucher.getDiscount() > 100) {
            throw new BadRequestException("The discount cannot be more than 100%.");
        }

        if (voucher.getStartDate() == null || voucher.getStartDate().trim().isEmpty()) {
            throw new BadRequestException("The start date must not be empty.");
        }

        if (voucher.getEndDate() == null || voucher.getEndDate().trim().isEmpty()) {
            throw new BadRequestException("The end date cannot be empty.");
        }

        try {
            LocalDateTime start = LocalDateTime.parse(voucher.getStartDate().replace(" ", "T"));
            LocalDateTime end = LocalDateTime.parse(voucher.getEndDate().replace(" ", "T"));
            if (start.isAfter(end)) {
                throw new BadRequestException("The start date must be earlier than the end date.");
            }
        } catch (Exception e) {
            throw new BadRequestException("Invalid date format. Use yyyy-MM-dd HH:mm:ss format.");
        }
    }
}
