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

@Entity
public class Storage {
    private @Id
    @GeneratedValue
    Long id;
    private String toolDetail;
    private String ownerName;
    private String locationName;
    private boolean borrowed;
    private String borrowerName;

    Storage() {} // Default constructor for JPA

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
}
