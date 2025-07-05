package ru.aleksaosk.cloud_staff.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.aleksaosk.cloud_staff.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyUpdateRequestDto;
import ru.aleksaosk.cloud_staff.entity.Company;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;
import ru.aleksaosk.cloud_staff.manager.CompanyManager;
import ru.aleksaosk.cloud_staff.manager.UserDto;
import ru.aleksaosk.cloud_staff.mapper.CompanyMapper;
import ru.aleksaosk.cloud_staff.repository.CompanyRepository;
import ru.aleksaosk.cloud_staff.service.validator.CompanyValidator;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyManager companyManager;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyResponseDto addNewCompany(CompanyRequestDto requestDto) {
        CompanyValidator.checkCompanyName(companyRepository.findByName(requestDto.getName()));
        Company company = companyMapper.mapToCompany(requestDto);
        company = companyRepository.save(company);
        CompanyResponseDto companyResponseDto = companyMapper.mapToCompanyResponseDto(company);
        log.info("Создали company c id = {}, возвращаем companyResponseDto = {}", company.getId(), companyResponseDto);
        return companyResponseDto;
    }

    @Override
    public CompanyResponseDto updateCompany(Long id, CompanyUpdateRequestDto requestDto) {
        Company company = findCompany(id);
        if (!requestDto.getName().isBlank() && !requestDto.getName().equals(company.getName())) {
            CompanyValidator.checkCompanyName(companyRepository.findByName(requestDto.getName()));
            company.setName(requestDto.getName());
        }
        if (requestDto.getBudget() != null && !company.getBudget().equals(requestDto.getBudget())) {
            company.setBudget(requestDto.getBudget());
        }

        company = companyRepository.save(company);
        CompanyResponseDto companyResponseDto = companyMapper.mapToCompanyResponseDto(company);
        companyResponseDto.setUsers(getUsersList(id));

        log.info("Обновили company c id = {}, возвращаем companyResponseDto = {}", id, companyResponseDto);
        return companyResponseDto;
    }

    @Override
    public CompanyResponseDto getCompany(Long id) {
        CompanyResponseDto companyResponseDto = companyMapper.mapToCompanyResponseDto(findCompany(id));
        companyResponseDto.setUsers(getUsersList(id));

        log.info("Возвращаем  CompanyResponseDto = {}", companyResponseDto);
        return companyResponseDto;
    }

    @Override
    public Page<CompanyResponseDto> getAllCompanies(int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Company> companies = companyRepository.findAll(pageable);

        Page<CompanyResponseDto> companyResponseDtoPage = companies.map(company -> {
            CompanyResponseDto companyResponseDto = companyMapper.mapToCompanyResponseDto(company);
            companyResponseDto.setUsers(getUsersList(company.getId()));
            return companyResponseDto;
        });

        log.info("Отправляем Page<CompanyResponseDto> с size = {}", companyResponseDtoPage.getContent().size());
        return companyResponseDtoPage;
    }

    @Override
    public void deleteCompany(Long id) {
        companyManager.deleteUsersByCompanyId(id);
        companyRepository.deleteById(id);
        log.info("Удалили company с id = {}", id);
    }

    @Override
    public CompanyShortResponseDto getCompanyById(Long compId) {
        CompanyShortResponseDto companyResponseDto = companyRepository.findById(compId)
                .map(companyMapper::mapToCompanyShortResponseDto).orElse(null);

        log.info("Отправляем для UserService companyDto  = {}", companyResponseDto);
        return companyResponseDto;
    }

    private Company findCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("company with id = " + id + " not found"));
    }

    private List<UserDto> getUsersList(Long id) {
        return companyManager.getUsersShortDtoList(id);
    }
}
