package queries;

import models.Bookings;
import database.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingQuery {
    public static List<Bookings> getBookingsByVillaId(int villaId) {
        List<Bookings> bookings = new ArrayList<>();
        try (Connection conn = DB.getConnection()) {
            String sql =
                "SELECT b.* " +
                "FROM bookings b " +
                "JOIN room_types r ON b.room_type = r.id " +
                " WHERE r.villa = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(new Bookings(
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
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
