package ru.aleksaosk.cloud_staff.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.aleksaosk.cloud_staff.service.CompanyController;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CompanyController.class)
public class CompanyControllerMockDeleteTest extends BaseCompanyControllerTest {
    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/companies/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
