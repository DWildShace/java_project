package com.atm.service;

import com.atm.DAO.DatabaseConnection;
import com.atm.DAO.GiaoDichDAO;
import com.atm.DAO.TaiKhoanDAO;
import com.atm.DAO.TheATMDAO;
import com.atm.dto.request.ChuyenTienRequest;
import com.atm.dto.request.DangNhapRequest;
import com.atm.dto.request.DoiPINRequest;
import com.atm.dto.request.NapTienRequest;
import com.atm.dto.request.RutTienRequest;
import com.atm.dto.response.KetQuaResponse;
import com.atm.dto.response.TaiKhoanResponse;
import com.atm.entity.TaiKhoan;
import com.atm.exception.ChuyenKhoanThatBaiException;
import com.atm.exception.KhongDuSoDuException;
import com.atm.exception.SaiPinException;
import com.atm.exception.SoTienKhongHopLeException;
import com.atm.exception.TaiKhoanBiKhoaException;
import com.atm.exception.VuotHanMucRutTienException;
import com.atm.util.Utils;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ATMService {

    public static final double HAN_MUC_RUT_TIEN_MOI_NGAY = 20_000_000;

    // ── DAO layer ─────────────────────────────────────────────────────
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private final TheATMDAO   theATMDAO   = new TheATMDAO();
    private final GiaoDichDAO giaoDichDAO = new GiaoDichDAO();

    /** Theo dõi hạn mức rút trong ngày (in-memory, reset khi khởi động lại). */
    private final Map<String, Double> tongTienRutTheoNgay = new HashMap<>();

    // ════════════════════════════════════════════════════════════════
    //  LEGACY STUBS  (DemoConsole dùng — chỉ thao tác in-memory)
    // ════════════════════════════════════════════════════════════════

    /** @deprecated chỉ dành cho DemoConsole. UI nên dùng DAO trực tiếp. */
    @Deprecated
    public void themTaiKhoan(TaiKhoan taiKhoan) { /* no-op — data comes from DB */ }

    /** @deprecated chỉ dành cho DemoConsole. UI nên dùng chuyenTienDTO(). */
    @Deprecated
    public String chuyenTien(TaiKhoan taiKhoanNguon, String pinNhap,
                             String soTienNhap, String soTaiKhoanDich) {
        try {
            kiemTraTaiKhoanCoTheGiaoDich(taiKhoanNguon);
            taiKhoanNguon.kiemTraPIN(pinNhap);
            double soTien = chuyenDoiSoTien(soTienNhap, "chuyen");
            kiemTraSoDu(taiKhoanNguon, soTien);
            // Tìm tài khoản đích từ DB (demo vẫn kết nối được)
            TaiKhoan taiKhoanDich = taiKhoanDAO.findBySoTK(soTaiKhoanDich);
            if (taiKhoanDich == null)
                throw new ChuyenKhoanThatBaiException("Loi: Tai khoan dich khong ton tai.");
            taiKhoanNguon.setSoDu(taiKhoanNguon.getSoDu() - soTien);
            taiKhoanDich.setSoDu(taiKhoanDich.getSoDu() + soTien);
            return "Chuyen tien thanh cong: " + Utils.formatCurrency(soTien) + ".";
        } catch (TaiKhoanBiKhoaException | SaiPinException | SoTienKhongHopLeException
                 | KhongDuSoDuException | ChuyenKhoanThatBaiException e) {
            return e.getMessage();
        }
    }

    // ════════════════════════════════════════════════════════════════
    //  DTO-BASED API  (UI giao tiếp qua layer này)
    // ════════════════════════════════════════════════════════════════

    /**
     * Đăng nhập theo luồng ATM đúng:
     *   1. Kiểm tra số thẻ tồn tại và đang hoạt động.
     *   2. Xác thực PIN của thẻ (có giới hạn số lần sai).
     *   3. Tra soTK liên kết → load TaiKhoan → trả session DTO.
     *
     * @return KetQuaResponse chứa thongBao lỗi hoặc "ok".
     *         Gọi thêm layThongTinTaiKhoanBySoThe(soThe) để lấy session DTO.
     */
    public KetQuaResponse dangNhap(DangNhapRequest req) {
        String soThe = req.soThe();

        // Bước 1 — Thẻ có tồn tại không?
        if (!theATMDAO.isTheHoatDong(soThe)) {
            return KetQuaResponse.loi("The ATM khong ton tai hoac da bi khoa/het han.");
        }

        // Bước 2 — Kiểm tra số lần nhập sai còn lại
        int soLanSai = theATMDAO.getSoLanNhapSai(soThe);
        int hanToiDa = theATMDAO.getHanNhapSaiToiDa(soThe);
        if (soLanSai >= hanToiDa) {
            theATMDAO.khoaThe(soThe);
            return KetQuaResponse.loi("The da bi khoa do nhap sai PIN qua " + hanToiDa + " lan.");
        }

        // Bước 3 — Xác thực PIN
        if (!theATMDAO.verifyPIN(soThe, req.pin())) {
            int lanSaiMoi = soLanSai + 1;
            theATMDAO.updateSoLanNhapSai(soThe, lanSaiMoi);
            if (lanSaiMoi >= hanToiDa) {
                theATMDAO.khoaThe(soThe);
                return KetQuaResponse.loi("Sai PIN. The da bi khoa sau " + hanToiDa + " lan nhap sai.");
            }
            int conLai = hanToiDa - lanSaiMoi;
            return KetQuaResponse.loi("Sai ma PIN. Con " + conLai + " lan thu.");
        }

        // Bước 4 — PIN đúng → reset đếm sai
        theATMDAO.updateSoLanNhapSai(soThe, 0);

        // Bước 5 — Load TaiKhoan liên kết
        String soTK = theATMDAO.findSoTKByThe(soThe);
        TaiKhoan taiKhoan = taiKhoanDAO.findBySoTK(soTK);
        if (taiKhoan == null)
            return KetQuaResponse.loi("Tai khoan lien ket voi the khong ton tai.");
        if (taiKhoan.isBiKhoa())
            return KetQuaResponse.loi("Tai khoan da bi khoa. Vui long lien he ngan hang.");

        return KetQuaResponse.ok(soTK); // trả soTK để UI gọi layThongTinTaiKhoan
    }

    /**
     * Sau khi dangNhap() trả ok, UI dùng method này để lấy session DTO.
     * soTK lấy từ KetQuaResponse.thongBao() khi đăng nhập thành công.
     */
    public TaiKhoanResponse layThongTinTaiKhoanBySoThe(String soThe) {
        String soTK = theATMDAO.findSoTKByThe(soThe);
        if (soTK == null) return null;
        return layThongTinTaiKhoan(soTK);
    }

    /** Lấy thông tin tài khoản từ DB (không PIN). */
    public TaiKhoanResponse layThongTinTaiKhoan(String soTK) {
        TaiKhoan tk = taiKhoanDAO.findBySoTK(soTK);
        if (tk == null) return null;
        return chuyenSangDTO(tk);
    }

    /**
     * Rút tiền: dùng transaction + SELECT FOR UPDATE để đảm bảo
     * thao tác đọc-sửa-ghi là NGUYÊN Tử (thread-safe).
     */
    public KetQuaResponse rutTienDTO(RutTienRequest req) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false); // bắt đầu transaction
            try {
                // Đọc + KHOÁ DÒNG — thread khác sẽ bị block ở đây
                TaiKhoan taiKhoan = taiKhoanDAO.findBySoTKForUpdate(conn, req.soTK());
                if (taiKhoan == null) {
                    conn.rollback();
                    return KetQuaResponse.loi("Tai khoan khong ton tai.");
                }

                String ketQua = rutTien(taiKhoan, req.pin(), req.soTienNhap());
                boolean ok    = ketQua.startsWith("Rut tien thanh cong");

                if (ok) {
                    // Ghi số dư mới trong cùng transaction
                    taiKhoanDAO.updateSoDu(conn, req.soTK(), taiKhoan.getSoDu());
                    conn.commit();
                    giaoDichDAO.insert("Rut_Tien", parseSafe(req.soTienNhap()), req.soTK(), "Thanh_Cong");
                } else {
                    conn.rollback();
                    persistPinState(taiKhoan);
                }
                return ok ? KetQuaResponse.ok(ketQua) : KetQuaResponse.loi(ketQua.replace("Loi: ", ""));
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            return KetQuaResponse.loi("Loi he thong: " + e.getMessage());
        }
    }

    /**
     * Nạp tiền: dùng transaction + SELECT FOR UPDATE (thread-safe).
     */
    public KetQuaResponse napTienDTO(NapTienRequest req) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                TaiKhoan taiKhoan = taiKhoanDAO.findBySoTKForUpdate(conn, req.soTK());
                if (taiKhoan == null) {
                    conn.rollback();
                    return KetQuaResponse.loi("Tai khoan khong ton tai.");
                }

                String ketQua = napTien(taiKhoan, req.soTienNhap());
                boolean ok    = ketQua.startsWith("Nap tien thanh cong");

                if (ok) {
                    taiKhoanDAO.updateSoDu(conn, req.soTK(), taiKhoan.getSoDu());
                    conn.commit();
                    giaoDichDAO.insert("Nap_Tien", parseSafe(req.soTienNhap()), req.soTK(), "Thanh_Cong");
                } else {
                    conn.rollback();
                }
                return ok ? KetQuaResponse.ok(ketQua) : KetQuaResponse.loi(ketQua.replace("Loi: ", ""));
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            return KetQuaResponse.loi("Loi he thong: " + e.getMessage());
        }
    }

    /** Chuyển tiền: load cả 2 tài khoản từ DB → logic → ghi DB + lịch sử. */
    public KetQuaResponse chuyenTienDTO(ChuyenTienRequest req) {
        TaiKhoan nguon = taiKhoanDAO.findBySoTK(req.soTKNguon());
        if (nguon == null) return KetQuaResponse.loi("Tai khoan nguon khong ton tai.");

        try {
            kiemTraTaiKhoanCoTheGiaoDich(nguon);
            nguon.kiemTraPIN(req.pin());
            double soTien = chuyenDoiSoTien(req.soTienNhap(), "chuyen");
            kiemTraSoDu(nguon, soTien);

            TaiKhoan dich = taiKhoanDAO.findBySoTK(req.soTKDich());
            if (dich == null)
                throw new ChuyenKhoanThatBaiException("Loi: Tai khoan dich khong ton tai.");

            nguon.setSoDu(nguon.getSoDu() - soTien);
            dich.setSoDu(dich.getSoDu() + soTien);

            taiKhoanDAO.updateSoDu(req.soTKNguon(), nguon.getSoDu());
            taiKhoanDAO.updateSoDu(req.soTKDich(),  dich.getSoDu());
            persistPinState(nguon);
            giaoDichDAO.insert("Chuyen_Tien", soTien, req.soTKNguon(), "Thanh_Cong");

            return KetQuaResponse.ok("Chuyen tien thanh cong: " + Utils.formatCurrency(soTien) + ".");

        } catch (TaiKhoanBiKhoaException | SaiPinException |
                 SoTienKhongHopLeException | KhongDuSoDuException |
                 ChuyenKhoanThatBaiException e) {
            persistPinState(nguon);
            return KetQuaResponse.loi(e.getMessage().replace("Loi: ", ""));
        }
    }

    /** Đổi PIN: load DB → kiểm tra → ghi PIN mới vào theatm. */
    public KetQuaResponse doiPINDTO(DoiPINRequest req) {
        TaiKhoan taiKhoan = taiKhoanDAO.findBySoTK(req.soTK());
        if (taiKhoan == null) return KetQuaResponse.loi("Tai khoan khong ton tai.");

        String ketQua = doiPIN(taiKhoan, req.pinCu(), req.pinMoi(), req.xacNhanPinMoi());
        boolean ok    = ketQua.startsWith("Doi ma PIN thanh cong");

        persistPinState(taiKhoan);
        if (ok) {
            taiKhoanDAO.updateMaPin(req.soTK(), req.pinMoi());
            giaoDichDAO.insert("Doi_PIN", 0, req.soTK(), "Thanh_Cong");
        }
        return ok ? KetQuaResponse.ok("Doi ma PIN thanh cong.") : KetQuaResponse.loi(ketQua.replace("Loi: ", ""));
    }

    // ════════════════════════════════════════════════════════════════
    //  PRIVATE HELPERS
    // ════════════════════════════════════════════════════════════════

    /** Mapper Entity → DTO an toàn (không có PIN). */
    private TaiKhoanResponse chuyenSangDTO(TaiKhoan tk) {
        return new TaiKhoanResponse(tk.getSoTK(), tk.getMaKH(), tk.getSoDu(),
                tk.getTrangThai(), tk.isBiKhoa());
    }

    /** Đồng bộ số lần nhập sai PIN và trạng thái khoá về DB. */
    private void persistPinState(TaiKhoan tk) {
        if (tk == null) return;
        try {
            taiKhoanDAO.updateSoLanNhapSai(tk.getSoTK(), tk.getSoLanNhapSaiPin());
            if (tk.isBiKhoa()) taiKhoanDAO.updateTrangThai(tk.getSoTK(), "Bi_Khoa");
        } catch (Exception e) {
            System.err.println("⚠️ persistPinState: " + e.getMessage());
        }
    }

    /** Parse số tiền an toàn (trả 0 nếu không hợp lệ). */
    private double parseSafe(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return 0; }
    }

    // ════════════════════════════════════════════════════════════════
    //  LEGACY BUSINESS LOGIC  (giữ nguyên, dùng bởi DTO methods)
    // ════════════════════════════════════════════════════════════════

    public String dangNhapATM(TaiKhoan taiKhoan, String pinNhap) {
        try {
            kiemTraTaiKhoanTonTai(taiKhoan);
            taiKhoan.kiemTraPIN(pinNhap);
            return "Dang nhap ATM thanh cong.";
        } catch (SaiPinException | TaiKhoanBiKhoaException e) {
            return e.getMessage();
        }
    }

    public String rutTien(TaiKhoan taiKhoan, String pinNhap, String soTienNhap) {
        try {
            kiemTraTaiKhoanCoTheGiaoDich(taiKhoan);
            taiKhoan.kiemTraPIN(pinNhap);
            double soTien = chuyenDoiSoTien(soTienNhap, "rut");
            kiemTraSoDu(taiKhoan, soTien);
            kiemTraHanMucRutTien(taiKhoan, soTien);
            taiKhoan.setSoDu(taiKhoan.getSoDu() - soTien);
            ghiNhanRutTien(taiKhoan, soTien);
            return "Rut tien thanh cong: " + Utils.formatCurrency(soTien) + ".";
        } catch (TaiKhoanBiKhoaException | SaiPinException | SoTienKhongHopLeException
                 | KhongDuSoDuException | VuotHanMucRutTienException e) {
            return e.getMessage();
        }
    }

    public String napTien(TaiKhoan taiKhoan, String soTienNhap) {
        try {
            kiemTraTaiKhoanCoTheGiaoDich(taiKhoan);
            double soTien = chuyenDoiSoTien(soTienNhap, "nap");
            taiKhoan.setSoDu(taiKhoan.getSoDu() + soTien);
            return "Nap tien thanh cong: " + Utils.formatCurrency(soTien) + ".";
        } catch (TaiKhoanBiKhoaException | SoTienKhongHopLeException e) {
            return e.getMessage();
        }
    }

    public String doiPIN(TaiKhoan taiKhoan, String pinCu, String pinMoi, String xacNhanPinMoi) {
        try {
            kiemTraTaiKhoanCoTheGiaoDich(taiKhoan);
            taiKhoan.kiemTraPIN(pinCu);
            if (pinMoi == null || !pinMoi.matches("\\d{6}"))
                throw new SaiPinException("Loi: Ma PIN moi phai gom dung 6 chu so.");
            if (!pinMoi.equals(xacNhanPinMoi))
                throw new SaiPinException("Loi: Xac nhan ma PIN moi khong khop.");
            taiKhoan.setMaPin(pinMoi);
            return "Doi ma PIN thanh cong.";
        } catch (TaiKhoanBiKhoaException | SaiPinException e) {
            return e.getMessage();
        }
    }

    /** @deprecated Dùng layThongTinTaiKhoan(soTK) thay thế. */
    @Deprecated
    public TaiKhoan timTaiKhoan(String soTK) {
        return taiKhoanDAO.findBySoTK(soTK);
    }

    // ── Validation helpers ────────────────────────────────────────

    private void kiemTraTaiKhoanTonTai(TaiKhoan tk) throws SaiPinException {
        if (tk == null) throw new SaiPinException("Loi: Khong tim thay tai khoan.");
    }

    private void kiemTraTaiKhoanCoTheGiaoDich(TaiKhoan tk) throws TaiKhoanBiKhoaException {
        if (tk == null)
            throw new TaiKhoanBiKhoaException("Loi: Khong tim thay tai khoan de thuc hien giao dich.");
        if (tk.isBiKhoa() || !tk.isTrangThaiHoatDong())
            throw new TaiKhoanBiKhoaException("Loi: Tai khoan da bi khoa. Vui long lien he ngan hang.");
    }

    private double chuyenDoiSoTien(String soTienNhap, String loai) throws SoTienKhongHopLeException {
        try {
            double so = Double.parseDouble(soTienNhap);
            if (so <= 0) throw new SoTienKhongHopLeException("Loi: So tien " + loai + " phai lon hon 0.");
            return so;
        } catch (NumberFormatException e) {
            throw new SoTienKhongHopLeException("Loi: So tien khong hop le. Vui long chi nhap chu so.");
        }
    }

    private void kiemTraSoDu(TaiKhoan tk, double soTien) throws KhongDuSoDuException {
        if (tk.getSoDu() < soTien)
            throw new KhongDuSoDuException("Loi: So du khong du de thuc hien giao dich.");
    }

    private void kiemTraHanMucRutTien(TaiKhoan tk, double soTien) throws VuotHanMucRutTienException {
        double daRut = tongTienRutTheoNgay.getOrDefault(tk.getSoTK(), 0.0);
        if (daRut + soTien > HAN_MUC_RUT_TIEN_MOI_NGAY)
            throw new VuotHanMucRutTienException(
                    "Loi: Ban da vuot han muc rut tien toi da " + Utils.formatCurrency(HAN_MUC_RUT_TIEN_MOI_NGAY) + " trong ngay.");
    }

    private void ghiNhanRutTien(TaiKhoan tk, double soTien) {
        double daRut = tongTienRutTheoNgay.getOrDefault(tk.getSoTK(), 0.0);
        tongTienRutTheoNgay.put(tk.getSoTK(), daRut + soTien);
    }
}
