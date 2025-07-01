package ru.aleksaosk.cloud_staff.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyNameAlreadyInUseException extends RuntimeException {
    private String reason;

    public CompanyNameAlreadyInUseException(String reason) {
        this.reason = reason;
    }
}
