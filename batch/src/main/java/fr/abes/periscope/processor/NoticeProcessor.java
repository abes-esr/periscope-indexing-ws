package fr.abes.periscope.processor;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import fr.abes.periscope.entity.xml.NoticeXml;
import fr.abes.periscope.entity.xml.NoticesBibio;
import fr.abes.periscope.service.NoticeXmlService;
import fr.abes.periscope.util.NoticeMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Getter @Setter
@Slf4j
public class NoticeProcessor implements ItemProcessor<NoticesBibio, NoticeSolrExtended> {
    private String threadName;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private NoticeXmlService service;

    @Override
    public NoticeSolrExtended process(NoticesBibio notice) throws Exception {
        log.info("Processing " + threadName + " : notice n°" + notice.getId());
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        NoticeXml noticeXml = xmlMapper.readValue(notice.getDataXml(), NoticeXml.class);
        if (service.isRessourceContinue(noticeXml)) {
            return noticeMapper.map(noticeXml, NoticeSolrExtended.class);
        }
        return null;

    }
}
