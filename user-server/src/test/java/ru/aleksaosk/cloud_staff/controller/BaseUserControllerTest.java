package ru.aleksaosk.cloud_staff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.aleksaosk.cloud_staff.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserUpdateRequestDto;
import ru.aleksaosk.cloud_staff.entity.User;
import ru.aleksaosk.cloud_staff.manager.CompanyDto;
import ru.aleksaosk.cloud_staff.mapper.UserMapper;
import ru.aleksaosk.cloud_staff.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public abstract class BaseUserControllerTest {
    @MockitoBean
    protected UserService userService;

    protected final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    protected MockMvc mvc;

    protected final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    protected User user, updatedUser;
    protected UserRequestDto userRequestDto;
    protected UserUpdateRequestDto updatedRequest;
    protected UserResponseDto userResponseDto, updateResponseDto;
    protected UserShortResponseDto userShortResponseDto;
    protected final CompanyDto companyDto = new CompanyDto(1L, "company",
            new BigDecimal(100));
    protected List<UserResponseDto> userResponseDtoList;
    protected List<UserShortResponseDto> userShortResponseDtoList;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto("name", "lastname", "79998880077", 1L);
        user = new User(1L, userRequestDto.getName(), userRequestDto.getLastName(),
                userRequestDto.getPhoneNumber(), userRequestDto.getCompanyId());
        userResponseDto = userMapper.mapToUserResponseDto(user);
        userResponseDto.setCompany(companyDto);

        updatedRequest = new UserUpdateRequestDto("update name", "update lastname", "79998880078", 1L);
        updatedUser = new User(1L, updatedRequest.getName(), updatedRequest.getLastName(),
                updatedRequest.getPhoneNumber(), updatedRequest.getCompanyId());
        updateResponseDto = userMapper.mapToUserResponseDto(updatedUser);
        updateResponseDto.setCompany(companyDto);

        userShortResponseDto = userMapper.mapToUserShortResponseDto(user);
        userShortResponseDtoList = new ArrayList<>();
        userShortResponseDtoList.add(userShortResponseDto);

        userResponseDtoList = new ArrayList<>();
        userResponseDtoList.add(userResponseDto);
    }
}
