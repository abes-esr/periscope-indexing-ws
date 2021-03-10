package fr.abes.periscope.core.service;

import fr.abes.periscope.core.MainApplication;
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

    @Test
    void testFindByIdBetween(){
        assertThat(service.findByIdBetween(1,3).size()).isEqualTo(3);
    }
}
