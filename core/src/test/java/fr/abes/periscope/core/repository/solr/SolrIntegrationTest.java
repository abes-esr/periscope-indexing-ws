package fr.abes.periscope.core.repository.solr;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.abes.periscope.core.EnableOnIntegrationTest;
import fr.abes.periscope.core.entity.solr.NoticeSolr;
import fr.abes.periscope.core.entity.xml.NoticeXml;
import fr.abes.periscope.core.service.NoticeStoreService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@EnableOnIntegrationTest
@SpringBootTest
public class SolrIntegrationTest {

    @Autowired
    private NoticeStoreService noticeService;

    @Test
    @DisplayName("historiette #id 12")
    public void testId12() throws IOException {

        File file = new File("/work/developpement/projects/periscope/documents/base_xml/13282261X.xml");
        //File file = new File("/work/developpement/projects/periscope/documents/base_xml/000000469.xml");
        String xml = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8);

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);

        NoticeXml notice = xmlMapper.readValue(xml, NoticeXml.class);

        System.out.println(notice);

        NoticeSolr noticeSolR = new NoticeSolr();

        noticeSolR.setPpn(notice.getControlFields().get(0).getValue());

        noticeService.save(noticeSolR);
    }

}

