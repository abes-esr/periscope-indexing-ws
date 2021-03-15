package fr.abes.periscope.repository.solr;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import fr.abes.periscope.entity.xml.NoticeXml;
import fr.abes.periscope.util.NoticeMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test l'extraction des dates de publication de la zone 100$a d'une NoticeSolr.
 */
@SpringBootTest
public class NoticeMapperTest {

    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * Test titre mort
     */
    @Test
    @DisplayName("Titre mort")
    @Disabled
    public void testDeadTitle() throws IOException {

        File file = new File("/work/developpement/projects/periscope/documents/base_xml/13282261X.xml");
        String xml = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8);

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        NoticeXml notice = xmlMapper.readValue(xml, NoticeXml.class);

        String expectedPpn = "13282261X";
        String expectedIssn = "21001456";

        NoticeSolrExtended noticeSolr = noticeMapper.map(notice, NoticeSolrExtended.class);

        System.out.println(noticeSolr);

        Assert.assertEquals(expectedPpn, noticeSolr.getPpn());
        Assert.assertEquals(expectedIssn, noticeSolr.getIssn());
    }
}
