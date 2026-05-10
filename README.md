
# 🏦 Hệ Thống Mô Phỏng Máy Rút Tiền Tự Động (ATM)

## 📋 Giai Đoạn 1: Xây Dựng Nền Tảng OOP (Tuần 1-2)

### 🎯 Mục Tiêu
Xây dựng các Entity classes (Thực thể) theo hướng đối tượng với các tính chất:
- **Kế thừa (Inheritance)**: Các class con kế thừa từ class cha
- **Interface**: Định nghĩa hành động chung
- **Encapsulation**: Tính đóng gói dữ liệu
- **Tính trừu tượng**: Các Interface định nghĩa khái niệm trừu tượng

---

## 📂 Cấu Trúc Project

```
project/
├── pom.xml                                    # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/atm/
│   │   │   └── entity/                       # Package chứa các Entity classes
│   │   │       ├── ITransaction.java         # Interface: định nghĩa hành động giao dịch
│   │   │       ├── IAccount.java             # Interface: định nghĩa quản lý tài khoản
│   │   │       ├── KhachHang.java            # Khách hàng
│   │   │       ├── TaiKhoan.java             # Tài khoản cơ bản (implements IAccount, ITransaction)
│   │   │       ├── TaiKhoanTietKiem.java     # Tài khoản tiết kiệm (extends TaiKhoan)
│   │   │       ├── TaiKhoanDangDo.java       # Tài khoản đang dùng (extends TaiKhoan)
│   │   │       ├── TheATM.java               # Thẻ ATM
│   │   │       ├── GiaoDich.java             # Giao dịch (Transaction Record)
│   │   │       └── ATMTest.java              # Class kiểm tra OOP
│   │   └── resources/
│   └── test/
│       └── java/com/atm/
└── README.md                                  # This file
```

---

## 🏗️ Các Entity Classes

### 1️⃣ **ITransaction (Interface)**
Định nghĩa các hành động giao dịch chung:
- `rutTien(double soTien)` - Rút tiền
- `napTien(double soTien)` - Nạp tiền
- `kiemTraSoDu()` - Kiểm tra số dư
- `hienThiLichSu()` - Xem lịch sử giao dịch

### 2️⃣ **IAccount (Interface)**
Định nghĩa quản lý trạng thái tài khoản:
- `khoa()` - Khóa tài khoản
- `moKhoa()` - Mở khóa tài khoản
- `isTrangThaiHoatDong()` - Kiểm tra trạng thái

### 3️⃣ **KhachHang (Customer)**
```
Biến:
- maKH, hoTen, soCCCD, soDienThoai, email
```

### 4️⃣ **TaiKhoan (Base Class - Implements IAccount, ITransaction)**
```
Biến:
- soTK, soDu, trangThai, maKH, ngayMo
- lichSuGiaoDich (danh sách giao dịch)

Phương thức:
- rutTien(double soTien)          // Rút tiền
- napTien(double soTien)          // Nạp tiền
- kiemTraSoDu()                   // Kiểm tra số dư
- khoa() / moKhoa()               // Quản lý trạng thái
```

### 5️⃣ **TaiKhoanTietKiem (Extends TaiKhoan)**
Đặc điểm: Tài khoản tiết kiệm có lãi suất, hạn rút tối thiểu
```
Biến thêm:
- laiSuat (%)
- hanRutToiThieu (ngày)
- ngayRutCuoi (lần rút tiền gần nhất)

Phương thức:
- tinhTienLai(int soNgayGui)   // Tính tiền lãi
- congTienLai(double tienLai)  // Cộng tiền lãi
- rutTien() override            // Kiểm tra hạn rút tối thiểu
```

### 6️⃣ **TaiKhoanDangDo (Extends TaiKhoan)**
Đặc điểm: Tài khoản đang dùng, rút tiền tự do nhưng có phí bảo hành
```
Biến thêm:
- phiBaoHanh (VND/tháng)
- soLanRutTrongThang
- hanSoLanRutToiDa

Phương thức:
- truPhiBaoHanh()               // Trừ phí hàng tháng
- resetSoLanRutThang()          // Reset lại số lần rút
- rutTien() override            // Kiểm tra số lần rút tối đa
```

### 7️⃣ **TheATM (ATM Card)**
```
Biến:
- soThe (16 chữ số)
- maPIN (4 chữ số)
- trangThai (Hoat_Dong, Khoa, Het_Han)
- soTK_LienKet
- soLanNhapSai (đếm PIN sai)
- hanNhapSaiToiDa (mặc định 3)

Phương thức:
- xacThucPIN(String pinNhap)    // Kiểm tra PIN
- moKhoaThe()                   // Mở khóa thẻ
- isTheHoatDong()               // Kiểm tra trạng thái
```

### 8️⃣ **GiaoDich (Transaction Record)**
```
Biến:
- maGD (mã giao dịch)
- loaiGiaoDich (Rut_Tien, Nap_Tien, etc.)
- soTien
- thoiGian
- trangThai (Thanh_Cong, That_Bai)
- soTK_LienKet

Phương thức:
- taoMaGiaoDich()               // Tạo mã duy nhất
```
## 🛡️ Giai Đoạn 2: Hàng Rào Bảo Vệ & Xử Lý Ngoại Lệ (Tuần 3-4)

### 🎯 Mục Tiêu

Tăng tính an toàn cho hệ thống ATM bằng cách xử lý các lỗi phổ biến trong giao dịch:

* Nhập sai mã PIN
* Nhập sai định dạng dữ liệu
* Rút tiền vượt quá số dư
* Rút tiền vượt hạn mức
* Tài khoản hoặc thẻ bị khóa
* Lỗi kết nối cơ sở dữ liệu

Mục tiêu là đảm bảo hệ thống không bị crash khi người dùng thao tác sai.

---

### ⚠️ Custom Exception

#### Package

```text id="0j6d8y"
src/main/java/com/atm/exception/
```

#### Bao gồm các exception chính

```text id="ux4s3g"
InvalidAmountException       — Số tiền không hợp lệ
InsufficientBalanceException — Không đủ số dư
InvalidPinException          — Sai mã PIN
AccountLockedException       — Tài khoản bị khóa
WithdrawalLimitException     — Vượt hạn mức rút tiền
TransferFailedException      — Chuyển khoản thất bại
```

---

### 🔐 Bảo Vệ Tài Khoản & Thẻ ATM

#### TaiKhoan.java

##### Bổ sung chức năng

* Khóa tài khoản
* Mở khóa tài khoản
* Kiểm tra trạng thái hoạt động

---

#### TheATM.java

##### Bổ sung chức năng

* Đếm số lần nhập sai mã PIN
* Tự động khóa thẻ sau 3 lần nhập sai
* Mở khóa thẻ
* Kiểm tra trạng thái thẻ


---

### 💳 Kiểm Tra Giao Dịch

#### Các giao dịch quan trọng đều được kiểm tra bằng `try-catch`

* Rút tiền (`rutTien`)
* Nạp tiền (`napTien`)
* Chuyển tiền (`chuyenTien`)
* Đổi mã PIN (`doiPIN`)
* Đăng nhập ATM (`dangNhapATM`)

---

#### Thứ tự kiểm tra

```text id="q6v56i"
1. Kiểm tra trạng thái thẻ
2. Kiểm tra mã PIN
3. Kiểm tra trạng thái tài khoản
4. Kiểm tra số tiền hợp lệ
5. Kiểm tra số dư khả dụng
6. Kiểm tra hạn mức giao dịch
7. Thực hiện giao dịch
```

---

### 🧪 Kiểm Thử

#### Các test case chính

* Sai PIN 3 lần → khóa thẻ
* Rút tiền âm hoặc bằng 0
* Rút vượt số dư
* Rút vượt hạn mức
* Chuyển khoản lỗi
* Database connection fail

#### Kết quả mong đợi

Hệ thống hiển thị thông báo lỗi rõ ràng thay vì dừng chương trình đột ngột.

---

## 🧪 Chạy Test Giai Đoạn 1

### Cách 1: Sử dụng Maven
```bash
# Compile project
mvn clean compile

# Run test class
mvn -q org.codehaus.mojo:exec-maven-plugin:3.1.0:java "-Dexec.mainClass=com.atm.entity.ATMTest"
```

### Cách 2: Chạy trực tiếp trong IDE
1. Mở file `ATMTest.java`
2. Click chuột phải → Run → Run 'ATMTest.main()'

## 🧪 Chạy Test Giai Đoạn 2

### Cách 1: Sử dụng Maven
```bash
# Compile project
mvn clean compile

# Chạy toàn bộ test
mvn test

# Chạy demo console để kiểm tra case đúng và case lỗi
java -cp target/classes com.atm.DemoConsole
```

### Cách 2: Chạy trực tiếp trong IDE
1. Mở file DemoConsole.java
2. Click chuột phải → Run → Run 'DemoConsole.main()'

### 📊 Output Mong Đợi
- ✅ Kiểm tra kế thừa - TaiKhoanTietKiem, TaiKhoanDangDo hoạt động đúng
- ✅ Kiểm tra Interface - Các phương thức được implement đúng
- ✅ Kiểm tra xác thực PIN - Thẻ khóa sau 3 lần nhập sai
- ✅ Kiểm tra giao dịch - Lịch sử giao dịch được ghi lại

---

## 📚 Tính Chất OOP Áp Dụng

| Tính Chất | Ví Dụ | Lợi Ích |
|-----------|-------|---------|
| **Kế Thừa** | TaiKhoanTietKiem extends TaiKhoan | Tái sử dụng code, mở rộng chức năng |
| **Interface** | ITransaction, IAccount | Quy định hành động chung, linh hoạt |
| **Encapsulation** | Private biến, public getter/setter | Bảo vệ dữ liệu, kiểm soát truy cập |
| **Tính Trừu Tượng** | Interface định nghĩa hành động | Ẩn chi tiết triển khai |

---


## 📝 Ghi Chú

- **Tác giả**: 
Nguyễn Văn Sơn (Giai đoạn 1)
, Vũ Mạnh Cường (Giai đoạn 2)
- **Công nghệ**: Java 11+, Maven
- **Database**: MySQL (Giai đoạn 2)
- **UI**: Java Swing (Giai đoạn 4)

---

## 📞 Liên Hệ & Hỗ Trợ

Nếu có câu hỏi về cấu trúc OOP, vui lòng tham khảo:
- Chương 1-4: Lập trình Hướng Đối Tượng
- Java Documentation: https://docs.oracle.com/en/java/

---

**Phiên bản**: 1.0 (Giai đoạn 1)  
**Cập nhật lần cuối**: 02/05/2026