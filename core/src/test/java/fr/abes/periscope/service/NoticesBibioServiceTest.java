package fr.abes.periscope.service;

import fr.abes.periscope.CoreTestConfiguration;
import fr.abes.periscope.EnableOnIntegrationTest;
import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(service.findById(1234567).getId()).isEqualTo(1234567);
    }

}
