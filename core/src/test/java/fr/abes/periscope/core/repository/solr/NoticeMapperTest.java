package fr.abes.periscope.core.repository.solr;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.abes.periscope.core.entity.solr.NoticeSolr;
import fr.abes.periscope.core.entity.xml.NoticeXml;
import fr.abes.periscope.core.exception.IllegalPublicationYearException;
import fr.abes.periscope.core.util.NoticeMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

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
    public void testDeadTitle() throws IOException {

        File file = new File("/work/developpement/projects/periscope/documents/base_xml/13282261X.xml");
        String xml = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8);

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
