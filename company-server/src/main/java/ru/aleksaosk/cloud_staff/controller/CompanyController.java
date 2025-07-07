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
import ru.aleksaosk.cloud_staff.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.CompanyService;

@RestController
@RequestMapping(path = "/companies")
@AllArgsConstructor
@Validated
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponseDto addNewCompany(@RequestBody @Valid CompanyRequestDto requestDto) {
        log.info("Пришел запрос на добавление новой company с name = {}", requestDto.getName());
        return companyService.addNewCompany(requestDto);
    }

    @PatchMapping("/{companyId}")
    public CompanyResponseDto updateCompany(@Positive(message = "incorrect companyId")
                                            @PathVariable("companyId") Long id,
                                            @RequestBody @Valid CompanyUpdateRequestDto requestDto) {
        log.info("Пришел запрос на обновление данных company с id = {}", id);
        return companyService.updateCompany(id, requestDto);
    }

    @GetMapping("/{companyId}")
    public CompanyResponseDto getCompany(@Positive(message = "incorrect companyId")
                                         @PathVariable("companyId") Long id) {
        log.info("Пришел запрос на получение company с id = {}", id);
        return companyService.getCompany(id);
    }

    @GetMapping
    public Page<CompanyResponseDto> getAllCompanies(@PositiveOrZero(message = "incorrect from")
                                                    @RequestParam(required = false, defaultValue = "0") int from,
                                                    @Positive(message = "incorrect size")
                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Пришел запрос на получение всех companies");
        return companyService.getAllCompanies(from, size);
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@Positive(message = "incorrect companyId") @PathVariable("companyId") Long id) {
        log.info("Пришел запрос на удаление company с id = {}", id);
        companyService.deleteCompany(id);
    }

    @GetMapping("/{companyId}/users")
    public CompanyShortResponseDto getCompanyById(@Positive(message = "incorrect companyId")
                                                  @PathVariable("companyId") Long companyId) {
        log.info("Пришел запрос на получение company с id = {} для users", companyId);
        return companyService.getCompanyById(companyId);
    }
}
