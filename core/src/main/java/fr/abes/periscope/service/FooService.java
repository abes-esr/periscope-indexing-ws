package fr.abes.periscope.service;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FooService {
    public NoticeSolrExtended saveOrDelete(NoticeSolrExtended notice) {
        if (notice.isToDelete())
            return delete(notice);
        return save(notice);
    }

    public NoticeSolrExtended save(NoticeSolrExtended notice) {
        //log.info("Sauvegarde notice " + notice.getId());
        return notice;
    }

    public NoticeSolrExtended delete(NoticeSolrExtended notice) {
        log.info("Suppression notice " + notice.getId());
        return notice;
    }
}
