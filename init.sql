-- init.sql: Cấu trúc Database cho dự án ATM (Kiến trúc 3 lớp, JDBC thuần)
-- Chạy đoạn script này trên MySQL để tạo lại database.

DROP DATABASE IF EXISTS atmdb;
CREATE DATABASE atmdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE atmdb;

-- 1. Bảng KhachHang
CREATE TABLE KhachHang (
  MaKH VARCHAR(20) PRIMARY KEY,
  HoTen VARCHAR(100) NOT NULL,
  SoCCCD VARCHAR(30) UNIQUE,
  SoDienThoai VARCHAR(20),
  Email VARCHAR(100)
) ENGINE=InnoDB;

-- 2. Bảng TaiKhoan (Gộp cả ThanhToan và TietKiem sử dụng Single-Table Design)
CREATE TABLE TaiKhoan (
  SoTK VARCHAR(30) PRIMARY KEY,
  LoaiTaiKhoan VARCHAR(30) NOT NULL, -- Phân loại: 'Thanh_Toan' hoặc 'Tiet_Kiem'
  SoDu DECIMAL(18,2) NOT NULL DEFAULT 0,
  TrangThai VARCHAR(20) NOT NULL DEFAULT 'Hoat_Dong',
  MaKH VARCHAR(20),
  NgayMo DATETIME DEFAULT CURRENT_TIMESTAMP,
  
  -- Các cột riêng của TaiKhoanThanhToan (Sẽ là NULL nếu là tài khoản tiết kiệm)
  PhiBaoHanh DECIMAL(18,2) DEFAULT NULL,
  SoLanRutTrongThang INT DEFAULT NULL,
  HanSoLanRutToiDa INT DEFAULT NULL,
  
  -- Các cột riêng của TaiKhoanTietKiem (Sẽ là NULL nếu là tài khoản thanh toán)
  LaiSuat DECIMAL(5,4) DEFAULT NULL,
  HanRutToiThieu INT DEFAULT NULL,
  NgayRutCuoi DATETIME DEFAULT NULL,
  
  FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 3. Bảng TheATM (Liên kết 1-n với TaiKhoan)
CREATE TABLE TheATM (
  SoThe VARCHAR(32) PRIMARY KEY,
  MaPIN VARCHAR(20) NOT NULL,
  TrangThai VARCHAR(20) NOT NULL DEFAULT 'Hoat_Dong',
  SoTK_LienKet VARCHAR(30),
  SoLanNhapSai INT DEFAULT 0,
  HanNhapSaiToiDa INT DEFAULT 3,
  NgayCapThe DATETIME DEFAULT CURRENT_TIMESTAMP,
  NgayHetHan DATETIME DEFAULT (CURRENT_TIMESTAMP + INTERVAL 5 YEAR),
  FOREIGN KEY (SoTK_LienKet) REFERENCES TaiKhoan(SoTK) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 4. Bảng GiaoDich (Lưu lịch sử giao dịch của Tài Khoản)
CREATE TABLE GiaoDich (
  MaGD VARCHAR(50) PRIMARY KEY,
  LoaiGiaoDich VARCHAR(30),
  SoTien DECIMAL(18,2),
  ThoiGian DATETIME DEFAULT CURRENT_TIMESTAMP,
  TrangThai VARCHAR(20),
  SoTK_LienKet VARCHAR(30),
  FOREIGN KEY (SoTK_LienKet) REFERENCES TaiKhoan(SoTK) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ========================================================
-- THÊM DỮ LIỆU MẪU (DUMMY DATA)
-- ========================================================

-- Mẫu Khách Hàng
INSERT INTO KhachHang (MaKH, HoTen, SoCCCD, SoDienThoai, Email)
VALUES ('KH001','Nguyen Van A','012345678912','0912345678','nguyenvana@email.com');

-- Mẫu Tài Khoản (1 Thanh toán, 1 Tiết kiệm)
INSERT INTO TaiKhoan (SoTK, LoaiTaiKhoan, SoDu, TrangThai, MaKH, PhiBaoHanh, SoLanRutTrongThang, HanSoLanRutToiDa)
VALUES ('TK_TT001', 'Thanh_Toan', 10000000.00, 'Hoat_Dong', 'KH001', 5000.00, 0, 10);

INSERT INTO TaiKhoan (SoTK, LoaiTaiKhoan, SoDu, TrangThai, MaKH, LaiSuat, HanRutToiThieu, NgayRutCuoi)
VALUES ('TK_TK001', 'Tiet_Kiem', 50000000.00, 'Hoat_Dong', 'KH001', 0.0500, 30, CURRENT_TIMESTAMP);

-- Mẫu Thẻ ATM (Liên kết với tài khoản thanh toán)
INSERT INTO TheATM (SoThe, MaPIN, TrangThai, SoTK_LienKet, SoLanNhapSai, HanNhapSaiToiDa)
VALUES ('1234567890123456', '1234', 'Hoat_Dong', 'TK_TT001', 0, 3);

-- Mẫu Giao Dịch
INSERT INTO GiaoDich (MaGD, LoaiGiaoDich, SoTien, TrangThai, SoTK_LienKet)
VALUES (CONCAT('GD', REPLACE(UNIX_TIMESTAMP(CURRENT_TIMESTAMP), '.','')), 'Nap_Tien', 2000000.00, 'Thanh_Cong', 'TK_TT001');
