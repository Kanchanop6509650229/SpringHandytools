/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package dev.kanchanop.handytools;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author dlwlrma
 */
public interface StorageRepository extends JpaRepository<Storage, Long> {
    List<Storage> findByBorrowed(boolean borrowed);
    List<Storage> findByBorrowerName(String borrowerName);
    List<Storage> findByOwnerName(String ownerName);
}
