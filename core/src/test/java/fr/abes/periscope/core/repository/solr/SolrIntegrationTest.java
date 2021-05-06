package fr.abes.periscope.core.repository.solr;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.abes.periscope.CoreTestConfiguration;
import fr.abes.periscope.EnableOnIntegrationTest;
import fr.abes.periscope.core.entity.solr.NoticeSolr;
import fr.abes.periscope.core.entity.xml.NoticeXml;
import fr.abes.periscope.core.service.NoticeStoreService;
import fr.abes.periscope.core.service.NoticesBibioService;
import fr.abes.periscope.core.util.BaseXMLConfiguration;
import fr.abes.periscope.core.util.NoticeMapper;
import org.apache.commons.io.IOUtils;
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
import static org.assertj.core.api.Assertions.assertThat;

@EnableOnIntegrationTest
@SpringBootTest(classes = {CoreTestConfiguration.class})
@ComponentScan(excludeFilters = @ComponentScan.Filter(BaseXMLConfiguration.class))
public class SolrIntegrationTest {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private NoticeStoreService noticeService;

    @Autowired
    private NoticesBibioService service;

    @Value("classpath:noticeXml/13282261X.xml")
    private Resource xmlFile1;

    private NoticeSolr getNoticeFromFile(Resource file) throws IOException {
        String xml = IOUtils.toString(new FileInputStream(xmlFile1.getFile()), StandardCharsets.UTF_8);

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        NoticeXml notice = xmlMapper.readValue(xml, NoticeXml.class);
        return noticeMapper.map(notice, NoticeSolr.class);
    }

    @Test
    @DisplayName("ajout d'une notice avec exemplaire")
    public void addNoticeToSolr() throws IOException {
        NoticeSolr notice = getNoticeFromFile(xmlFile1);
        noticeService.save(notice);
    }

    @Test
    @DisplayName("supression d'une notice avec exemplaire")
    public void deleteNoticeToSolr() throws IOException {
        NoticeSolr notice = getNoticeFromFile(xmlFile1);
        noticeService.delete(notice);
    }

    @Test
    @DisplayName("test indexation champs notice")
    public void indexChampsNotice() throws IOException {
        NoticeSolr notice = getNoticeFromFile(xmlFile1);
        noticeService.save(notice);
        NoticeSolr noticesolrOut = noticeService.findByPpn("13282261X");

        System.out.println(noticesolrOut);
        assertThat(noticesolrOut.getIssn()).isEqualTo("2100-1456");
        assertThat(noticesolrOut.getEditor()).isEqualTo("Fédération Allier Nature, FAN");
        assertThat(noticesolrOut.getKeyTitle()).isEqualTo("Auvergne environnement");
        assertThat(noticesolrOut.getKeyShortedTitle()).isEqualTo("Autre titre");
        assertThat(noticesolrOut.getProperTitle()).isEqualTo("Auvergne environnement");
        assertThat(noticesolrOut.getTitleFromDifferentAuthor()).isEqualTo("Titre from different author");
        assertThat(noticesolrOut.getParallelTitle()).isEqualTo("Titre parallèle");
        assertThat(noticesolrOut.getTitleComplement()).isEqualTo("magazine trimestriel des associations pour la nature et l'environnement");
        assertThat(noticesolrOut.getSectionTitle()).isEqualTo("Titre de section");
        assertThat(noticesolrOut.getKeyTitleQualifer()).isEqualTo("Key title qualifier");
        assertThat(noticesolrOut.getNbLocation()).isEqualTo(5);
        assertThat(noticesolrOut.getLanguage()).isEqualTo("fre");
        assertThat(noticesolrOut.getCountry()).isEqualTo("FR");
        assertThat(noticesolrOut.getEndYear()).isEqualTo(2014);
        assertThat(noticesolrOut.getEndYearConfidenceIndex()).isEqualTo(0);
        assertThat(noticesolrOut.getStartYear()).isEqualTo(2009);
        assertThat(noticesolrOut.getStartYearConfidenceIndex()).isEqualTo(0);
        assertThat(noticesolrOut.getExternalURLs().size()).isEqualTo(2);

        //noticeService.delete(notice);
    }

}
