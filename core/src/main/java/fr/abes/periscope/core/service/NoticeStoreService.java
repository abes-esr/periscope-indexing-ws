package fr.abes.periscope.core.service;

import fr.abes.periscope.core.entity.solr.NoticeSolr;
import fr.abes.periscope.core.repository.solr.NoticeSolrRepository;
import fr.abes.periscope.core.util.NoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente la couche service pour les Notices
 */
@Slf4j
@Service
public class NoticeStoreService {

    private final NoticeSolrRepository noticeRepository;

    private final NoticeMapper noticeMapper;

    @Autowired
    public NoticeStoreService(NoticeSolrRepository noticeRepository, NoticeMapper mapper) {
        this.noticeRepository = noticeRepository;
        this.noticeMapper = mapper;
    }

    public void saveOrDelete(List<NoticeSolr> notice) {
        List noticeToDelete = new ArrayList();
        List noticeToUpdate = new ArrayList();
        notice.forEach(n -> {
            if (n.isToDelete()) {
                noticeToDelete.add(n);
            } else {
                noticeToUpdate.add(n);
            }
        });
        saveList(noticeToUpdate);
        deleteList(noticeToDelete);
    }

    public Iterable<NoticeSolr> saveList(List<NoticeSolr> notice) {
        try {
            if (notice.size() > 0)
                return noticeRepository.saveAll(notice);
        } catch (DataAccessResourceFailureException ex) {
            log.error("Erreur d'indexation notice : " + ex.getMessage());
        }
        return null;
    }

    public void deleteList(List<NoticeSolr> notice) {
        if (notice.size() > 0)
            noticeRepository.deleteAll(notice);
    }

    public void delete(NoticeSolr notice) {
        noticeRepository.delete(notice);
    }

    public NoticeSolr save(NoticeSolr notice) {
        return noticeRepository.save(notice);
    }
}
