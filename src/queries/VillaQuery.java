package queries;

import models.Villa;
import database.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VillaQuery {
    private static final String SELECT_ALL = "SELECT * FROM villas";
    private static final String SELECT_BY_ID = "SELECT * FROM villas WHERE id = ?";
    private static final String INSERT = "INSERT INTO villas (name, description, address) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM villas WHERE id = ?";
    private static final String AVAILABLE = "SELECT * FROM villas WHERE id NOT IN (SELECT villa_id FROM bookings WHERE (? < check_out_date AND ? > check_in_date))";

    public static List<Villa> getAllVillas() {
        List<Villa> list = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Villa(
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil semua data villa: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    public static Villa getVillaById(int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Villa(rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), rs.getString("address"));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data villa ID " + id + ": " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    public static boolean insertVilla(Villa villa) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {
            stmt.setString(1, villa.getName());
            stmt.setString(2, villa.getDescription());
            stmt.setString(3, villa.getAddress());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menambahkan data villa: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    public static boolean updateVilla(Villa villa) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            stmt.setString(1, villa.getName());
            stmt.setString(2, villa.getDescription());
            stmt.setString(3, villa.getAddress());
            stmt.setInt(4, villa.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui data villa ID " + villa.getId() + ": " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    public static boolean deleteVilla(int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus villa ID " + id + ": " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    public static List<Villa> getAvailableVillas(String ci, String co) {
        List<Villa> list = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(AVAILABLE)) {
            stmt.setString(1, ci);
            stmt.setString(2, co);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Villa(
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil villa yang tersedia antara " + ci + " dan " + co + ": " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }
}
