package com.atm.DAO;

import com.atm.entity.TaiKhoan;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * TaiKhoanDAO - truy vấn bảng taikhoan + theatm.
 * PIN được lưu trong theatm.MaPIN (liên kết qua SoTK_LienKet).
 */
public class TaiKhoanDAO {

    /** Load tài khoản từ DB, JOIN theatm để lấy PIN và số lần nhập sai. */
    public TaiKhoan findBySoTK(String soTK) {
        String sql = """
                SELECT t.SoTK, t.SoDu, t.TrangThai, t.MaKH, t.NgayMo,
                       a.MaPIN, a.SoLanNhapSai, a.HanNhapSaiToiDa
                FROM taikhoan t
                LEFT JOIN theatm a ON a.SoTK_LienKet = t.SoTK
                WHERE t.SoTK = ?
                LIMIT 1
                """;
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soTK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("TaiKhoanDAO.findBySoTK: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Load tài khoản VỚI KHOÁ DÒNG (SELECT FOR UPDATE) trong một transaction.
     * Dùng cho các thao tác đọc-sửa-ghi nguyên tử để tránh race condition.
     *
     * @param conn Connection đang trong transaction (autoCommit=false)
     */
    public TaiKhoan findBySoTKForUpdate(Connection conn, String soTK) throws SQLException {
        String sql = """
                SELECT t.SoTK, t.SoDu, t.TrangThai, t.MaKH, t.NgayMo,
                       a.MaPIN, a.SoLanNhapSai, a.HanNhapSaiToiDa
                FROM taikhoan t
                LEFT JOIN theatm a ON a.SoTK_LienKet = t.SoTK
                WHERE t.SoTK = ?
                LIMIT 1
                FOR UPDATE
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soTK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    /** Cập nhật số dư với Connection có sẵn (dùng trong transaction). */
    public void updateSoDu(Connection conn, String soTK, double soDuMoi) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE taikhoan SET SoDu = ? WHERE SoTK = ?")) {
            ps.setDouble(1, soDuMoi);
            ps.setString(2, soTK);
            ps.executeUpdate();
        }
    }

    /** Cập nhật số dư sau giao dịch. */
    public void updateSoDu(String soTK, double soDuMoi) {
        execute("UPDATE taikhoan SET SoDu = ? WHERE SoTK = ?", ps -> {
            ps.setDouble(1, soDuMoi);
            ps.setString(2, soTK);
        });
    }

    /** Cập nhật trạng thái tài khoản (khoá/mở khoá) ở cả hai bảng. */
    public void updateTrangThai(String soTK, String trangThai) {
        String db = trangThai.replace(" ", "_");
        execute("UPDATE taikhoan SET TrangThai = ? WHERE SoTK = ?", ps -> {
            ps.setString(1, db); ps.setString(2, soTK);
        });
        execute("UPDATE theatm SET TrangThai = ? WHERE SoTK_LienKet = ?", ps -> {
            ps.setString(1, db); ps.setString(2, soTK);
        });
    }

    /** Cập nhật PIN mới trong theatm. */
    public void updateMaPin(String soTK, String pinMoi) {
        execute("UPDATE theatm SET MaPIN = ? WHERE SoTK_LienKet = ?", ps -> {
            ps.setString(1, pinMoi); ps.setString(2, soTK);
        });
    }

    /** Cập nhật số lần nhập sai PIN. */
    public void updateSoLanNhapSai(String soTK, int soLan) {
        execute("UPDATE theatm SET SoLanNhapSai = ? WHERE SoTK_LienKet = ?", ps -> {
            ps.setInt(1, soLan); ps.setString(2, soTK);
        });
    }

    // ── helpers ──────────────────────────────────────────────────────

    private TaiKhoan mapRow(ResultSet rs) throws SQLException {
        String soTK     = rs.getString("SoTK");
        double soDu     = rs.getDouble("SoDu");
        String trangThai = rs.getString("TrangThai");
        String maKH     = rs.getString("MaKH");
        String maPin    = rs.getString("MaPIN");
        Timestamp ts    = rs.getTimestamp("NgayMo");
        LocalDateTime ngayMo = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();
        int soLanSai    = rs.getInt("SoLanNhapSai");
        int hanSai      = rs.getInt("HanNhapSaiToiDa");
        if (rs.wasNull()) hanSai = 3;

        TaiKhoan tk = new TaiKhoan(soTK, soDu, trangThai, maKH, maPin, ngayMo);
        boolean biKhoa = "Bi_Khoa".equalsIgnoreCase(trangThai) || soLanSai >= hanSai;
        if (biKhoa) tk.khoaTaiKhoan();
        return tk;
    }

    @FunctionalInterface
    private interface PsSetter { void set(PreparedStatement ps) throws SQLException; }

    private void execute(String sql, PsSetter setter) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setter.set(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("TaiKhoanDAO: " + e.getMessage(), e);
        }
    }
}
