package controllers;

import core.Request;
import core.Response;
import exceptions.NotFoundException;
import models.Booking;
import models.Customer;
import models.Review;
import queries.BookingQuery;
import queries.CustomerQuery;
import queries.ReviewQuery;
import utils.AuthUtil;
import utils.CustomerValidator;
import utils.BookingValidator;


import java.util.List;

public class CustomerController extends BaseController {

    // === GET Methods ===

    public static void getAllCustomers(Request req, Response res) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            List<Customer> customers = CustomerQuery.getAllCustomers();
            sendJsonWithMessage(res, "Customers retrieved successfully.", customers, 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    public static void getCustomerById(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Customer customer = CustomerQuery.getCustomerById(id);
            if (customer == null) throw new NotFoundException("Customer not found.");
            sendJsonWithMessage(res, "Customer retrieved successfully.", customer, 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    public static void getBookingsByCustomerId(Request req, Response res, int customerId) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Customer customer = CustomerQuery.getCustomerById(customerId);
            if (customer == null) throw new NotFoundException("Customer not found.");

            List<Booking> bookings = BookingQuery.getBookingsByCustomerId(customerId);
            sendJsonWithMessage(res, "Bookings retrieved successfully.", bookings, 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    public static void getReviewsByCustomerId(Request req, Response res, int customerId) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Customer customer = CustomerQuery.getCustomerById(customerId);
            if (customer == null) throw new NotFoundException("Customer not found.");

            List<Review> reviews = ReviewQuery.getReviewsByCustomerId(customerId);
            sendJsonWithMessage(res, "Reviews retrieved successfully.", reviews, 200);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === POST Methods ===

    public static void createCustomer(Request req, Response res) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Customer customer = mapper.readValue(req.getBody(), Customer.class);
            CustomerValidator.validate(customer);
            Customer created = CustomerQuery.insertCustomer(customer);
            sendJsonWithMessage(res, "Customer successfully created.", created, 201);
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    public static void createBookingForCustomer(Request req, Response res, int customerId) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Customer customer = CustomerQuery.getCustomerById(customerId);
            if (customer == null) throw new NotFoundException("Customer not found.");

            Booking booking = mapper.readValue(req.getBody(), Booking.class);
            booking.setCustomer(customerId);

            if (booking.getPaymentStatus() == null)
                booking.setPaymentStatus("waiting");

            if (booking.getFinalPrice() == 0)
                booking.setFinalPrice(booking.getPrice());

            // **Tambahkan validasi disini**
            BookingValidator.validateDates(booking.getCheckin_date(), booking.getCheckout_date());
            BookingValidator.validateOther(booking);


            Booking created = BookingQuery.insertBooking(booking);
            sendJsonWithMessage(res, "Booking successfully created.", created, 201);
        } catch (IllegalArgumentException e) {
            res.setBody("{\"error\":\"" + e.getMessage() + "\"}");
            res.send(400);
        } catch (Exception e) {
            handleException(res, e);
        }
    }


    // === PUT Methods ===

    public static void updateCustomer(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Customer existing = CustomerQuery.getCustomerById(id);
            if (existing == null) throw new NotFoundException("Customer not found.");

            Customer customer = mapper.readValue(req.getBody(), Customer.class);
            CustomerValidator.validate(customer);

            if (CustomerQuery.updateCustomer(id, customer)) {
                sendJsonWithMessage(res, "Customer was successfully updated.", customer, 200);
            } else {
                throw new RuntimeException("Failed to update customer.");
            }
        } catch (Exception e) {
            handleException(res, e);
        }
    }

    // === DELETE Methods ===

    public static void deleteCustomer(Request req, Response res, int id) {
        if (!AuthUtil.authorizeOrAbort(req, res)) return;

        try {
            Customer existing = CustomerQuery.getCustomerById(id);
            if (existing == null) throw new NotFoundException("Customer not found.");

            if (CustomerQuery.deleteCustomer(id)) {
                sendJsonWithMessage(res, "Customer was successfully deleted.", null, 200);
            } else {
                throw new RuntimeException("Failed to delete customer.");
            }
        } catch (Exception e) {
            handleException(res, e);
        }
    }
}
