/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dev.kanchanop.handytools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * คลาสสำหรับโหลดข้อมูลเริ่มต้นลงในฐานข้อมูล
 * ใช้สำหรับสร้างข้อมูลตัวอย่างเมื่อเริ่มต้นแอปพลิเคชัน
 */
@Configuration
public class LoadDatabase {
    
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    /**
     * สร้างข้อมูลตัวอย่างในฐานข้อมูล
     * เพิ่มอุปกรณ์ตัวอย่างจำนวน 3 รายการ
     * @param storageRepository repository สำหรับจัดการข้อมูลอุปกรณ์
     */
    @Bean
    public CommandLineRunner setupData(StorageRepository storageRepository) {
        return (args) -> {
            log.info("Preloading " + storageRepository.save(new Storage("Hammer", "John", "Garage", false, "")));
            log.info("Preloading " + storageRepository.save(new Storage("Screwdriver", "Jane", "Shed", true, "John")));
            log.info("Preloading " + storageRepository.save(new Storage("Wrench", "Jill", "Basement", false, "")));
        };
    }
}
