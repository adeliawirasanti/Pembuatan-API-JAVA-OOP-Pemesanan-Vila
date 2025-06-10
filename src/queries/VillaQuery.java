package queries;

import models.Villas;
import database.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VillaQuery {

    public static List<Villas> getAllVillas() {
        List<Villas> villas = new ArrayList<>();
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT * FROM villas";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                villas.add(new Villas(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return villas;
    }

    public static boolean insertVilla(Villas villa) {
        try (Connection conn = DB.getConnection()) {
            String sql = "INSERT INTO villas (name, description, address) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, villa.getName());
            stmt.setString(2, villa.getDescription());
            stmt.setString(3, villa.getAddress());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateVilla(Villas villa) {
        String sql = "UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, villa.getName());
            stmt.setString(2, villa.getDescription());
            stmt.setString(3, villa.getAddress());
            stmt.setInt(4, villa.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteVilla(int id) {
        String sql = "DELETE FROM villas WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Villas getVillaById(int id) {
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT * FROM villas WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Villas(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Villas> getAvailableVillas(String ci, String co) {
        List<Villas> villas = new ArrayList<>();
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT * FROM villas WHERE id NOT IN (" +
                    "SELECT villa_id FROM bookings WHERE (? < check_out_date AND ? > check_in_date))";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ci);
            stmt.setString(2, co);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                villas.add(new Villas(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return villas;
    }
}
