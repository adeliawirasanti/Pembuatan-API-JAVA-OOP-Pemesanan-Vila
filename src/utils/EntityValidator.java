package utils;

import models.Villa;
import models.Customer;
import queries.VillaQuery;
import queries.CustomerQuery;
import exceptions.NotFoundException;

public class EntityValidator {

    public static Villa checkVillaExists(int villaId) {
        Villa villa = null;
        try {
            villa = VillaQuery.getVillaById(villaId);
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengambil data villa.", e);
        }

        if (villa == null) {
            throw new NotFoundException("Villa dengan ID " + villaId + " tidak ditemukan.");
        }
        return villa;
    }

    public static Customer checkCustomerExists(int customerId) {
        Customer customer = null;
        try {
            customer = CustomerQuery.getCustomerById(customerId);
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengambil data customer.", e);
        }

        if (customer == null) {
            throw new NotFoundException("Customer dengan ID " + customerId + " tidak ditemukan.");
        }
        return customer;
    }
}

