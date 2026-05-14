# 🏦 SƠ ĐỒ THỰC THỂ DỮ LIỆU (DATABASE ENTITY)

Dự án sử dụng cơ sở dữ liệu quan hệ (MySQL) thông qua mô hình DAO. Dưới đây là sơ đồ liên kết giữa các bảng/thực thể:

👤 KHÁCH HÀNG (KhachHang)
  │   - maKH (Primary Key)
  │   - tenKH, cccd, email, sdt...
  │
  └─── 1-N (Một KH có thể sở hữu nhiều Tài Khoản) ───┐
                                                     ▼
                                          🏦 TÀI KHOẢN NGÂN HÀNG (TaiKhoan)
                                          │      - soTK (Primary Key)
                                          │      - maKH (Foreign Key -> KhachHang)
                                          │      - loaiTaiKhoan (THANH_TOAN / TIET_KIEM)
                                          │      - soDu, trangThai, ngayMo
                                          │
                                          ├───────── 1-N (Một TK có nhiều Giao Dịch) ────────┐
                                          │                                                  ▼
                                          │                                       🧾 LỊCH SỬ GIAO DỊCH (GiaoDich)
                                          │                                           - maGD (Primary Key)
                                          │                                           - soTK (Foreign Key -> TaiKhoan)
                                          │                                           - loaiGiaoDich (RUT_TIEN, NAP_TIEN, CHUYEN_TIEN)
                                          │                                           - soTien, ngayGio, noiDung
                                          │
                                          ├───────── Phân loại OOP (Logic Layer) ─────────┐
                                          │                                               │
                                          ▼                                               ▼
                                 💸 TÀI KHOẢN THANH TOÁN                        💰 TÀI KHOẢN TIẾT KIỆM
                                    (TaiKhoanThanhToan)                            (TaiKhoanTietKiem)
                                  - Xử lý các giao dịch thông thường             - Xử lý tính lãi suất
                                  - Có thể có phí duy trì                        - Có điều kiện kỳ hạn / số dư tối thiểu
                                          ▲
                                          ║ 
                                          ║ (1-1: Một thẻ liên kết 1 Tài Khoản)
                                          ║ (soTK_LienKet là Foreign Key -> TaiKhoan)
                                          ║
                                  💳 THẺ ATM (TheATM)
                                      - soThe (Primary Key)
                                      - soTK_LienKet (Foreign Key -> TaiKhoan)
                                      - maPIN, trangThai, soLanNhapSai
                                      - Đóng vai trò là công cụ xác thực quyền truy cập qua ATM.