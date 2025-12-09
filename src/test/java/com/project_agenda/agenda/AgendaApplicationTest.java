package com.project_agenda.agenda;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgendaApplicationTest {

    @Test
    void contextLoads() {
    }
    @Test
    void testMainMethodRuns() {
        assertDoesNotThrow(() ->
                AgendaApplication.main(new String[]{})
        );
    }
}