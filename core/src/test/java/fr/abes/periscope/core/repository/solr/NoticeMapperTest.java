package fr.abes.periscope.core.repository.solr;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.abes.periscope.core.entity.solr.NoticeSolr;
import fr.abes.periscope.core.entity.xml.NoticeXml;
import fr.abes.periscope.core.util.BaseXMLConfiguration;
import fr.abes.periscope.core.util.NoticeMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test l'extraction des dates de publication de la zone 100$a d'une NoticeSolr.
 */
@SpringBootTest(classes = NoticeMapper.class)
@ComponentScan(excludeFilters = @ComponentScan.Filter(BaseXMLConfiguration.class))
public class NoticeMapperTest {

    @Autowired
    private NoticeMapper noticeMapper;

    @Value("classpath:noticeXml/13282261X.xml")
    private Resource xmlFile;

    /**
     * Test titre mort
     */
    @Test
    @DisplayName("Titre mort")
    @Disabled
    public void testDeadTitle() throws IOException {

        String xml = IOUtils.toString(new FileInputStream(xmlFile.getFile()), StandardCharsets.UTF_8);

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        NoticeXml notice = xmlMapper.readValue(xml, NoticeXml.class);

        String expectedPpn = "13282261X";
        String expectedIssn = "21001456";

        NoticeSolr noticeSolr = noticeMapper.map(notice, NoticeSolr.class);

        Assert.assertEquals(expectedPpn, noticeSolr.getPpn());
        Assert.assertEquals(expectedIssn, noticeSolr.getIssn());
    }
}
