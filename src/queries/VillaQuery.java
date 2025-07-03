package queries;

import database.DB;
import models.Villa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VillaQuery {
    private static final String SELECT_ALL = "SELECT * FROM villas";
    private static final String SELECT_BY_ID = "SELECT * FROM villas WHERE id = ?";
    private static final String INSERT = "INSERT INTO villas (name, description, address) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM villas WHERE id = ?";

    // Query diperbaiki untuk cek villa yang tersedia berdasarkan tanggal check-in & check-out
    private static final String AVAILABLE =
            "SELECT DISTINCT v.* FROM villas v " +
                    "JOIN room_types r ON v.id = r.villa " +
                    "WHERE r.id NOT IN (" +
                    "  SELECT b.room_type FROM bookings b " +
                    "  WHERE NOT (b.checkout_date <= ? OR b.checkin_date >= ?)" +
                    ")";

    public static List<Villa> getAllVillas() {
        List<Villa> list = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapVilla(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil semua data villa: " + e.getMessage());
        }
        return list;
    }

    public static Villa getVillaById(int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapVilla(rs);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data villa ID " + id + ": " + e.getMessage());
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
            System.err.println("Gagal memperbarui villa ID " + villa.getId() + ": " + e.getMessage());
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
        }
        return false;
    }

    // Method untuk mengambil villa yang tersedia berdasarkan tanggal check-in (ci) dan check-out (co)
    public static List<Villa> getAvailableVillas(String ci, String co) {
        List<Villa> list = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(AVAILABLE)) {

            System.out.println("Check-in: " + ci);
            System.out.println("Check-out: " + co);

            // Urutan parameter sesuai query yang sudah diperbaiki
            stmt.setString(1, ci); // b.checkout_date <= ?
            stmt.setString(2, co); // b.checkin_date >= ?

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapVilla(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil villa tersedia: " + e.getMessage());
        }
        return list;
    }

    private static Villa mapVilla(ResultSet rs) throws SQLException {
        return new Villa(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("address")
        );
    }
}
