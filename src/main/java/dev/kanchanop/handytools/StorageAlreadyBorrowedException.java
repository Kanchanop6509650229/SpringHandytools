package dev.kanchanop.handytools;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageAlreadyBorrowedException extends RuntimeException {
    public StorageAlreadyBorrowedException(String borrower) {
        super("Tool is already borrowed by: " + borrower);
    }
}