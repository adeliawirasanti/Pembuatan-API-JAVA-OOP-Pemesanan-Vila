package queries;

import database.DB;
import models.Voucher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherQuery {

    public static List<Voucher> getAll() throws SQLException {
        List<Voucher> list = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vouchers");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Voucher(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("description"),
                        rs.getDouble("discount"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                ));
            }
        }
        return list;
    }

    public static Voucher findById(int id) throws SQLException {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vouchers WHERE id = ?")) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
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
        }
        return null;
    }

    public static void insert(Voucher v) throws SQLException {
        String sql = "INSERT INTO vouchers (code, description, discount, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, v.getCode());
            stmt.setString(2, v.getDescription());
            stmt.setDouble(3, v.getDiscount());
            stmt.setString(4, v.getStart_date());
        }
    }
    public static boolean existsByCode(String code) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vouchers WHERE code = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}