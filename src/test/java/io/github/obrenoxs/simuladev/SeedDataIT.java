package io.github.obrenoxs.simuladev;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class SeedDataIT extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void companyTypesShouldHaveThreeSeededRows() {

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM company_types", Integer.class);

        Assertions.assertEquals(3, count);
    }
}
