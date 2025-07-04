package queries;

import database.DB;
import models.Villa;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.time.format.DateTimeParseException;

public class VillaQuery {
    private static final String SELECT_ALL = "SELECT * FROM villas";
    private static final String SELECT_BY_ID = "SELECT * FROM villas WHERE id = ?";
    private static final String INSERT = "INSERT INTO villas (name, description, address) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM villas WHERE id = ?";

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

    public static List<Map<String, Object>> getAvailableVillas(String ci, String co) {
        List<Map<String, Object>> result = new ArrayList<>();

        LocalDate checkIn, checkOut;
        try {
            checkIn = LocalDate.parse(ci);
            checkOut = LocalDate.parse(co);
            if (!checkIn.isBefore(checkOut)) {
                throw new IllegalArgumentException("Check-in date must be before check-out date");
            }
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            System.err.println("Invalid date input: " + ex.getMessage());
            return result;
        }

        String sqlRoomTypes = "SELECT id AS room_id, villa, quantity FROM room_types";
        String sqlBookings = "SELECT room_type, checkin_date, checkout_date FROM bookings WHERE payment_status IN ('waiting', 'success')";

        try (Connection conn = DB.getConnection();
             PreparedStatement psRoomTypes = conn.prepareStatement(sqlRoomTypes);
             PreparedStatement psBookings = conn.prepareStatement(sqlBookings);
             ResultSet rsRoomTypes = psRoomTypes.executeQuery();
             ResultSet rsBookings = psBookings.executeQuery()) {

            Map<Integer, Integer> roomQuantities = new HashMap<>();
            Map<Integer, Integer> roomToVillaMap = new HashMap<>();

            while (rsRoomTypes.next()) {
                int roomId = rsRoomTypes.getInt("room_id");
                roomQuantities.put(roomId, rsRoomTypes.getInt("quantity"));
                roomToVillaMap.put(roomId, rsRoomTypes.getInt("villa"));
            }

            Map<Integer, Map<LocalDate, Integer>> bookingCounts = new HashMap<>();
            while (rsBookings.next()) {
                int roomId = rsBookings.getInt("room_type");
                LocalDate bCheckIn = rsBookings.getDate("checkin_date").toLocalDate();
                LocalDate bCheckOut = rsBookings.getDate("checkout_date").toLocalDate();

                for (LocalDate date = bCheckIn; date.isBefore(bCheckOut); date = date.plusDays(1)) {
                    bookingCounts.computeIfAbsent(roomId, k -> new HashMap<>())
                            .merge(date, 1, Integer::sum);
                }
            }

            Map<Integer, Set<Integer>> villaToAvailableRoomIds = new HashMap<>();

            for (Map.Entry<Integer, Integer> entry : roomQuantities.entrySet()) {
                int roomId = entry.getKey();
                int quantity = entry.getValue();
                Map<LocalDate, Integer> bookedDates = bookingCounts.getOrDefault(roomId, Collections.emptyMap());

                boolean isAvailable = true;
                for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
                    int bookedCount = bookedDates.getOrDefault(date, 0);
                    if (bookedCount >= quantity) {
                        isAvailable = false;
                        break;
                    }
                }

                if (isAvailable) {
                    int villaId = roomToVillaMap.get(roomId);
                    villaToAvailableRoomIds.computeIfAbsent(villaId, k -> new HashSet<>()).add(roomId);
                }
            }

            if (villaToAvailableRoomIds.isEmpty()) return result;

            Set<Integer> villaIds = villaToAvailableRoomIds.keySet();
            List<Villa> villas = getVillasByIds(conn, villaIds);

            for (Villa v : villas) {
                Map<String, Object> villaMap = new LinkedHashMap<>();
                villaMap.put("id", v.getId());
                villaMap.put("name", v.getName());
                villaMap.put("description", v.getDescription());
                villaMap.put("address", v.getAddress());
                Set<Integer> roomTypeIds = villaToAvailableRoomIds.get(v.getId());
                String ids = roomTypeIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                villaMap.put("available_room_type_ids", ids);
                result.add(villaMap);
            }

        } catch (SQLException e) {
            System.err.println("Error saat mengambil villa tersedia: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    private static List<Villa> getVillasByIds(Connection conn, Set<Integer> villaIds) throws SQLException {
        if (villaIds.isEmpty()) return Collections.emptyList();

        List<Villa> villas = new ArrayList<>();
        String placeholders = villaIds.stream().map(id -> "?").collect(Collectors.joining(","));
        String sql = "SELECT id, name, description, address FROM villas WHERE id IN (" + placeholders + ")";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int index = 1;
            for (Integer id : villaIds) {
                stmt.setInt(index++, id);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Villa villa = new Villa(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("address")
                    );
                    villas.add(villa);
                }
            }
        }

        return villas;
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