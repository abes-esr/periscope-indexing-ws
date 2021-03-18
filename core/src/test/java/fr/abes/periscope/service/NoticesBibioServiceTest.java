package fr.abes.periscope.service;

import fr.abes.periscope.MainApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MainApplication.class)
public class NoticesBibioServiceTest {
    @Autowired
    NoticesBibioService service;
    @Test
    void testFindById() {
        assertThat(service.findById(1).getId()).isEqualTo(1);
    }

}
