package queries;

import database.DB;
import models.Voucher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherQuery {
    public static List<Voucher> getAll() {
        List<Voucher> vouchers = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vouchers");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vouchers.add(mapVoucher(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data voucher: " + e.getMessage());
        }
        return vouchers;
    }

    public static Voucher findById(int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vouchers WHERE id = ?")) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapVoucher(rs);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data voucher ID " + id + ": " + e.getMessage());
        }
        return null;
    }

    public static boolean existsByCode(String code) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id FROM vouchers WHERE code = ?")) {

            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengecek kode voucher: " + e.getMessage());
        }
        return false;
    }

    public static boolean insert(Voucher v) {
        String sql = "INSERT INTO vouchers (code, description, discount, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, v.getCode());
            stmt.setString(2, v.getDescription());
            stmt.setDouble(3, v.getDiscount());
            stmt.setString(4, v.getStartDate());
            stmt.setString(5, v.getEndDate());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menambahkan voucher: " + e.getMessage());
        }
        return false;
    }

    public static boolean update(int id, Voucher v) {
        String sql = "UPDATE vouchers SET code = ?, description = ?, discount = ?, start_date = ?, end_date = ? WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, v.getCode());
            stmt.setString(2, v.getDescription());
            stmt.setDouble(3, v.getDiscount());
            stmt.setString(4, v.getStartDate());
            stmt.setString(5, v.getEndDate());
            stmt.setInt(6, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui voucher ID " + id + ": " + e.getMessage());
        }
        return false;
    }

    public static boolean delete(int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM vouchers WHERE id = ?")) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus voucher ID " + id + ": " + e.getMessage());
        }
        return false;
    }

    private static Voucher mapVoucher(ResultSet rs) throws SQLException {
        return new Voucher(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("description"),
                rs.getDouble("discount"),
                rs.getString("start_date"),
                rs.getString("end_date")
        );
    }
}
