package queries;

import models.Reviews;
import database.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewQuery {
    public static List<Reviews> getReviewsByVillaId(int villaId) {
        List<Reviews> list = new ArrayList<>();
        String sql = "SELECT r.* FROM reviews r JOIN bookings b ON r.booking = b.id JOIN room_types rt ON b.room_type = rt.id WHERE rt.villa = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Reviews(
                        rs.getInt("booking"), rs.getInt("star"), rs.getString("title"), rs.getString("content")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}