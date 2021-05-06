package fr.abes.periscope.service;

import fr.abes.periscope.entity.solr.NoticeSolr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class FooService {
    public void saveOrDelete(List<NoticeSolr> notices) {
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

    public NoticeSolr save(NoticeSolr notice) {
        //log.info("Sauvegarde notice " + notice.getId());
        return notice;
    }

    public NoticeSolr delete(NoticeSolr notice) {
        log.info("Suppression notice " + notice.getId());
        return notice;
    }

    public void saveList(List<NoticeSolr> notice) {
        log.debug("sauvegarde liste " + notice.size());
    }

    public void deleteList(List<NoticeSolr> notice) {
        log.debug("suppression liste " + notice.size());
    }
}
