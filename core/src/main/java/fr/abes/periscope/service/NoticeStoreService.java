package fr.abes.periscope.service;

import fr.abes.periscope.entity.solr.NoticeSolr;
import fr.abes.periscope.entity.xml.PeriscopeIndex;
import fr.abes.periscope.repository.baseXml.PeriscopeIndexRepository;
import fr.abes.periscope.repository.solr.NoticeSolrRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Représente la couche service pour les Notices
 */
@Slf4j
@Service
public class NoticeStoreService {

    private final NoticeSolrRepository noticeRepository;
    private final PeriscopeIndexRepository periscopeIndexRepository;

    @Autowired
    public NoticeStoreService(NoticeSolrRepository noticeRepository, PeriscopeIndexRepository periscopeIndexRepository) {
        this.noticeRepository = noticeRepository;
        this.periscopeIndexRepository = periscopeIndexRepository;
    }

    public void saveOrDelete(List<NoticeSolr> notice) {
        List noticeToDeleteSolr = new ArrayList();
        List noticeToUpdateSolr = new ArrayList();
        List<PeriscopeIndex> indexToDelete = new ArrayList<>();
        List<PeriscopeIndex> indexToUpdate = new ArrayList<>();

        notice.forEach(n -> {
            if (n.isToDelete()) {
                noticeToDeleteSolr.add(n);
                indexToDelete.add(new PeriscopeIndex(n.getPpn(), new Date()));
            } else {
                noticeToUpdateSolr.add(n);
                indexToUpdate.add(new PeriscopeIndex(n.getPpn(), new Date()));
            }
        });
        saveList(noticeToUpdateSolr, indexToUpdate);
        deleteList(noticeToDeleteSolr, indexToDelete);
    }

    public void saveList(List<NoticeSolr> notice, List<PeriscopeIndex> index) {
        try {
            if (index.size() > 0) {
                noticeRepository.saveAll(notice);
                periscopeIndexRepository.saveAll(index);
            }
        } catch (DataAccessResourceFailureException ex) {
            log.error("Erreur d'indexation notice : " + ex.getMessage());
        }
    }

    public void deleteList(List<NoticeSolr> notice, List<PeriscopeIndex> index) {
        if (index.size() > 0) {
            noticeRepository.deleteAll(notice);
            periscopeIndexRepository.deleteAll(index);
        }
    }

    public void delete(NoticeSolr notice) {
        noticeRepository.delete(notice);
    }

    public void deleteAll() { noticeRepository.deleteAll(); }

    public NoticeSolr save(NoticeSolr notice) {
        return noticeRepository.save(notice);
    }

    public NoticeSolr findByPpn(String ppn) { return noticeRepository.findByPpn(ppn);}
}
