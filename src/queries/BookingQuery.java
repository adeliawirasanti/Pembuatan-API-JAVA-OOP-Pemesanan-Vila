package queries;

import database.DB;
import models.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingQuery {

    public static List<Booking> getBookingsByVillaId(int villaId) {
        String sql = "SELECT b.* FROM bookings b " +
                "JOIN room_types r ON b.room_type = r.id " +
                "WHERE r.villa = ?";
        return executeBookingListQuery(sql, villaId);
    }

    public static List<Booking> getBookingsByCustomerId(int customerId) {
        String sql = "SELECT * FROM bookings WHERE customer = ?";
        return executeBookingListQuery(sql, customerId);
    }

    public static Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapBooking(rs);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil booking ID " + bookingId + ": " + e.getMessage());
        }
        return null;
    }

    public static Booking insertBooking(Booking b) {
        String sql = "INSERT INTO bookings (" +
                "customer, room_type, checkin_date, checkout_date, " +
                "price, voucher, final_price, payment_status, has_checkedin, has_checkedout" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, b.getCustomer());
            stmt.setInt(2, b.getRoom_type());
            stmt.setString(3, b.getCheckin_date());
            stmt.setString(4, b.getCheckout_date());
            stmt.setInt(5, b.getPrice());
            stmt.setObject(6, b.getVoucher() == 0 ? null : b.getVoucher(), Types.INTEGER);
            stmt.setInt(7, b.getFinal_price());
            stmt.setString(8, b.getPayment_status());
            stmt.setBoolean(9, b.isHas_checkedin());
            stmt.setBoolean(10, b.isHas_checkedout());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) b.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Gagal menambahkan booking: " + e.getMessage());
        }
        return b;
    }

    private static List<Booking> executeBookingListQuery(String sql, int param) {
        List<Booking> list = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapBooking(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data booking: " + e.getMessage());
        }
        return list;
    }

    private static Booking mapBooking(ResultSet rs) throws SQLException {
        return new Booking(
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
}