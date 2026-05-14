# 📝 Phân Công Công Việc - Thành Viên: Nguyễn Văn Sơn

Dựa trên lịch sử phát triển của dự án, dưới đây là chi tiết các hạng mục công việc và đóng góp của thành viên **Nguyễn Văn Sơn** trong quá trình phát triển Hệ thống ATM:

## 1. Khởi Tạo Dự Án (Project Setup)
- Thực hiện thiết lập nền móng dự án ban đầu (Initial commit).
- Cấu hình các thư mục cốt lõi và kiến trúc thư mục chuẩn cho dự án Java.

## 2. Thiết Kế Cơ Sở Dữ Liệu (Database Design)
- Trực tiếp tham gia sửa đổi và chuẩn hóa lại cấu trúc của cơ sở dữ liệu (Database schema) để phục vụ cho mô hình giao dịch chuẩn.
- Đảm bảo tính liên kết hợp lý giữa các bảng (Khách hàng, Tài khoản, Thẻ ATM, Giao dịch) để áp dụng vào kiến trúc DAO với JDBC.

## 3. Lập Trình Giao Diện và Cấu Trúc Mã Nguồn (UI & Refactoring)
- Hoàn thiện giao diện người dùng bằng Java Swing.
- **Quan trọng:** Áp dụng việc refactor lại luồng mã nguồn thông qua việc bổ sung thêm các lớp **DTOs (Data Transfer Objects)**. Việc này giúp decouple (tách rời) phần giao diện và dữ liệu lõi của ứng dụng, tuân thủ đúng nguyên lý kiến trúc Layered/MVC.

## 4. Tài Liệu Hóa (Documentation)
- Viết và liên tục cập nhật file tài liệu `README.md` của dự án để hướng dẫn các thành viên khác cách setup môi trường, kết nối Database, và chạy test chức năng xử lý đa luồng (Concurrency).

## 5. Dọn Dẹp và Tối Ưu Hóa (Code Cleanup)
- Dọn dẹp mã nguồn, xóa bỏ các file rác, những thư viện hoặc tham chiếu (references) không còn sử dụng tới để mã nguồn trở nên sạch sẽ và tối ưu hơn.
- Xử lý các luồng conflict và gộp nhánh (Merge branches) trong quá trình làm việc nhóm trên GitHub.

