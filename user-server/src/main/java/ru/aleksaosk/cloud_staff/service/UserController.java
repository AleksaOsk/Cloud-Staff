package ru.aleksaosk.cloud_staff.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.aleksaosk.cloud_staff.service.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.UserUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto addNewUser(@RequestBody @Valid UserRequestDto requestDto) {
        return userService.addNewUser(requestDto);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(@Positive(message = "incorrect userId") @PathVariable("userId") Long id,
                                      @RequestBody @Valid UserUpdateRequestDto requestDto) {
        return userService.updateUser(id, requestDto);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@Positive(message = "incorrect userId") @PathVariable("userId") Long id) {
        return userService.getUser(id);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive(message = "incorrect userId") @PathVariable("userId") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/company/{componyId}")
    public List<UserShortResponseDto> getUsersByCompanyId(@Positive(message = "incorrect componyId")
                                                          @PathVariable("componyId") Long componyId) {
        return userService.getUsersByCompanyId(componyId);
    }

    @DeleteMapping("/company/{componyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUsersByCompanyId(@Positive(message = "incorrect compId") @PathVariable("componyId") Long compId) {
        userService.deleteUsersByCompanyId(compId);
    }
}
