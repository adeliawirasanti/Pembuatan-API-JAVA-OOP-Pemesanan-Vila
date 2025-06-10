package queries;

import models.Rooms;
import database.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomQuery {

    public static List<Rooms> getRoomsByVillaId(int villaId) {
        List<Rooms> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room_types WHERE villa = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rooms.add(new Rooms(
                        rs.getInt("id"),
                        rs.getInt("villa"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getInt("capacity"),
                        rs.getInt("price"),
                        rs.getString("bed_size"),
                        rs.getInt("has_desk") == 1,
                        rs.getInt("has_ac") == 1,
                        rs.getInt("has_tv") == 1,
                        rs.getInt("has_wifi") == 1,
                        rs.getInt("has_shower") == 1,
                        rs.getInt("has_hotwater") == 1,
                        rs.getInt("has_fridge") == 1
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public static boolean insertRoom(Rooms room) {
        String sql = "INSERT INTO room_types (villa, name, quantity, capacity, price, bed_size, has_desk, has_ac, has_tv, has_wifi, has_shower, has_hotwater, has_fridge) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, room.getVilla());
            stmt.setString(2, room.getName());
            stmt.setInt(3, room.getQuantity());
            stmt.setInt(4, room.getCapacity());
            stmt.setInt(5, room.getPrice());
            stmt.setString(6, room.getBed_size());
            stmt.setInt(7, room.isHas_desk() ? 1 : 0);
            stmt.setInt(8, room.isHas_ac() ? 1 : 0);
            stmt.setInt(9, room.isHas_tv() ? 1 : 0);
            stmt.setInt(10, room.isHas_wifi() ? 1 : 0);
            stmt.setInt(11, room.isHas_shower() ? 1 : 0);
            stmt.setInt(12, room.isHas_hotwater() ? 1 : 0);
            stmt.setInt(13, room.isHas_fridge() ? 1 : 0);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateRoom(Rooms room) {
        String sql = "UPDATE room_types SET name = ?, quantity = ?, capacity = ?, price = ?, bed_size = ?, has_desk = ?, has_ac = ?, has_tv = ?, has_wifi = ?, has_shower = ?, has_hotwater = ?, has_fridge = ? WHERE id = ? AND villa = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getQuantity());
            stmt.setInt(3, room.getCapacity());
            stmt.setInt(4, room.getPrice());
            stmt.setString(5, room.getBed_size());
            stmt.setInt(6, room.isHas_desk() ? 1 : 0);
            stmt.setInt(7, room.isHas_ac() ? 1 : 0);
            stmt.setInt(8, room.isHas_tv() ? 1 : 0);
            stmt.setInt(9, room.isHas_wifi() ? 1 : 0);
            stmt.setInt(10, room.isHas_shower() ? 1 : 0);
            stmt.setInt(11, room.isHas_hotwater() ? 1 : 0);
            stmt.setInt(12, room.isHas_fridge() ? 1 : 0);
            stmt.setInt(13, room.getId());
            stmt.setInt(14, room.getVilla());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM room_types WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
