package ru.aleksaosk.cloud_staff.service;

import ru.aleksaosk.cloud_staff.exception.InvalidPhoneNumberException;
import ru.aleksaosk.cloud_staff.exception.PhoneNumberAlreadyInUseException;
import ru.aleksaosk.cloud_staff.service.entity.User;

import java.util.Optional;

public class UserValidator {
    public static void checkPhoneNumber(Optional<User> userOpt) {
        if (userOpt.isPresent()) {
            throw new PhoneNumberAlreadyInUseException("phone number is already in use");
        }
    }

    public static String normalizePhone(String phoneNumber) {
        String digits = phoneNumber.replaceAll("[^0-9]", "");

        if (digits.length() == 11 && digits.startsWith("8")) {
            return "7" + digits.substring(1);
        } else if (digits.length() == 11 && digits.startsWith("7")) {
            return digits;
        } else if (digits.length() == 10 && !digits.startsWith("7") && !digits.startsWith("8")) {
            return "7" + digits;
        } else {
            throw new InvalidPhoneNumberException("incorrect phone number");
        }
    }
}
