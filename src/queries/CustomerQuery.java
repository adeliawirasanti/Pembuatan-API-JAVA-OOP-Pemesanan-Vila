package queries;

import database.DB;
import models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerQuery {
    private static final String SELECT_ALL = "SELECT * FROM customers";
    private static final String SELECT_BY_ID = "SELECT * FROM customers WHERE id = ?";
    private static final String INSERT = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM customers WHERE id = ?";

    public static List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil semua data customer: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    public static Customer getCustomerById(int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil customer ID " + id + ": " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    public static Customer insertCustomer(Customer customer) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                customer.setId(rs.getInt(1));
                return customer;
            }
        } catch (SQLException e) {
            System.err.println("Gagal menambahkan customer baru: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    public static boolean updateCustomer(int id, Customer customer) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            stmt.setInt(4, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui customer ID " + id + ": " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    public static boolean deleteCustomer(int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus customer ID " + id + ": " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }
}
