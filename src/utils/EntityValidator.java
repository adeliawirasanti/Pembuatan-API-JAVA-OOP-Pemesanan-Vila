package utils;

import models.Villas;
import queries.VillaQuery;
import exceptions.NotFoundException;

public class EntityValidator {
    public static Villas checkVillaExists(int villaId) {
        Villas villa = VillaQuery.getVillaById(villaId);
        if (villa == null) {
            throw new NotFoundException("Villa dengan ID " + villaId + " tidak ditemukan.");
        }
        return villa;
    }
}