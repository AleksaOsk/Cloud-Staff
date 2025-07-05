package ru.aleksaosk.cloud_staff.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.aleksaosk.cloud_staff.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserUpdateRequestDto;
import ru.aleksaosk.cloud_staff.entity.User;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;
import ru.aleksaosk.cloud_staff.manager.CompanyDto;
import ru.aleksaosk.cloud_staff.manager.UserManager;
import ru.aleksaosk.cloud_staff.mapper.UserMapper;
import ru.aleksaosk.cloud_staff.repository.UserRepository;
import ru.aleksaosk.cloud_staff.service.validator.UserValidator;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserManager userManager;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto addNewUser(UserRequestDto requestDto) {
        String phoneNumber = UserValidator.normalizePhone(requestDto.getPhoneNumber());
        UserValidator.checkPhoneNumber(userRepository.findByPhoneNumber(phoneNumber));
        requestDto.setPhoneNumber(phoneNumber);
        CompanyDto companyDto = getCompanyDto(requestDto.getCompanyId());
        User user = userMapper.mapToUser(requestDto);
        user = userRepository.save(user);

        UserResponseDto userResponseDto = userMapper.mapToUserResponseDto(user);
        userResponseDto.setCompany(companyDto);

        log.info("Создали user c id = {}, возвращаем userResponseDto = {}", user.getId(), userResponseDto);
        return userResponseDto;
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto) {
        User user = findUser(id);

        if (!requestDto.getPhoneNumber().isBlank() && !user.getPhoneNumber().equals(requestDto.getPhoneNumber())) {
            String phoneNumber = UserValidator.normalizePhone(requestDto.getPhoneNumber());
            UserValidator.checkPhoneNumber(userRepository.findByPhoneNumber(phoneNumber));
            user.setPhoneNumber(phoneNumber);
        }

        CompanyDto companyDto;
        if (requestDto.getCompanyId() != null && !requestDto.getCompanyId().equals(user.getCompanyId())) {
            companyDto = getCompanyDto(requestDto.getCompanyId());
            user.setCompanyId(requestDto.getCompanyId());
        } else {
            companyDto = getCompanyDto(user.getCompanyId());
        }

        UserValidator.checkName(user, requestDto);
        UserValidator.checkLastName(user, requestDto);

        user = userRepository.save(user);

        UserResponseDto userResponseDto = userMapper.mapToUserResponseDto(user);
        userResponseDto.setCompany(companyDto);

        log.info("Обновили user c id = {}, возвращаем userResponseDto = {}", id, userResponseDto);
        return userResponseDto;
    }

    @Override
    public UserResponseDto getUser(Long id) {
        User user = findUser(id);
        UserResponseDto userResponseDto = userMapper.mapToUserResponseDto(user);
        userResponseDto.setCompany(getCompanyDto(user.getCompanyId()));

        log.info("Возвращаем  UserResponseDto = {}", userResponseDto);
        return userResponseDto;
    }

    @Override
    public Page<UserResponseDto> getAllUsers(int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<User> users = userRepository.findAll(pageable);

        Page<UserResponseDto> userResponseDtoPage = users.map(user -> {
            UserResponseDto userResponseDto = userMapper.mapToUserResponseDto(user);
            userResponseDto.setCompany(getCompanyDto(user.getId()));
            return userResponseDto;
        });

        log.info("Отправляем Page<UserResponseDto> с size = {}", userResponseDtoPage.getContent().size());
        return userResponseDtoPage;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("Удалили user с id = {}", id);
    }

    @Override
    public List<UserShortResponseDto> getUsersByCompanyId(Long componyId) {
        List<UserShortResponseDto> list = userRepository.findAllByCompanyId(componyId).stream()
                .map(userMapper::mapToUserShortResponseDto)
                .toList();
        log.info("Отправляется для CompanyService List<UserShortResponseDto> с size  = {}", list.size());
        return list;
    }

    @Override
    public void deleteUsersByCompanyId(Long compId) {
        userRepository.deleteAllByCompanyId(compId);
        log.info("Были удалены все users для company с id = {}", compId);
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
