package fr.abes.periscope.repository.solr;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.abes.periscope.entity.solr.NoticeSolr;
import fr.abes.periscope.entity.xml.NoticeXml;
import fr.abes.periscope.exception.IllegalPublicationYearException;
import fr.abes.periscope.util.BaseXMLConfiguration;
import fr.abes.periscope.util.NoticeMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
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

    @Value("classpath:noticeXml/129542059.xml")
    private Resource xmlFile2;

    /**
     * Test titre mort
     */
    @Test
    @DisplayName("Titre mort")
    public void testDeadTitle() throws IOException {

        String xml = IOUtils.toString(new FileInputStream(xmlFile.getFile()), StandardCharsets.UTF_8);

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        NoticeXml notice = xmlMapper.readValue(xml, NoticeXml.class);

        String expectedPpn = "13282261X";
        String expectedIssn = "2100-1456";

        NoticeSolr noticeSolr = noticeMapper.map(notice, NoticeSolr.class);

        Assertions.assertEquals(expectedPpn, noticeSolr.getPpn());
        Assertions.assertEquals(expectedIssn, noticeSolr.getIssn());
    }

    @Test
    @DisplayName("test construction date de début et date de fin")
    public void buildStartPublicationYearTest() {
        String value = "        f    1975";
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getYear(), "1975");
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getConfidenceIndex(), Integer.valueOf(0));
        value = "        f19941995";
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getYear(), "1994");
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getConfidenceIndex(), Integer.valueOf(1));
        value = "        f17801789";
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getYear(), "1780");
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getConfidenceIndex(), Integer.valueOf(9));
        value = "        b20002010";
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getYear(), "2000");
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getConfidenceIndex(), Integer.valueOf(0));
        Assertions.assertEquals(noticeMapper.buildEndPublicationYear(value).getYear(), "2010");
        Assertions.assertEquals(noticeMapper.buildEndPublicationYear(value).getConfidenceIndex(), Integer.valueOf(0));
        value = "        b200     ";
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getYear(), "200X");
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getConfidenceIndex(), Integer.valueOf(10));
        Assertions.assertEquals(noticeMapper.buildEndPublicationYear(value).getYear(), null);
        Assertions.assertEquals(noticeMapper.buildEndPublicationYear(value).getConfidenceIndex(), Integer.valueOf(0));
        value = "        a200 9999";
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getYear(), "200X");
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getConfidenceIndex(), Integer.valueOf(10));
        Assertions.assertEquals(noticeMapper.buildEndPublicationYear(value).getYear(), null);
        Assertions.assertEquals(noticeMapper.buildEndPublicationYear(value).getConfidenceIndex(), Integer.valueOf(0));
        value = "        a200 1234";
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getYear(), "200X");
        Assertions.assertEquals(noticeMapper.buildStartPublicationYear(value).getConfidenceIndex(), Integer.valueOf(10));
        String finalValue = value;
        Assertions.assertThrows(IllegalPublicationYearException.class, () -> {noticeMapper.buildEndPublicationYear(finalValue).getYear();});

    }

    @Test
    void testErreurNbLocs() throws IOException {
        String xml = IOUtils.toString(new FileInputStream(xmlFile2.getFile()), StandardCharsets.UTF_8);

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        NoticeXml notice = xmlMapper.readValue(xml, NoticeXml.class);

        NoticeSolr noticeSolr = noticeMapper.map(notice, NoticeSolr.class);

        Assertions.assertEquals(1, noticeSolr.getNbLocation().intValue());


    }
}
