package fr.abes.periscope.repository.solr;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.abes.periscope.CoreTestConfiguration;
import fr.abes.periscope.EnableOnIntegrationTest;
import fr.abes.periscope.entity.solr.NoticeSolr;
import fr.abes.periscope.entity.xml.NoticeXml;
import fr.abes.periscope.service.NoticeStoreService;
import fr.abes.periscope.util.NoticeMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@EnableOnIntegrationTest
@SpringBootTest(classes = {CoreTestConfiguration.class})
public class SolrIntegrationTest {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private NoticeStoreService noticeService;

    @Value("classpath:noticeXml/13282261X.xml")
    private Resource xmlFile;

    @Test
    @DisplayName("ajout d'une notice avec exemplaire")
    @Disabled
    public void addNoticeToSolr() throws IOException {

        String xml = IOUtils.toString(new FileInputStream(xmlFile.getFile()), StandardCharsets.UTF_8);

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        NoticeXml notice = xmlMapper.readValue(xml, NoticeXml.class);

        NoticeSolr noticeSolr = noticeMapper.map(notice, NoticeSolr.class);

        System.out.println(noticeSolr);

        noticeService.save(noticeSolr);
    }

    @Test
    @DisplayName("supression d'une notice avec exemplaire")
    @Disabled
    public void deleteNoticeToSolr() throws IOException {

        String xml = IOUtils.toString(new FileInputStream(xmlFile.getFile()), StandardCharsets.UTF_8);

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        NoticeXml notice = xmlMapper.readValue(xml, NoticeXml.class);

        NoticeSolr noticeSolr = noticeMapper.map(notice, NoticeSolr.class);

        System.out.println(noticeSolr);

        noticeService.delete(noticeSolr);
    }

}

