package ru.aleksaosk.cloud_staff.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.aleksaosk.cloud_staff.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;
import ru.aleksaosk.cloud_staff.manager.CompanyManager;
import ru.aleksaosk.cloud_staff.service.CompanyMapper;
import ru.aleksaosk.cloud_staff.service.CompanyRepository;
import ru.aleksaosk.cloud_staff.service.CompanyValidator;
import ru.aleksaosk.cloud_staff.service.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.entity.Company;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyManager companyManager;


    @Override
    public CompanyResponseDto addNewCompany(CompanyRequestDto requestDto) {
        log.info("Пришел запрос на добавление новой company с name = {}", requestDto.getName());
        CompanyValidator.checkCompanyName(companyRepository.findByName(requestDto.getName()));
        Company company = CompanyMapper.mapToCompany(requestDto);
        company = companyRepository.save(company);
        return CompanyMapper.mapToCompanyResponseDto(company);
    }

    @Override
    public CompanyResponseDto updateCompany(Long id, CompanyUpdateRequestDto requestDto) {
        log.info("Пришел запрос на обновление данных company с id = {}", id);
        Company company = findCompany(id);
        if (!requestDto.getName().isBlank() && !requestDto.getName().equals(company.getName())) {
            CompanyValidator.checkCompanyName(companyRepository.findByName(requestDto.getName()));
        }

        CompanyMapper.mapToCompany(company, requestDto);
        company = companyRepository.save(company);
        return CompanyMapper.mapToCompanyResponseDto(company, companyManager.getUsersShortDtoList(id));
    }

    @Override
    public CompanyResponseDto getCompany(Long id) {
        log.info("Пришел запрос на получение company с id = {}", id);
        Company company = findCompany(id);
        List<UserShortResponseDto> users = companyManager.getUsersShortDtoList(id);
        return CompanyMapper.mapToCompanyResponseDto(company, users);
    }

    @Override
    public List<CompanyResponseDto> getAllCompanies() {
        log.info("Пришел запрос на получение всех companies");
        return companyRepository.findAll().stream()
                .map((company) -> CompanyMapper.mapToCompanyResponseDto(company,
                        companyManager.getUsersShortDtoList(company.getId())))
                .toList();
    }

    @Override
    public void deleteCompany(Long id) {
        log.info("Пришел запрос на удаление company с id = {}", id);
        companyManager.deleteUsersByCompanyId(id);
        companyRepository.deleteById(id);
    }

    @Override
    public CompanyShortResponseDto getCompanyById(Long compId) {
        log.info("Пришел запрос на получение company с id = {} для users", compId);
        return companyRepository.findById(compId).map(CompanyMapper::mapToCompanyShortResponseDto).orElse(null);
    }

    private Company findCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("company with id = " + id + " not found"));
    }
}
