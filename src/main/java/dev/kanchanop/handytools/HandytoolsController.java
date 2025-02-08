/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dev.kanchanop.handytools;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dlwlrma
 */
@RestController
public class HandytoolsController {
    private final StorageRepository repository;

    public HandytoolsController(StorageRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/handytools")
    List<Storage> findAll() {
        return repository.findAll();
    }

    @GetMapping("/handytools/{id}")
    Storage findOne(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new StorageNotFoundException(id));
    }

    @PostMapping("/handytools")
    Storage newStorage(@RequestBody Storage newStorage) {
        return repository.save(newStorage);
    }

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

    @DeleteMapping("/handytools/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteStorage(@PathVariable Long id) {
        repository.deleteById(id);
    }
}