package utils;

import models.Room;
import exceptions.BadRequestException;

import java.util.Arrays;
import java.util.List;

public class RoomValidator {
    private static final List<String> validBedSizes = Arrays.asList("double", "queen", "king");

    public static void validate(Room room) {
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            throw new BadRequestException("The room name cannot be empty.");
        }
        if (room.getQuantity() < 1) {
            throw new BadRequestException("Minimum number of rooms is 1.");
        }
        if (room.getCapacity() < 1) {
            throw new BadRequestException("Room capacity is at least 1 person.");
        }
        if (room.getPrice() <= 0) {
            throw new BadRequestException("Room price must be greater than 0.");
        }
        if (room.getBed_size() == null || room.getBed_size().trim().isEmpty()) {
            throw new BadRequestException("The mattress size should not be empty.");
        }
        if (!validBedSizes.contains(room.getBed_size().toLowerCase())) {
            throw new BadRequestException("The mattress size must be one of: double, queen, king.");
        }
    }
}
