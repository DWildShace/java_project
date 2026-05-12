package com.atm.dto.response;

/**
 * KetQuaResponse - DTO kết quả chung cho mọi thao tác giao dịch.
 *
 * Service trả về object này thay vì String thô.
 * UI dựa vào `thanhCong` để quyết định hiển thị màu xanh hay đỏ,
 * và dùng `thongBao` để hiển thị nội dung chi tiết.
 */
public record KetQuaResponse(boolean thanhCong, String thongBao) {

    /** Factory method: tạo response thành công */
    public static KetQuaResponse ok(String thongBao) {
        return new KetQuaResponse(true, thongBao);
    }

    /** Factory method: tạo response lỗi */
    public static KetQuaResponse loi(String thongBao) {
        return new KetQuaResponse(false, thongBao);
    }
}
