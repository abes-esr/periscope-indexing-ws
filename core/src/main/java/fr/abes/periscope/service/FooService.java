package fr.abes.periscope.service;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FooService {
    public NoticeSolrExtended save(NoticeSolrExtended notice) {
        log.info("Indexation notice " + notice.getId());
        return notice;
    }
}
