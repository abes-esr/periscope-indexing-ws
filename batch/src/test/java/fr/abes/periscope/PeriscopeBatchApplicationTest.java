package fr.abes.periscope;

import fr.abes.periscope.batch.BatchApplication;
import org.junit.Test;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BatchApplication.class)
public class PeriscopeBatchApplicationTest {

    @Test
    public void contextLoads() {
        System.out.println("To be implemented");
        Assert.assertTrue(true);
    }

}
