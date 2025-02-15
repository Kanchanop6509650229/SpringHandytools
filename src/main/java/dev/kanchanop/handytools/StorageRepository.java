/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package dev.kanchanop.handytools;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * อินเตอร์เฟซสำหรับจัดการข้อมูลอุปกรณ์ในฐานข้อมูล
 * ใช้ JPA Repository เพื่อจัดการการเข้าถึงข้อมูล
 */
public interface StorageRepository extends JpaRepository<Storage, Long> {
    /**
     * ค้นหาอุปกรณ์ตามสถานะการยืม
     * @param borrowed สถานะการยืม (true = ถูกยืมแล้ว, false = ยังไม่ถูกยืม)
     * @return รายการอุปกรณ์ที่มีสถานะตรงตามเงื่อนไข
     */
    List<Storage> findByBorrowed(boolean borrowed);

    /**
     * ค้นหาอุปกรณ์ตามชื่อผู้ยืม
     * @param borrowerName ชื่อผู้ยืม
     * @return รายการอุปกรณ์ที่ถูกยืมโดยผู้ยืมที่ระบุ
     */
    List<Storage> findByBorrowerName(String borrowerName);

    /**
     * ค้นหาอุปกรณ์ตามชื่อเจ้าของ
     * @param ownerName ชื่อเจ้าของ
     * @return รายการอุปกรณ์ที่เป็นของเจ้าของที่ระบุ
     */
    List<Storage> findByOwnerName(String ownerName);
}
