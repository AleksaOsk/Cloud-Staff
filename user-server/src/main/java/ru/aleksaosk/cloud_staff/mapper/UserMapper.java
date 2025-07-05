package ru.aleksaosk.cloud_staff.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.aleksaosk.cloud_staff.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User mapToUser(UserRequestDto request);

    @Mapping(target = "company", ignore = true)
    UserResponseDto mapToUserResponseDto(User user);

    UserShortResponseDto mapToUserShortResponseDto(User user);
}
