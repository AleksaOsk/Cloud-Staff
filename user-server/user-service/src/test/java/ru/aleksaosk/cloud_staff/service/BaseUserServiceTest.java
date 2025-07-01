package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aleksaosk.cloud_staff.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.manager.UserManager;
import ru.aleksaosk.cloud_staff.service.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.UserUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.entity.User;
import ru.aleksaosk.cloud_staff.service.service.UserServiceImpl;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public abstract class BaseUserServiceTest {
    @Mock
    protected UserRepository userRepository;
    @Mock
    protected UserManager userManager;
    @InjectMocks
    protected UserServiceImpl userService;

    protected User user, updatedUser;
    protected UserRequestDto userRequestDto;
    protected UserUpdateRequestDto updatedRequest;
    protected UserResponseDto userResponseDto, updateResponseDto;
    protected UserShortResponseDto userShortResponseDto;
    protected final CompanyShortResponseDto companyDto = new CompanyShortResponseDto(1L, "company",
            new BigDecimal(100));
    protected List<UserResponseDto> userResponseDtoList;
    protected List<UserShortResponseDto> userShortResponseDtoList;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto("name", "lastname", "79998880077", 1L);
        user = new User(1L, userRequestDto.getName(), userRequestDto.getLastName(),
                userRequestDto.getPhoneNumber(), userRequestDto.getCompanyId());
        userResponseDto = UserMapper.mapToUserResponseDto(user, companyDto);

        updatedRequest = new UserUpdateRequestDto("update name", "update lastname", "79998880078", 1L);
        updatedUser = new User(1L, updatedRequest.getName(), updatedRequest.getLastName(),
                updatedRequest.getPhoneNumber(), updatedRequest.getCompanyId());
        updateResponseDto = UserMapper.mapToUserResponseDto(updatedUser, companyDto);

        userShortResponseDto = UserMapper.mapToUserShortResponseDto(user);
        userShortResponseDtoList = List.of(userShortResponseDto);

        userResponseDtoList = List.of(userResponseDto);
    }
}
