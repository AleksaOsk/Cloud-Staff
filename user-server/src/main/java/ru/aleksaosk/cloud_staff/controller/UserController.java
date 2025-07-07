package ru.aleksaosk.cloud_staff.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.aleksaosk.cloud_staff.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto addNewUser(@RequestBody @Valid UserRequestDto requestDto) {
        log.info("Пришел запрос на создание нового пользователя с phoneNumber = {}", requestDto.getPhoneNumber());
        return userService.addNewUser(requestDto);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(@Positive(message = "incorrect userId") @PathVariable("userId") Long id,
                                      @RequestBody @Valid UserUpdateRequestDto requestDto) {
        log.info("Пришел запрос на обновление данных для user с id = {}", id);
        return userService.updateUser(id, requestDto);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@Positive(message = "incorrect userId") @PathVariable("userId") Long id) {
        log.info("Пришел запрос на получение user с id = {}", id);
        return userService.getUser(id);
    }

    @GetMapping
    public Page<UserResponseDto> getAllUsers(@PositiveOrZero(message = "incorrect from")
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @Positive(message = "incorrect size")
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Пришел запрос на получение списка всех users");
        return userService.getAllUsers(from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive(message = "incorrect userId") @PathVariable("userId") Long id) {
        log.info("Пришел запрос на удаление user с id = {}", id);
        userService.deleteUser(id);
    }

    @GetMapping("/company/{componyId}")
    public List<UserShortResponseDto> getUsersByCompanyId(@Positive(message = "incorrect componyId")
                                                          @PathVariable("componyId") Long componyId) {
        log.info("Пришел запрос на получение списка всех users для company с id = {}", componyId);
        return userService.getUsersByCompanyId(componyId);
    }

    @DeleteMapping("/company/{componyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUsersByCompanyId(@Positive(message = "incorrect compId") @PathVariable("componyId") Long compId) {
        log.info("Пришел запрос на удаление всех users для company с id = {}", compId);
        userService.deleteUsersByCompanyId(compId);
    }
}
