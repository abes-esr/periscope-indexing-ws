package fr.abes.periscope.web.controller;

import fr.abes.periscope.web.PeriscopeIndexationApplicationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PublicTest extends PeriscopeIndexationApplicationTest {

    @InjectMocks
    protected PublicController controller;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(controller);
    }
}
