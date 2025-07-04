package queries;

import database.DB;
import models.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewQuery {

    public static List<Review> getReviewsByVillaId(int villaId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.* FROM reviews r " +
                "JOIN bookings b ON r.booking = b.id " +
                "JOIN room_types rt ON b.room_type = rt.id " +
                "WHERE rt.villa = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, villaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReview(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil review vila: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    public static List<Review> getReviewsByCustomerId(int customerId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.* FROM reviews r " +
                "JOIN bookings b ON r.booking = b.id " +
                "WHERE b.customer = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReview(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil review customer: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    public static Review insertReview(Review review) {
        String sql = "INSERT INTO reviews (booking, star, title, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, review.getBooking());
            stmt.setInt(2, review.getStar());
            stmt.setString(3, review.getTitle());
            stmt.setString(4, review.getContent());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return review;
            }
        } catch (SQLException e) {
            System.err.println("Gagal menambahkan review: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    private static Review mapReview(ResultSet rs) throws SQLException {
        return new Review(
                rs.getInt("booking"),
                rs.getInt("star"),
                rs.getString("title"),
                rs.getString("content")
        );
    }
}
