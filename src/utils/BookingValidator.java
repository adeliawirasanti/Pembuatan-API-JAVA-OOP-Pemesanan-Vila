package utils;

import models.Booking;

public class BookingValidator {

    // Validasi format dan urutan tanggal checkin & checkout
    public static void validateDates(String checkin_date, String checkout_date) throws IllegalArgumentException {
        if (!checkin_date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}") ||
                !checkout_date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Format tanggal harus: YYYY-MM-DD hh:mm:ss");
        }
        if (checkin_date.compareTo(checkout_date) >= 0) {
            throw new IllegalArgumentException("checkin_date harus sebelum checkout_date");
        }
    }

    // Validasi atribut lain seperti price, payment_status, dll
    public static void validateOther(Booking booking) throws IllegalArgumentException {
        if (booking.getPrice() < 0) {
            throw new IllegalArgumentException("Price tidak boleh negatif");
        }
        if (booking.getFinal_price() < 0) {
            throw new IllegalArgumentException("Final price tidak boleh negatif");
        }
        if (booking.getPayment_status() == null ||
                !(booking.getPayment_status().equals("waiting") || booking.getPayment_status().equals("failed") || booking.getPayment_status().equals("success"))) {
            throw new IllegalArgumentException("Payment status harus salah satu dari: waiting, failed, success");
        }
        // Tambahkan validasi lain sesuai kebutuhan
    }
}
