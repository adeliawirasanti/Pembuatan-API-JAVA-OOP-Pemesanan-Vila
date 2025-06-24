package utils;

import models.Villa;
import exceptions.BadRequestException;

public class VillaValidator {
    public static void validate(Villa villa) {
        if (villa.getName() == null || villa.getName().trim().isEmpty()) {
            throw new BadRequestException("The villa name cannot be empty.");
        }
        if (villa.getName().length() > 100) {
            throw new BadRequestException("The villa name cannot be longer than 100 characters.");
        }
        if (villa.getDescription() == null || villa.getDescription().trim().isEmpty()) {
            throw new BadRequestException("The villa description should not be empty.");
        }
        if (villa.getDescription().length() < 10) {
            throw new BadRequestException("A villa description of at least 10 characters.");
        }
        if (villa.getAddress() == null || villa.getAddress().trim().isEmpty()) {
            throw new BadRequestException("The villa address must not be empty.");
        }
        if (villa.getAddress().length() < 5) {
            throw new BadRequestException("Villa address must be at least 5 characters.");
        }
        if (!villa.getAddress().matches(".*[a-zA-Z0-9].*")) {
            throw new BadRequestException("Invalid villa address. Must contain letters or numbers.");
        }
    }
}
