package com.atm.DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * GiaoDichDAO - ghi lịch sử giao dịch vào bảng giaodich.
 */
public class GiaoDichDAO {

    /**
     * @param loaiGiaoDich  "Rut_Tien" | "Nap_Tien" | "Chuyen_Tien" | "Doi_PIN"
     * @param soTien        0 nếu là Doi_PIN
     * @param soTK          tài khoản liên kết
     * @param trangThai     "Thanh_Cong" | "That_Bai"
     */
    public void insert(String loaiGiaoDich, double soTien, String soTK, String trangThai) {
        String sql = """
                INSERT INTO giaodich (MaGD, LoaiGiaoDich, SoTien, ThoiGian, TrangThai, SoTK_LienKet)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        String maGD = "GD_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maGD);
            ps.setString(2, loaiGiaoDich);
            ps.setDouble(3, soTien);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(5, trangThai);
            ps.setString(6, soTK);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Không ném exception để không làm hỏng giao dịch chính
            System.err.println("⚠️ GiaoDichDAO.insert: " + e.getMessage());
        }
    }
}
