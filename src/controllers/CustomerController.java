package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.Request;
import core.Response;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import models.Customer;
import models.Bookings;
import models.Reviews;
import queries.CustomerQuery;
import queries.BookingQuery;
import queries.ReviewQuery;
import utils.CustomerValidator;

import java.io.IOException;
import java.util.List;

public class CustomerController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getAllCustomers(Request req, Response res) {
        try {
            List<Customer> customers = CustomerQuery.getAllCustomers();
            res.setBody(mapper.writeValueAsString(customers));
            res.send(200);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal mengambil data customer."));
            res.send(500);
        }
    }

    public static void getCustomerById(Request req, Response res, int id) {
        try {
            Customer customer = CustomerQuery.getCustomerById(id);
            if (customer == null) throw new NotFoundException("Customer tidak ditemukan.");
            res.setBody(mapper.writeValueAsString(customer));
            res.send(200);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal mengambil detail customer."));
            res.send(500);
        }
    }

    public static void createCustomer(Request req, Response res) {
        try {
            Customer customer = mapper.readValue(req.getBody(), Customer.class);
            CustomerValidator.validate(customer);
            Customer created = CustomerQuery.insertCustomer(customer);
            res.setBody(mapper.writeValueAsString(created));
            res.send(201);
        } catch (BadRequestException | IOException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(400);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal membuat customer."));
            res.send(500);
        }
    }

    public static void updateCustomer(Request req, Response res, int id) {
        try {
            Customer existing = CustomerQuery.getCustomerById(id);
            if (existing == null) throw new NotFoundException("Customer tidak ditemukan.");

            Customer customer = mapper.readValue(req.getBody(), Customer.class);
            CustomerValidator.validate(customer);

            if (CustomerQuery.updateCustomer(id, customer)) {
                res.setBody(jsonMessage("Customer berhasil diperbarui."));
                res.send(200);
            } else {
                throw new RuntimeException("Gagal memperbarui customer.");
            }
        } catch (BadRequestException | IOException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(400);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (Exception e) {
            res.setBody(jsonError("Terjadi kesalahan server."));
            res.send(500);
        }
    }

    public static void deleteCustomer(Request req, Response res, int id) {
        try {
            Customer existing = CustomerQuery.getCustomerById(id);
            if (existing == null) throw new NotFoundException("Customer tidak ditemukan.");

            if (CustomerQuery.deleteCustomer(id)) {
                res.setBody(jsonMessage("Customer berhasil dihapus."));
                res.send(200);
            } else {
                throw new RuntimeException("Gagal menghapus customer.");
            }
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (Exception e) {
            res.setBody(jsonError("Terjadi kesalahan server."));
            res.send(500);
        }
    }

    public static void getBookingsByCustomerId(Request req, Response res, int customerId) {
        try {
            Customer customer = CustomerQuery.getCustomerById(customerId);
            if (customer == null) throw new NotFoundException("Customer tidak ditemukan.");

            List<Bookings> bookings = BookingQuery.getBookingsByCustomerId(customerId);
            res.setBody(mapper.writeValueAsString(bookings));
            res.send(200);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal mengambil data booking customer."));
            res.send(500);
        }
    }

    public static void getReviewsByCustomerId(Request req, Response res, int customerId) {
        try {
            Customer customer = CustomerQuery.getCustomerById(customerId);
            if (customer == null) throw new NotFoundException("Customer tidak ditemukan.");

            List<Reviews> reviews = ReviewQuery.getReviewsByCustomerId(customerId);
            res.setBody(mapper.writeValueAsString(reviews));
            res.send(200);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal mengambil data review customer."));
            res.send(500);
        }
    }

    public static void createBookingForCustomer(Request req, Response res, int customerId) {
        try {
            Customer customer = CustomerQuery.getCustomerById(customerId);
            if (customer == null) throw new NotFoundException("Customer tidak ditemukan.");

            Bookings booking = mapper.readValue(req.getBody(), Bookings.class);
            booking.setCustomer(customerId);

            if (booking.getPaymentStatus() == null) {
                booking.setPaymentStatus("waiting");
            }

            if (booking.getFinalPrice() == 0) {
                booking.setFinalPrice(booking.getPrice());
            }

            Bookings created = BookingQuery.insertBooking(booking);
            res.setBody(mapper.writeValueAsString(created));
            res.send(201);
        } catch (BadRequestException | IOException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(400);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal membuat booking."));
            res.send(500);
        }
    }

    public static void createReviewForBooking(Request req, Response res, int customerId, int bookingId) {
        try {
            Customer customer = CustomerQuery.getCustomerById(customerId);
            if (customer == null) throw new NotFoundException("Customer tidak ditemukan.");

            Bookings booking = BookingQuery.getBookingById(bookingId);
            if (booking == null || booking.getCustomer() != customerId) {
                throw new NotFoundException("Booking tidak ditemukan untuk customer ini.");
            }

            Reviews review = mapper.readValue(req.getBody(), Reviews.class);
            review.setBooking(bookingId);

            Reviews created = ReviewQuery.insertReview(review);
            if (created == null) {
                throw new RuntimeException("Gagal menambahkan review.");
            }

            res.setBody(mapper.writeValueAsString(created));
            res.send(201);
        } catch (BadRequestException | IOException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(400);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(404);
        } catch (Exception e) {
            res.setBody(jsonError("Gagal menambahkan review."));
            res.send(500);
        }
    }

    private static String jsonMessage(String msg) {
        return String.format("{\"message\":\"%s\"}", msg);
    }

    private static String jsonError(String msg) {
        return String.format("{\"error\":\"%s\"}", msg);
    }
}