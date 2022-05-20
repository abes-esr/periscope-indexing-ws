package fr.abes.periscope.processor;

import fr.abes.periscope.entity.solr.NoticeSolr;
import fr.abes.periscope.service.NoticeStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PeriscopeIndexWriter implements ItemWriter<NoticeSolr> {
    @Autowired
    private NoticeStoreService service;

    @Override
    public void write(List<? extends NoticeSolr> list) throws Exception {
        log.debug("Ecriture notices ");
        list.forEach(l -> log.debug(l.getPpn() + ", "));
        List<NoticeSolr> listToSave = new ArrayList<>();
        listToSave.addAll(list);
        service.saveOrDelete(listToSave);
    }
}
