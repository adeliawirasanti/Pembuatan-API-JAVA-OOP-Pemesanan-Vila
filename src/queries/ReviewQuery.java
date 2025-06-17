package queries;

import models.Reviews;
import database.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewQuery {

    public static List<Reviews> getReviewsByVillaId(int villaId) {
        List<Reviews> list = new ArrayList<>();
        String sql = "SELECT r.* FROM reviews r " +
                "JOIN bookings b ON r.booking = b.id " +
                "JOIN room_types rt ON b.room_type = rt.id " +
                "WHERE rt.villa = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Reviews(
                        rs.getInt("booking"),
                        rs.getInt("star"),
                        rs.getString("title"),
                        rs.getString("content")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil review vila: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static List<Reviews> getReviewsByCustomerId(int customerId) {
        List<Reviews> list = new ArrayList<>();
        String sql = "SELECT r.* FROM reviews r " +
                "JOIN bookings b ON r.booking = b.id " +
                "WHERE b.customer = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Reviews(
                        rs.getInt("booking"),
                        rs.getInt("star"),
                        rs.getString("title"),
                        rs.getString("content")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil review customer: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static Reviews insertReview(Reviews review) {
        String sql = "INSERT INTO reviews (booking, star, title, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, review.getBooking());
            stmt.setInt(2, review.getStar());
            stmt.setString(3, review.getTitle());
            stmt.setString(4, review.getContent());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return review; // Tidak ada ID auto-increment di tabel reviews, jadi cukup return objeknya
            }
        } catch (SQLException e) {
            System.err.println("Gagal menambahkan review: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
