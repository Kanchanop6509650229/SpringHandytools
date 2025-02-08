package dev.kanchanop.handytools;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFoundException extends RuntimeException {
    StorageNotFoundException(Long id) {
        super("Could not find storage " + id);
    }
}