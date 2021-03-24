package fr.abes.periscope.processor;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import fr.abes.periscope.service.NoticeStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SolrItemWriter extends AbstractItemStreamItemWriter<NoticeSolrExtended>
        implements ItemStreamWriter<NoticeSolrExtended>, InitializingBean {
    private NoticeStoreService service;

    public SolrItemWriter(NoticeStoreService noticeStoreService) {
        this.service = noticeStoreService;
    }

    @Override
    public void write(List<? extends NoticeSolrExtended> list) throws Exception {
        long startTime = System.currentTimeMillis();
        List<NoticeSolrExtended> listToSave = new ArrayList<>();
        listToSave.addAll(list);
        service.saveOrDelete(listToSave);
        long endTime = System.currentTimeMillis();
        log.debug("Temps d'écriture Solr : " + (endTime - startTime) + "ms");
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
