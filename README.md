# 🏦 ATM System - Concurrency & OOP Project

Hệ thống mô phỏng ATM được xây dựng bằng Java Swing, tập trung vào tính đúng đắn của dữ liệu trong môi trường đa luồng (Multi-threading) và các nguyên lý thiết kế OOP.

## 🚀 Quick Start (Cho thành viên nhóm)

### 1. Cấu hình Database
- File cấu hình: `src/main/resources/database.properties`
- Đảm bảo đã import database `atmdb`.

### 2. Chạy ứng dụng chính (UI)
```bash
mvn compile exec:java -Dexec.mainClass="com.atm.Main"
```

### 3. Chạy Test Đa luồng (Kiểm tra Race Condition)
Đây là phần quan trọng nhất để chứng minh tính an toàn của giao dịch:
```bash
mvn compile exec:java -Dexec.mainClass="com.atm.ConcurrencyTest"
```

---

## 🛠 Tech Stack
- **Language:** Java 21 (Hỗ trợ 11+)
- **Database:** MySQL 8.0
- **Build Tool:** Maven
- **UI Framework:** Java Swing
- **Core Logic:** JDBC Transaction, Row-level Locking (`SELECT FOR UPDATE`)

---

## 🏗 Kiến trúc & Luồng xử lý Đa luồng

Dự án áp dụng mô hình Layered Architecture:
`UI (Panel) -> Service (ATMService) -> DAO (TaiKhoanDAO/TheATMDAO) -> MySQL`

### Cơ chế bảo vệ số dư (Concurrency Control)
Để tránh lỗi "mất tiền" khi nhiều người cùng rút tiền trên một tài khoản tại các cây ATM khác nhau:
1. **Transaction:** Sử dụng `connection.setAutoCommit(false)`.
2. **Locking:** Sử dụng `SELECT ... FOR UPDATE` trong `TaiKhoanDAO.findBySoTKForUpdate()`.
   - Luồng 1 đang rút tiền sẽ giữ khóa dòng đó.
   - Luồng 2 phải đợi Luồng 1 `COMMIT` hoặc `ROLLBACK` mới được đọc số dư mới.
3. **Atomic:** Đảm bảo quá trình "Đọc số dư -> Kiểm tra -> Trừ tiền -> Ghi lịch sử" là một khối nguyên tử.

---

## 📂 Cấu trúc thư mục chính
- `com.atm.entity`: Các lớp đối tượng (TaiKhoan, TheATM, KhachHang...).
- `com.atm.DAO`: Xử lý truy vấn SQL và quản lý Connection.
- `com.atm.service`: Chứa Logic nghiệp vụ và quản lý Transaction.
- `com.atm.ui`: Các thành phần giao diện Swing.
- `com.atm.exception`: Các ngoại lệ tự định nghĩa (InsufficientBalance, AccountLocked...).

---

## 📝 Lưu ý khi phát triển
- **UI Lifecycle:** Khi chuyển Panel, hãy gọi `setVisible(true)` để trigger việc refresh dữ liệu từ DB.
- **Transaction:** Luôn đóng connection hoặc trả về pool sau khi sử dụng để tránh leak.
- **Card-First Auth:** Luồng đăng nhập hiện tại là: Nhập Số Thẻ -> Kiểm tra PIN -> Truy xuất Tài khoản liên kết.

---
**Phiên bản:** 2.0 (Cập nhật xử lý đa luồng)
**Cập nhật cuối:** 12/05/2026