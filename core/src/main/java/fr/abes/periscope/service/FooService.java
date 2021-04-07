package fr.abes.periscope.service;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class FooService {
    public void saveOrDelete(List<NoticeSolrExtended> notices) {
        List noticeToDelete = new ArrayList();
        List noticeToUpdate = new ArrayList();
        notices.forEach(n -> {
            if (n.isToDelete()) {
                noticeToDelete.add(n);
            } else {
                noticeToUpdate.add(n);
            }
        });
        saveList(noticeToUpdate);
        deleteList(noticeToDelete);
    }

    public NoticeSolrExtended save(NoticeSolrExtended notice) {
        //log.info("Sauvegarde notice " + notice.getId());
        return notice;
    }

    public NoticeSolrExtended delete(NoticeSolrExtended notice) {
        log.info("Suppression notice " + notice.getId());
        return notice;
    }

    public void saveList(List<NoticeSolrExtended> notice) {
        log.debug("sauvegarde liste " + notice.size());
    }

    public void deleteList(List<NoticeSolrExtended> notice) {
        log.debug("suppression liste " + notice.size());
    }
}
