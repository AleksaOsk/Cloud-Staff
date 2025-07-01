package ru.aleksaosk.cloud_staff.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumberAlreadyInUseException extends RuntimeException {
    private String reason;

    public PhoneNumberAlreadyInUseException(String reason) {
        this.reason = reason;
    }
}
