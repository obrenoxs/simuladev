package io.github.obrenoxs.simuladev.concept.controller;

import io.github.obrenoxs.simuladev.AbstractIntegrationTest;
import io.github.obrenoxs.simuladev.auth.service.JwtService;
import io.github.obrenoxs.simuladev.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ConceptControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    void findAllShouldReturnConceptsWhenTokenIsValid() throws Exception {

        String token = jwtService.generateToken(UUID.randomUUID(), UserRole.USER);

        mockMvc.perform(get("/api/v1/concepts")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
}
