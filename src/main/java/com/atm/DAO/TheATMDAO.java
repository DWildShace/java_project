package com.atm.DAO;

import java.sql.*;

/**
 * TheATMDAO - xác thực thẻ và tra cứu tài khoản liên kết.
 *
 * Trong quy trình ATM thực:
 *   1. Khách nhập số thẻ → findSoTKByThe(soThe) → lấy soTK liên kết
 *   2. Khách nhập PIN     → verifyPIN(soThe, pin) → xác thực
 */
public class TheATMDAO {

    /**
     * Lấy SoTK liên kết từ số thẻ.
     * @return soTK nếu thẻ tồn tại, null nếu không tìm thấy.
     */
    public String findSoTKByThe(String soThe) {
        String sql = "SELECT SoTK_LienKet FROM theatm WHERE SoThe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soThe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("SoTK_LienKet");
            }
        } catch (SQLException e) {
            throw new RuntimeException("TheATMDAO.findSoTKByThe: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Kiểm tra thẻ có tồn tại và đang hoạt động không (chưa khoá, chưa hết hạn).
     */
    public boolean isTheHoatDong(String soThe) {
        String sql = """
                SELECT TrangThai, NgayHetHan FROM theatm
                WHERE SoThe = ?
                """;
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soThe);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                String trangThai  = rs.getString("TrangThai");
                Timestamp hetHan  = rs.getTimestamp("NgayHetHan");
                boolean chuaKhoa  = "Hoat_Dong".equalsIgnoreCase(trangThai);
                boolean chuaHetHan = hetHan == null || hetHan.after(new Timestamp(System.currentTimeMillis()));
                return chuaKhoa && chuaHetHan;
            }
        } catch (SQLException e) {
            throw new RuntimeException("TheATMDAO.isTheHoatDong: " + e.getMessage(), e);
        }
    }

    /**
     * Xác thực PIN của thẻ.
     * @return true nếu PIN khớp, false nếu sai.
     */
    public boolean verifyPIN(String soThe, String pin) {
        String sql = "SELECT MaPIN FROM theatm WHERE SoThe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soThe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return pin.equals(rs.getString("MaPIN"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("TheATMDAO.verifyPIN: " + e.getMessage(), e);
        }
        return false;
    }

    /** Cập nhật số lần nhập sai PIN của thẻ. */
    public void updateSoLanNhapSai(String soThe, int soLan) {
        String sql = "UPDATE theatm SET SoLanNhapSai = ? WHERE SoThe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLan);
            ps.setString(2, soThe);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("TheATMDAO.updateSoLanNhapSai: " + e.getMessage(), e);
        }
    }

    /** Khoá thẻ khi nhập sai PIN quá số lần cho phép. */
    public void khoaThe(String soThe) {
        String sql = "UPDATE theatm SET TrangThai = 'Bi_Khoa' WHERE SoThe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soThe);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("TheATMDAO.khoaThe: " + e.getMessage(), e);
        }
    }

    /** Lấy số lần nhập sai PIN hiện tại. */
    public int getSoLanNhapSai(String soThe) {
        String sql = "SELECT SoLanNhapSai, HanNhapSaiToiDa FROM theatm WHERE SoThe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soThe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("SoLanNhapSai");
            }
        } catch (SQLException e) {
            throw new RuntimeException("TheATMDAO.getSoLanNhapSai: " + e.getMessage(), e);
        }
        return 0;
    }

    /** Lấy giới hạn nhập sai tối đa của thẻ (mặc định 3). */
    public int getHanNhapSaiToiDa(String soThe) {
        String sql = "SELECT HanNhapSaiToiDa FROM theatm WHERE SoThe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soThe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int han = rs.getInt("HanNhapSaiToiDa");
                    return rs.wasNull() ? 3 : han;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("TheATMDAO.getHanNhapSaiToiDa: " + e.getMessage(), e);
        }
        return 3;
    }
}
