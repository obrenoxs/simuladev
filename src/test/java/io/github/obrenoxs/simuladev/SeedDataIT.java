package io.github.obrenoxs.simuladev;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeedDataIT extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void companyTypesShouldHaveThreeSeededRows() {

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM company_types", Integer.class);

        assertEquals(3, count);
    }

    @Test
    void companyTypesShouldAllHaveFoundationalConcept() {

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM company_types WHERE foundational_concept_id IS NULL",
                Integer.class);

        assertEquals(0, count);
    }

    @Test
    void conceptPrerequisitesShouldHaveOneSeededRow() {

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM concept_prerequisites",
                Integer.class);

        assertEquals(1, count);
    }

    @Test
    void conceptsShouldHaveThreeSeededRows() {

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM concepts",
                Integer.class);

        assertEquals(3, count);
    }
}
