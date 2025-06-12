package queries;

import models.Villas;
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

    public static List<Villas> getAllVillas() {
        List<Villas> list = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Villas(
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), rs.getString("address")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static Villas getVillaById(int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Villas(rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), rs.getString("address"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static boolean insertVilla(Villas villa) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {
            stmt.setString(1, villa.getName());
            stmt.setString(2, villa.getDescription());
            stmt.setString(3, villa.getAddress());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean updateVilla(Villas villa) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            stmt.setString(1, villa.getName());
            stmt.setString(2, villa.getDescription());
            stmt.setString(3, villa.getAddress());
            stmt.setInt(4, villa.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean deleteVilla(int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static List<Villas> getAvailableVillas(String ci, String co) {
        List<Villas> list = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(AVAILABLE)) {
            stmt.setString(1, ci);
            stmt.setString(2, co);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Villas(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("address")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}