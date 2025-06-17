package queries;

import models.Bookings;
import database.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingQuery {
    public static List<Bookings> getBookingsByVillaId(int villaId) {
        List<Bookings> list = new ArrayList<>();
        String sql = "SELECT b.* FROM bookings b JOIN room_types r ON b.room_type = r.id WHERE r.villa = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Bookings(
                        rs.getInt("id"), rs.getInt("customer"), rs.getInt("room_type"),
                        rs.getString("checkin_date"), rs.getString("checkout_date"),
                        rs.getInt("price"), rs.getInt("voucher"), rs.getInt("final_price"),
                        rs.getString("payment_status"), rs.getInt("has_checkedin") == 1,
                        rs.getInt("has_checkedout") == 1
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static List<Bookings> getBookingsByCustomerId(int customerId) {
        List<Bookings> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE customer = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Bookings(
                        rs.getInt("id"), rs.getInt("customer"), rs.getInt("room_type"),
                        rs.getString("checkin_date"), rs.getString("checkout_date"),
                        rs.getInt("price"), rs.getInt("voucher"), rs.getInt("final_price"),
                        rs.getString("payment_status"), rs.getInt("has_checkedin") == 1,
                        rs.getInt("has_checkedout") == 1
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static Bookings insertBooking(Bookings b) {
        String sql = "INSERT INTO bookings (customer, room_type, checkin_date, checkout_date, price, voucher, final_price, payment_status, has_checkedin, has_checkedout) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, b.getCustomer());
            stmt.setInt(2, b.getRoomType());
            stmt.setString(3, b.getCheckinDate());
            stmt.setString(4, b.getCheckoutDate());
            stmt.setInt(5, b.getPrice());
            stmt.setObject(6, b.getVoucherObj(), Types.INTEGER);
            stmt.setInt(7, b.getFinalPrice());
            stmt.setString(8, b.getPaymentStatus());
            stmt.setBoolean(9, b.isHasCheckedin());
            stmt.setBoolean(10, b.isHasCheckedout());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                b.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static Bookings getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Bookings(
                        rs.getInt("id"),
                        rs.getInt("customer"),
                        rs.getInt("room_type"),
                        rs.getString("checkin_date"),
                        rs.getString("checkout_date"),
                        rs.getInt("price"),
                        rs.getInt("voucher"),
                        rs.getInt("final_price"),
                        rs.getString("payment_status"),
                        rs.getInt("has_checkedin") == 1,
                        rs.getInt("has_checkedout") == 1
                );
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil booking ID " + bookingId + ": " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }
}
