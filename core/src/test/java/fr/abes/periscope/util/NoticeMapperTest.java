package fr.abes.periscope.util;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.abes.periscope.entity.solr.NoticeSolr;
import fr.abes.periscope.entity.xml.NoticeXml;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest(classes = {NoticeMapper.class})
public class NoticeMapperTest {
    @Autowired
    private NoticeMapper mapper;


    @Value("classpath:noticeXml/037596225.xml")
    private Resource xmlFileOrphan;

    @Value("classpath:noticeXml/037982176.xml")
    private Resource xmlFilePC;

    @Test
    @DisplayName("Test génération statut bibliothèque orphelin")
    void testStatutBibliothequeOrphelin() throws IOException {
        String xml = IOUtils.toString(new FileInputStream(xmlFileOrphan.getFile()), StandardCharsets.UTF_8);

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        NoticeXml notice = xmlMapper.readValue(xml, NoticeXml.class);

        NoticeSolr noticeSolr = mapper.map(notice, NoticeSolr.class);
        Assertions.assertEquals(1, noticeSolr.getStatutList().size());
        Assertions.assertEquals("Orphelin", noticeSolr.getStatutList().stream().findFirst().get());
    }

    @Test
    @DisplayName("Test génération statut bibliothèque PC")
    void testStatutBibliothequePC() throws IOException {
        String xml = IOUtils.toString(new FileInputStream(xmlFilePC.getFile()), StandardCharsets.UTF_8);

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        NoticeXml notice = xmlMapper.readValue(xml, NoticeXml.class);

        NoticeSolr noticeSolr = mapper.map(notice, NoticeSolr.class);
        Assertions.assertEquals(1, noticeSolr.getStatutList().size());
        Assertions.assertEquals("PC", noticeSolr.getStatutList().stream().findFirst().get());
    }
}
