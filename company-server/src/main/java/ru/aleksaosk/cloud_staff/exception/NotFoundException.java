package ru.aleksaosk.cloud_staff.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException {
    private String reason;

    public NotFoundException(String reason) {
        this.reason = reason;
    }
}