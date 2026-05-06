chưa hợp lý với entity ở đây có vẻ đang sử dụng đối với file text , cần sửa lại đề kết nối với database

👤 KHÁCH HÀNG (KhachHang)
  │   - Tên, CCCD, Email...
  │
  └─── Sở hữu ───┐
                 ▼
          🏦 TÀI KHOẢN NGÂN HÀNG (TaiKhoan) ◄════ (Tuân thủ luật: IAccount, ITransaction)
          │      - Số dư, Trạng thái, Ngày mở
          │
          ├───────── Sinh ra & Lưu trữ ────────┐
          │                                    ▼
          │                          🧾 LỊCH SỬ GIAO DỊCH (GiaoDich)
          │                              - Sinh ra khi Tài Khoản thực hiện Rút/Nạp
          │                              - Nằm trong: List<GiaoDich> lichSuGiaoDich
          │
          ├───────── Phân loại ─────────┐
          │                             │
          ▼                             ▼
 💸 TÀI KHOẢN THANH TOÁN            💰 TÀI KHOẢN TIẾT KIỆM
    (TaiKhoanThanhToan)                (TaiKhoanTietKiem)
  - Trừ phí bảo hành hàng tháng      - Tính tiền lãi theo năm
  - Giới hạn số lần rút/tháng        - Có hạn rút tiền tối thiểu
          ▲
          ║ 
          ║ (Chỉ liên kết qua soTK_LienKet)
          ║
  💳 THẺ ATM (TheATM)
      - Số thẻ, Mã PIN, Trạng thái khóa/mở
      - Đóng vai trò là công cụ xác thực quyền truy cập vào Tài Khoản.



tạm thời là như thế này