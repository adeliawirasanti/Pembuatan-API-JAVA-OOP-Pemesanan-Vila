package utils;

import models.Room;
import exceptions.BadRequestException;

import java.util.Arrays;
import java.util.List;

public class RoomValidator {
    private static final List<String> validBedSizes = Arrays.asList("double", "queen", "king");

    public static void validate(Room room) {
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            throw new BadRequestException("Nama room tidak boleh kosong.");
        }
        if (room.getQuantity() < 1) {
            throw new BadRequestException("Jumlah room minimal 1.");
        }
        if (room.getCapacity() < 1) {
            throw new BadRequestException("Kapasitas room minimal 1 orang.");
        }
        if (room.getPrice() <= 0) {
            throw new BadRequestException("Harga room harus lebih dari 0.");
        }
        if (room.getBed_size() == null || room.getBed_size().trim().isEmpty()) {
            throw new BadRequestException("Ukuran kasur tidak boleh kosong.");
        }
        if (!validBedSizes.contains(room.getBed_size().toLowerCase())) {
            throw new BadRequestException("Ukuran kasur harus salah satu dari: double, queen, king.");
        }
    }
}
