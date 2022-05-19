package fr.abes.periscope.processor;

import fr.abes.periscope.entity.solr.NoticeSolr;
import fr.abes.periscope.entity.xml.PeriscopeIndex;
import fr.abes.periscope.repository.baseXml.PeriscopeIndexRepository;
import fr.abes.periscope.service.NoticeStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class SolrItemWriter extends AbstractItemStreamItemWriter<NoticeSolr>
        implements ItemStreamWriter<NoticeSolr>, InitializingBean {
    private final NoticeStoreService service;

    public SolrItemWriter(NoticeStoreService noticeStoreService) {
        this.service = noticeStoreService;
    }

    @Override
    public void write(List<? extends NoticeSolr> list) throws Exception {
        long startTime = System.currentTimeMillis();
        List<NoticeSolr> listToSave = new ArrayList<>();
        listToSave.addAll(list);

        service.saveOrDelete(listToSave);

        long endTimeSolr = System.currentTimeMillis();
        log.debug("Temps d'écriture Solr : " + (endTimeSolr - startTime) + "ms");

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
