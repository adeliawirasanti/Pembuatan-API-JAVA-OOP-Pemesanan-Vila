package utils;

import models.Villa;
import exceptions.BadRequestException;

public class VillaValidator {
    public static void validate(Villa villa) {
        if (villa.getName() == null || villa.getName().trim().isEmpty()) {
            throw new BadRequestException("Nama vila tidak boleh kosong.");
        }
        if (villa.getName().length() > 100) {
            throw new BadRequestException("Nama vila tidak boleh lebih dari 100 karakter.");
        }
        if (villa.getDescription() == null || villa.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Deskripsi vila tidak boleh kosong.");
        }
        if (villa.getDescription().length() < 10) {
            throw new BadRequestException("Deskripsi vila minimal 10 karakter.");
        }
        if (villa.getAddress() == null || villa.getAddress().trim().isEmpty()) {
            throw new BadRequestException("Alamat vila tidak boleh kosong.");
        }
        if (villa.getAddress().length() < 5) {
            throw new BadRequestException("Alamat vila minimal 5 karakter.");
        }
        if (!villa.getAddress().matches(".*[a-zA-Z0-9].*")) {
            throw new BadRequestException("Alamat vila tidak valid. Harus mengandung huruf atau angka.");
        }
    }
}
