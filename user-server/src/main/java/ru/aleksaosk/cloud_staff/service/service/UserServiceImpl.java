package ru.aleksaosk.cloud_staff.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;
import ru.aleksaosk.cloud_staff.manager.CompanyDto;
import ru.aleksaosk.cloud_staff.manager.UserManager;
import ru.aleksaosk.cloud_staff.service.UserMapper;
import ru.aleksaosk.cloud_staff.service.UserRepository;
import ru.aleksaosk.cloud_staff.service.UserValidator;
import ru.aleksaosk.cloud_staff.service.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.UserUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.entity.User;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserManager userManager;

    @Override
    public UserResponseDto addNewUser(UserRequestDto requestDto) {
        log.info("Пришел запрос на создание нового пользователя с phoneNumber = {}", requestDto.getPhoneNumber());

        String phoneNumber = UserValidator.normalizePhone(requestDto.getPhoneNumber());
        UserValidator.checkPhoneNumber(userRepository.findByPhoneNumber(phoneNumber));
        CompanyDto companyDto = getCompanyDto(requestDto.getCompanyId());
        User user = UserMapper.mapToUser(requestDto, phoneNumber);
        user = userRepository.save(user);

        return UserMapper.mapToUserResponseDto(user, companyDto);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto) {
        log.info("Пришел запрос на обновление данных для user с id = {}", id);

        User user = findUser(id);
        if (!requestDto.getPhoneNumber().isBlank() && !user.getPhoneNumber().equals(requestDto.getPhoneNumber())) {
            String phoneNumber = UserValidator.normalizePhone(requestDto.getPhoneNumber());
            UserValidator.checkPhoneNumber(userRepository.findByPhoneNumber(phoneNumber));
            requestDto.setPhoneNumber(phoneNumber);
        }

        CompanyDto companyDto;
        if (requestDto.getCompanyId() != null && !requestDto.getCompanyId().equals(user.getCompanyId())) {
            companyDto = getCompanyDto(requestDto.getCompanyId());
        } else {
            companyDto = getCompanyDto(user.getCompanyId());
        }

        UserMapper.mapToUpdateUser(requestDto, user);
        user = userRepository.save(user);

        return UserMapper.mapToUserResponseDto(user, companyDto);
    }

    @Override
    public UserResponseDto getUser(Long id) {
        log.info("Пришел запрос на получение user с id = {}", id);
        User user = findUser(id);
        return UserMapper.mapToUserResponseDto(user, userManager.getCompanyDto(user.getCompanyId()));
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        log.info("Пришел запрос на получение списка всех users");
        return userRepository.findAll().stream()
                .map(user -> UserMapper.mapToUserResponseDto(user, userManager.getCompanyDto(user.getCompanyId())))
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Пришел запрос на удаление user с id = {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserShortResponseDto> getUsersByCompanyId(Long componyId) {
        log.info("Пришел запрос на получение списка всех users для company с id = {}", componyId);
        return userRepository.findAllByCompanyId(componyId).stream()
                .map(UserMapper::mapToUserShortResponseDto)
                .toList();
    }

    @Override
    public void deleteUsersByCompanyId(Long compId) {
        log.info("Пришел запрос на удаление всех users для company с id = {}", compId);
        userRepository.deleteAllByCompanyId(compId);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user with id = " + id + " not found"));
    }

    private CompanyDto getCompanyDto(Long companyId) {
        CompanyDto companyDto = userManager.getCompanyDto(companyId);
        if (companyDto == null) {
            throw new NotFoundException("company with id = " + companyId + " not found");
        }
        return companyDto;
    }
}
