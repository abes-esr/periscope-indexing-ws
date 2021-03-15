package fr.abes.periscope.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManagerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UtilHibernateTest {
    @Autowired
    EntityManagerFactory baseXmlEntityManager;
    @Test
    void testfindNamedQuery() {
        assertThat(UtilHibernate.findNamedQuery(baseXmlEntityManager, "findByIdBetween").query()).isEqualTo("select n from NoticesBibio n where n.id >= :minValue and n.id < :maxValue");
    }
}
