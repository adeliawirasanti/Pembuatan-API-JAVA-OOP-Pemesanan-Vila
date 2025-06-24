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
            throw new BadRequestException("The customer name cannot be empty.");
        }
        if (!NAME_REGEX.matcher(customer.getName().trim()).matches()) {
            throw new BadRequestException("Names can only contain letters and spaces (2-50 characters).");
        }

        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email cannot be empty.");
        }
        if (!EMAIL_REGEX.matcher(customer.getEmail().trim()).matches()) {
            throw new BadRequestException("Invalid email format.");
        }

        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            throw new BadRequestException("The phone number cannot be empty.");
        }
        if (!PHONE_REGEX.matcher(customer.getPhone().trim()).matches()) {
            throw new BadRequestException("Phone numbers should be 10-15 digit numbers.");
        }
    }
}
