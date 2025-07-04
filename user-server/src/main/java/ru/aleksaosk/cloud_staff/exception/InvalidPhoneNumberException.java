package ru.aleksaosk.cloud_staff.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidPhoneNumberException extends RuntimeException {
    private String reason;

    public InvalidPhoneNumberException(String reason) {
        this.reason = reason;
    }
}
