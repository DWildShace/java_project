package com.atm.entity;

/**
 * Interface định nghĩa các hành động chung cho giao dịch
 * Áp dụng Interface Segregation Principle (ISP)
 */
public interface ITransaction {
    /**
     * Thực hiện rút tiền từ tài khoản
     * @param soTien số tiền cần rút
     * @return true nếu rút tiền thành công, false nếu thất bại
     */
    boolean rutTien(double soTien);

    /**
     * Thực hiện nạp tiền vào tài khoản
     * @param soTien số tiền cần nạp
     * @return true nếu nạp tiền thành công, false nếu thất bại
     */
    boolean napTien(double soTien);

    /**
     * Kiểm tra số dư tài khoản
     * @return số dư hiện tại
     */
    double kiemTraSoDu();

    /**
     * Lấy lịch sử giao dịch
     * @return danh sách các giao dịch
     */
    String hienThiLichSu();
}
