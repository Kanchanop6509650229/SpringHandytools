/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dev.kanchanop.handytools;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * คอนโทรลเลอร์สำหรับจัดการการยืม-คืนอุปกรณ์
 * รับผิดชอบในการจัดการ API endpoints ทั้งหมดที่เกี่ยวข้องกับการจัดการอุปกรณ์
 */
@RestController
public class HandytoolsController {
    private final StorageRepository repository;
    private static final Logger log = LoggerFactory.getLogger(HandytoolsController.class);

    /**
     * คอนสตรักเตอร์สำหรับ HandytoolsController
     * @param repository ที่เก็บข้อมูลสำหรับจัดการอุปกรณ์
     */
    public HandytoolsController(StorageRepository repository) {
        this.repository = repository;
    }

    /**
     * ดึงข้อมูลอุปกรณ์ทั้งหมด
     */
    @GetMapping("/handytools")
    List<Storage> findAll() {
        return repository.findAll();
    }

    /**
     * ค้นหาอุปกรณ์ตาม ID
     * @param id รหัสอุปกรณ์
     */
    @GetMapping("/handytools/{id}")
    Storage findOne(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new StorageNotFoundException(id));
    }

    /**
     * เพิ่มอุปกรณ์ใหม่
     * @param newStorage ข้อมูลอุปกรณ์ที่จะเพิ่ม
     */
    @PostMapping("/handytools")
    Storage newStorage(@RequestBody Storage newStorage) {
        return repository.save(newStorage);
    }

    /**
     * อัพเดทข้อมูลอุปกรณ์ที่มีอยู่
     * @param newStorage ข้อมูลอุปกรณ์ใหม่
     * @param id รหัสอุปกรณ์ที่ต้องการอัพเดท
     */
    @PutMapping("/handytools/{id}")
    Storage replaceStorage(@RequestBody Storage newStorage, @PathVariable Long id) {
        return repository.findById(id)
            .map(storage -> {
                storage.setToolDetail(newStorage.getToolDetail());
                storage.setOwnerName(newStorage.getOwnerName());
                storage.setLocationName(newStorage.getLocationName());
                storage.setBorrowed(newStorage.isBorrowed());
                storage.setBorrowerName(newStorage.getBorrowerName());
                return repository.save(storage);
            })
            .orElseGet(() -> {
                return repository.save(newStorage);
            });
    }

    /**
     * ลบอุปกรณ์ตาม ID
     * @param id รหัสอุปกรณ์ที่ต้องการลบ
     */
    @DeleteMapping("/handytools/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteStorage(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            log.info("Tool {} deleted", id);
        } catch (Exception e) {
            log.warn("Failed to delete tool {}: {}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * บันทึกการยืมอุปกรณ์
     * @param id รหัสอุปกรณ์
     * @param borrowerName ชื่อผู้ยืม
     */
    @PutMapping("/handytools/{id}/borrow")
    Storage borrowTool(@PathVariable Long id, @RequestBody String borrowerName) {
        return repository.findById(id)
            .map(storage -> {
                try {
                    storage.borrowTool(borrowerName);
                    Storage saved = repository.save(storage);
                    log.info("Tool {} borrowed by {}", saved.getToolDetail(), saved.getBorrowerName());
                    return saved;
                } catch (StorageAlreadyBorrowedException e) {
                    log.warn("Tool {} is already borrowed: {}", id, e.getMessage());
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Tool is currently borrowed");
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid borrower name for tool {}: {}", id, e.getMessage());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
                }
            })
            .orElseThrow(() -> new StorageNotFoundException(id));
    }

    /**
     * บันทึกการคืนอุปกรณ์
     * @param id รหัสอุปกรณ์
     */
    @PutMapping("/handytools/{id}/return")
    Storage returnTool(@PathVariable Long id) {
        return repository.findById(id)
            .map(storage -> {
                try {
                    String previousBorrower = storage.getBorrowerName();
                    storage.returnTool();
                    Storage saved = repository.save(storage);
                    log.info("Tool {} returned by {}", saved.getToolDetail(), previousBorrower);
                    return saved;
                } catch (IllegalStateException e) {
                    log.warn("Cannot return tool {}: {}", id, e.getMessage());
                    throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
                }
            })
            .orElseThrow(() -> new StorageNotFoundException(id));
    }

    /**
     * ดึงรายการอุปกรณ์ที่สามารถยืมได้
     */
    @GetMapping("/handytools/available")
    List<Storage> findAvailableTools() {
        return repository.findAll().stream()
            .filter(Storage::canBeBorrowed)
            .toList();
    }

    /**
     * ดึงรายการอุปกรณ์ที่ถูกยืมไปแล้ว
     */
    @GetMapping("/handytools/borrowed")
    List<Storage> findBorrowedTools() {
        return repository.findAll().stream()
            .filter(s -> !s.canBeBorrowed())
            .toList();
    }

    /**
     * ค้นหาอุปกรณ์ที่ถูกยืมโดยผู้ยืมที่ระบุ
     */
    @GetMapping("/handytools/borrowed-by/{borrowerName}")
    List<Storage> findToolsBorrowedBy(@PathVariable String borrowerName) {
        return repository.findAll().stream()
            .filter(s -> s.isBorrowedBy(borrowerName))
            .toList();
    }
}