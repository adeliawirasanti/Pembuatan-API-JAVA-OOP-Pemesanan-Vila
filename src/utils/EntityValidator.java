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
            throw new RuntimeException("Failed to retrieve villa data.", e);
        }

        if (villa == null) {
            throw new NotFoundException("Villa with ID " + villaId + " was not found.");
        }
        return villa;
    }

    public static Customer checkCustomerExists(int customerId) {
        Customer customer = null;
        try {
            customer = CustomerQuery.getCustomerById(customerId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve customer data.", e);
        }

        if (customer == null) {
            throw new NotFoundException("Customer with ID " + customerId + " was not found.");
        }
        return customer;
    }
}

