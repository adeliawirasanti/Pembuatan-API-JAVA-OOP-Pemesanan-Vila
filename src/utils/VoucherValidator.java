package utils;

import models.Voucher;

public class VoucherValidator {

    public static String validate(Voucher v) {
        if (v.getCode() == null || v.getCode().trim().isEmpty()) {
            return "Code cannot be empty";
        }

        if (v.getDiscount() <= 0 || v.getDiscount() > 100) {
            return "Discount must be between 0 and 100";
        }

        if (v.getStart_date() == null || v.getEnd_date() == null ||
                !v.getStart_date().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}") ||
                !v.getEnd_date().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            return "Invalid date format. Use YYYY-MM-DD hh:mm:ss";
        }

        if (v.getStart_date().compareTo(v.getEnd_date()) > 0) {
            return "start_date must be before end_date";
        }

        return null;
    }
}
