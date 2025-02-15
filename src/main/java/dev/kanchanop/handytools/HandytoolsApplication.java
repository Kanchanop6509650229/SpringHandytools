package dev.kanchanop.handytools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * คลาสหลักของแอปพลิเคชัน Spring Boot
 * ใช้สำหรับเริ่มต้นการทำงานของแอปพลิเคชัน
 */
@SpringBootApplication
public class HandytoolsApplication {
    /**
     * เมธอดหลักสำหรับเริ่มต้นการทำงานของแอปพลิเคชัน
     * @param args พารามิเตอร์ที่รับจากคอมมานด์ไลน์
     */
    public static void main(String[] args) {
        SpringApplication.run(HandytoolsApplication.class, args);
    }
}
