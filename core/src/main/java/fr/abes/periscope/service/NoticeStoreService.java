package fr.abes.periscope.service;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import fr.abes.periscope.repository.NoticeRepository;
import fr.abes.periscope.util.NoticeMapper;
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

    private final NoticeRepository noticeRepository;

    private final NoticeMapper noticeMapper;

    @Autowired
    public NoticeStoreService(NoticeRepository noticeRepository, NoticeMapper mapper) {
        this.noticeRepository = noticeRepository;
        this.noticeMapper = mapper;
    }

    public void saveOrDelete(List<NoticeSolrExtended> notice) {
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

    public Iterable<NoticeSolrExtended> saveList(List<NoticeSolrExtended> notice) {
        try {
            if (notice.size() > 0)
                return noticeRepository.saveAll(notice);
        } catch (DataAccessResourceFailureException ex) {
            log.error("Erreur d'indexation notice : " + ex.getMessage());
        }
            return null;

    }

    public void deleteList(List<NoticeSolrExtended> notice) {
        if (notice.size() > 0)
            noticeRepository.deleteAll(notice);
    }

    public void delete(NoticeSolrExtended notice) {
        noticeRepository.delete(notice);
    }

    public NoticeSolrExtended save(NoticeSolrExtended notice) {
        return noticeRepository.save(notice);
    }
}
