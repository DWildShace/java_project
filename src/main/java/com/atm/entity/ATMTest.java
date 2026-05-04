package com.atm.entity;

/**
 * Lớp ATMTest - Kiểm tra cấu trúc OOP của các Entity classes
 * Giai đoạn 1: Xây dựng nền tảng OOP (Tuần 1-2)
 */
public class ATMTest {
    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("  TEST HỆ THỐNG ATM - GIAI ĐOẠN 1");
        System.out.println("====================================\n");

        // ========== TEST 1: Tạo đối tượng KhachHang ==========
        System.out.println("1️⃣ TEST KHÁCH HÀNG:");
        System.out.println("-".repeat(50));
        KhachHang kh1 = new KhachHang("KH001", "Nguyễn Văn A", "123456789", "0912345678", "nguyenvana@email.com");
        System.out.println(kh1);

        // ========== TEST 2: Tạo TaiKhoan (Base Class) ==========
        System.out.println("\n2️⃣ TEST TÀI KHOẢN CƠ BẢN (TaiKhoan):");
        System.out.println("-".repeat(50));
        TaiKhoan tk1 = new TaiKhoan("TK001", 10000000, "KH001");
        System.out.println(tk1);
        System.out.println("• Kiểm tra số dư:");
        tk1.kiemTraSoDu();

        System.out.println("\n• Nạp tiền 2,000,000 VND:");
        tk1.napTien(2000000);

        System.out.println("\n• Rút tiền 1,500,000 VND:");
        tk1.rutTien(1500000);

        System.out.println("\n• Lịch sử giao dịch:");
        System.out.println(tk1.hienThiLichSu());

        // ========== TEST 3: Tạo TaiKhoanTietKiem (Kế Thừa) ==========
        System.out.println("\n3️⃣ TEST TÀI KHOẢN TIẾT KIỆM (TaiKhoanTietKiem - Kế Thừa):");
        System.out.println("-".repeat(50));
        TaiKhoanTietKiem tkTK = new TaiKhoanTietKiem("TK002", 50000000, "KH001", 0.05, 30);
        System.out.println(tkTK);
        System.out.println("• Kiểm tra số dư:");
        tkTK.kiemTraSoDu();

        System.out.println("\n• Tính tiền lãi cho 365 ngày:");
        double tienLai = tkTK.tinhTienLai(365);

        System.out.println("\n• Cộng tiền lãi:");
        tkTK.congTienLai(tienLai);

        System.out.println("\n• Thử rút tiền (nên thất bại do chưa đủ 30 ngày):");
        tkTK.rutTien(5000000);

        // ========== TEST 4: Tạo TaiKhoanDangDo (Kế Thừa) ==========
        System.out.println("\n4️⃣ TEST TÀI KHOẢN ĐANG DÙNG (TaiKhoanDangDo - Kế Thừa):");
        System.out.println("-".repeat(50));
        TaiKhoanDangDo tkDD = new TaiKhoanDangDo("TK003", 5000000, "KH001", 5000, 10);
        System.out.println(tkDD);
        System.out.println("• Kiểm tra số dư:");
        tkDD.kiemTraSoDu();

        System.out.println("\n• Trừ phí bảo hành:");
        tkDD.truPhiBaoHanh();

        System.out.println("\n• Rút tiền lần 1 (1,000,000 VND):");
        tkDD.rutTien(1000000);

        System.out.println("\n• Rút tiền lần 2 (500,000 VND):");
        tkDD.rutTien(500000);

        // ========== TEST 5: Tạo TheATM ==========
        System.out.println("\n5️⃣ TEST THẺ ATM (TheATM):");
        System.out.println("-".repeat(50));
        TheATM the1 = new TheATM("1234567890123456", "1234", "TK001");
        System.out.println(the1);

        System.out.println("\n• Xác thực PIN lần 1 (sai):");
        the1.xacThucPIN("9999");

        System.out.println("\n• Xác thực PIN lần 2 (sai):");
        the1.xacThucPIN("5555");

        System.out.println("\n• Xác thực PIN lần 3 (sai):");
        the1.xacThucPIN("6666");

        System.out.println("\n• Xác thực PIN lần 4 (thẻ đã bị khóa):");
        the1.xacThucPIN("1234");

        System.out.println("\n• Mở khóa thẻ:");
        the1.moKhoaThe();

        System.out.println("\n• Xác thực PIN sau khi mở khóa (đúng):");
        the1.xacThucPIN("1234");

        // ========== TEST 6: Quản lý trạng thái Account ==========
        System.out.println("\n6️⃣ TEST QUẢN LÝ TRẠNG THÁI TÀI KHOẢN (IAccount):");
        System.out.println("-".repeat(50));
        System.out.println("• Trạng thái hiện tại: " + (tk1.isTrangThaiHoatDong() ? "✅ Hoạt động" : "❌ Bị khóa"));

        System.out.println("\n• Khóa tài khoản:");
        tk1.khoa();

        System.out.println("• Thử rút tiền khi bị khóa:");
        tk1.rutTien(1000000);

        System.out.println("\n• Mở khóa tài khoản:");
        tk1.moKhoa();

        System.out.println("• Thử rút tiền sau khi mở khóa:");
        tk1.rutTien(1000000);

        // ========== TEST 7: GiaoDich ==========
        System.out.println("\n7️⃣ TEST GIAO DỊCH (GiaoDich):");
        System.out.println("-".repeat(50));
        String maGD = GiaoDich.taoMaGiaoDich();
        System.out.println("• Mã giao dịch được tạo: " + maGD);

        // ========== SUMMARY ==========
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  TÓNG KẾT GIAI ĐOẠN 1 - OOP CƠ BẢN");
        System.out.println("=".repeat(50));
        System.out.println("✅ Interface ITransaction - Định nghĩa hành động giao dịch");
        System.out.println("✅ Interface IAccount - Định nghĩa quản lý tài khoản");
        System.out.println("✅ Class KhachHang - Thông tin khách hàng");
        System.out.println("✅ Class TaiKhoan (Base) - Tài khoản cơ bản");
        System.out.println("✅ Class TaiKhoanTietKiem (Kế thừa) - Tài khoản tiết kiệm");
        System.out.println("✅ Class TaiKhoanDangDo (Kế thừa) - Tài khoản đang dùng");
        System.out.println("✅ Class TheATM - Quản lý thẻ ATM");
        System.out.println("✅ Class GiaoDich - Ghi lại lịch sử giao dịch");
        System.out.println("=" + "=".repeat(49));
    }
}
