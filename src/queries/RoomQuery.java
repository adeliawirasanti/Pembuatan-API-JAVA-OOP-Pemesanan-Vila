package queries;

import models.Rooms;
import database.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomQuery {
    public static List<Rooms> getRoomsByVillaId(int villaId) {
        List<Rooms> list = new ArrayList<>();
        String sql = "SELECT * FROM room_types WHERE villa = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRoom(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean insertRoom(Rooms r) {
        String sql = "INSERT INTO room_types (villa, name, quantity, capacity, price, bed_size, has_desk, has_ac, has_tv, has_wifi, has_shower, has_hotwater, has_fridge) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            bindRoomParams(stmt, r);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean updateRoom(Rooms r) {
        String sql = "UPDATE room_types SET name=?, quantity=?, capacity=?, price=?, bed_size=?, has_desk=?, has_ac=?, has_tv=?, has_wifi=?, has_shower=?, has_hotwater=?, has_fridge=? WHERE id=? AND villa=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            bindRoomParams(stmt, r);
            stmt.setInt(13, r.getId());
            stmt.setInt(14, r.getVilla());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM room_types WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private static void bindRoomParams(PreparedStatement stmt, Rooms r) throws SQLException {
        stmt.setInt(1, r.getVilla());
        stmt.setString(2, r.getName());
        stmt.setInt(3, r.getQuantity());
        stmt.setInt(4, r.getCapacity());
        stmt.setInt(5, r.getPrice());
        stmt.setString(6, r.getBed_size());
        stmt.setInt(7, r.isHas_desk() ? 1 : 0);
        stmt.setInt(8, r.isHas_ac() ? 1 : 0);
        stmt.setInt(9, r.isHas_tv() ? 1 : 0);
        stmt.setInt(10, r.isHas_wifi() ? 1 : 0);
        stmt.setInt(11, r.isHas_shower() ? 1 : 0);
        stmt.setInt(12, r.isHas_hotwater() ? 1 : 0);
        stmt.setInt(13, r.isHas_fridge() ? 1 : 0);
    }

    private static Rooms mapRoom(ResultSet rs) throws SQLException {
        return new Rooms(
                rs.getInt("id"), rs.getInt("villa"), rs.getString("name"), rs.getInt("quantity"),
                rs.getInt("capacity"), rs.getInt("price"), rs.getString("bed_size"),
                rs.getInt("has_desk") == 1, rs.getInt("has_ac") == 1, rs.getInt("has_tv") == 1,
                rs.getInt("has_wifi") == 1, rs.getInt("has_shower") == 1,
                rs.getInt("has_hotwater") == 1, rs.getInt("has_fridge") == 1
        );
    }
}
