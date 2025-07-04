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
import ru.aleksaosk.cloud_staff.service.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.service.CompanyService;

import java.util.List;

@RestController
@RequestMapping(path = "/companies")
@AllArgsConstructor
@Validated
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponseDto addNewCompany(@RequestBody @Valid CompanyRequestDto requestDto) {
        return companyService.addNewCompany(requestDto);
    }

    @PatchMapping("/{companyId}")
    public CompanyResponseDto updateCompany(@Positive(message = "incorrect companyId")
                                            @PathVariable("companyId") Long id,
                                            @RequestBody @Valid CompanyUpdateRequestDto requestDto) {
        return companyService.updateCompany(id, requestDto);
    }

    @GetMapping("/{companyId}")
    public CompanyResponseDto getCompany(@Positive(message = "incorrect companyId")
                                         @PathVariable("companyId") Long id) {
        return companyService.getCompany(id);
    }

    @GetMapping
    public List<CompanyResponseDto> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@Positive(message = "incorrect companyId") @PathVariable("companyId") Long id) {
        companyService.deleteCompany(id);
    }

    @GetMapping("/{companyId}/users")
    public CompanyShortResponseDto getCompanyById(@Positive(message = "incorrect companyId")
                                                  @PathVariable("companyId") Long companyId) {
        return companyService.getCompanyById(companyId);
    }
}
