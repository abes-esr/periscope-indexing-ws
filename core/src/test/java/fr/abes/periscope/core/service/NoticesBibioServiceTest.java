package fr.abes.periscope.core.service;

import fr.abes.periscope.CoreTestConfiguration;
import fr.abes.periscope.EnableOnIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@EnableOnIntegrationTest
@SpringBootTest(classes = {CoreTestConfiguration.class})
public class NoticesBibioServiceTest {

    @Autowired
    NoticesBibioService service;

    @Test
    void testFindById() {
        assertThat(service.findById(1234567).getId()).isEqualTo(1234567);
    }

}
