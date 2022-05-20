package fr.abes.periscope.processor;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.abes.periscope.entity.solr.NoticeSolr;
import fr.abes.periscope.entity.xml.NoticeXml;
import fr.abes.periscope.entity.xml.NoticesBibio;
import fr.abes.periscope.service.NoticeXmlService;
import fr.abes.periscope.util.NoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PeriscopeIndexProcessor implements ItemProcessor<NoticesBibio, NoticeSolr> {
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private NoticeXmlService service;

    @Override
    public NoticeSolr process(NoticesBibio noticesBibio) throws Exception {
        try {
            log.debug("Traitement notice : " + noticesBibio.getPpn());
            JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(false);
            XmlMapper xmlMapper = new XmlMapper(module);
            NoticeXml noticeXml = xmlMapper.readValue(noticesBibio.getDataXml().getCharacterStream(), NoticeXml.class);
            if (service.isRessourceContinue(noticeXml)) {
                return noticeMapper.map(noticeXml, NoticeSolr.class);
            }
        }catch (Exception ex) {
            log.error("Erreur dans la conversion JSON notice n° : " + noticesBibio.getId() + " Exception " + ex.getClass().getName() + " : " + ex.getMessage());
        }
        return null;
    }
}
