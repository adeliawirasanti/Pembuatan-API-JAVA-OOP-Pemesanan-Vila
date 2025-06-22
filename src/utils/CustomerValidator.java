package utils;

import models.Customer;
import exceptions.BadRequestException;

import java.util.regex.Pattern;

public class CustomerValidator {

    private static final Pattern NAME_REGEX =
            Pattern.compile("^[a-zA-Z\\s]{2,50}$");

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_REGEX =
            Pattern.compile("^\\d{10,15}$");

    public static void validate(Customer customer) {
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new BadRequestException("Nama customer tidak boleh kosong.");
        }
        if (!NAME_REGEX.matcher(customer.getName().trim()).matches()) {
            throw new BadRequestException("Nama hanya boleh berisi huruf dan spasi (2–50 karakter).");
        }

        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email tidak boleh kosong.");
        }
        if (!EMAIL_REGEX.matcher(customer.getEmail().trim()).matches()) {
            throw new BadRequestException("Format email tidak valid.");
        }

        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            throw new BadRequestException("Nomor telepon tidak boleh kosong.");
        }
        if (!PHONE_REGEX.matcher(customer.getPhone().trim()).matches()) {
            throw new BadRequestException("Nomor telepon harus terdiri dari 10–15 digit angka.");
        }
    }
}
