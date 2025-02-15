package dev.kanchanop.handytools;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ข้อผิดพลาดที่เกิดขึ้นเมื่อไม่พบอุปกรณ์ในระบบ
 * ส่งกลับ HTTP 404 Not Found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFoundException extends RuntimeException {
    /**
     * สร้างข้อความแจ้งเตือนเมื่อไม่พบอุปกรณ์
     * @param id รหัสอุปกรณ์ที่ไม่พบในระบบ
     */
    StorageNotFoundException(Long id) {
        super("Could not find storage " + id);
    }
}