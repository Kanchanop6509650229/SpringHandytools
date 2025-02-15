/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dev.kanchanop.handytools;
/**
 *
 * @author dlwlrma
 */

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * คลาสสำหรับเก็บข้อมูลอุปกรณ์
 * เป็น Entity class ที่ใช้เก็บข้อมูลในฐานข้อมูล
 */
@Entity
public class Storage {
    /**
     * รหัสอุปกรณ์ (สร้างอัตโนมัติ)
     */
    private @Id @GeneratedValue Long id;

    /**
     * รายละเอียดของอุปกรณ์
     */
    private String toolDetail;

    /**
     * ชื่อเจ้าของอุปกรณ์
     */
    private String ownerName;

    /**
     * สถานที่เก็บอุปกรณ์
     */
    private String locationName;

    /**
     * สถานะการยืม (true = ถูกยืมแล้ว, false = ยังไม่ถูกยืม)
     */
    private boolean borrowed;

    /**
     * ชื่อผู้ยืมอุปกรณ์
     */
    private String borrowerName;

    /**
     * คอนสตรักเตอร์เปล่าสำหรับ JPA
     */
    Storage() {} // Default constructor for JPA

    /**
     * คอนสตรักเตอร์สำหรับสร้างอุปกรณ์ใหม่
     */
    public Storage(String toolDetail, String ownerName, String locationName, boolean borrowed,
            String borrowerName) {
        this.toolDetail = toolDetail;
        this.ownerName = ownerName;
        this.locationName = locationName;
        this.borrowed = borrowed;
        this.borrowerName = borrowerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToolDetail() {
        return toolDetail;
    }
    public void setToolDetail(String toolDetail) {
        this.toolDetail = toolDetail;
    }

    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getLocationName() {
        return locationName;
    }
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public boolean isBorrowed() {
        return borrowed;
    }
    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public String getBorrowerName() {
        return borrowerName;
    }
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    /**
     * ตรวจสอบว่าอุปกรณ์สามารถยืมได้หรือไม่
     * @return true ถ้าสามารถยืมได้, false ถ้าถูกยืมไปแล้ว
     */
    public boolean canBeBorrowed() {
        return !this.borrowed;
    }
    
    /**
     * ตรวจสอบว่าอุปกรณ์สามารถคืนได้หรือไม่
     * @return true ถ้าสามารถคืนได้, false ถ้ายังไม่ถูกยืม
     */
    public boolean canBeReturned() {
        return this.borrowed;
    }
    
    /**
     * บันทึกการยืมอุปกรณ์
     * @param borrowerName ชื่อผู้ยืม
     * @throws IllegalArgumentException ถ้าไม่ระบุชื่อผู้ยืม
     * @throws IllegalStateException ถ้าอุปกรณ์ถูกยืมไปแล้ว
     */
    public void borrowTool(String borrowerName) {
        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Borrower name must not be empty");
        }
        if (!canBeBorrowed()) {
            throw new IllegalStateException("Tool is already borrowed by " + this.borrowerName);
        }
        this.borrowed = true;
        this.borrowerName = borrowerName.trim();
    }
    
    /**
     * บันทึกการคืนอุปกรณ์
     * @throws IllegalStateException ถ้าอุปกรณ์ยังไม่ถูกยืม
     */
    public void returnTool() {
        if (!canBeReturned()) {
            throw new IllegalStateException("Tool is not currently borrowed");
        }
        this.borrowed = false;
        this.borrowerName = "";
    }
}
