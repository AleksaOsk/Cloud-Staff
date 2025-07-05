package ru.aleksaosk.cloud_staff.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.aleksaosk.cloud_staff.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.entity.Company;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {
    @Mapping(target = "id", ignore = true)
    Company mapToCompany(CompanyRequestDto requestDto);

    @Mapping(target = "users", ignore = true)
    CompanyResponseDto mapToCompanyResponseDto(Company company);

    CompanyShortResponseDto mapToCompanyShortResponseDto(Company company);
}
