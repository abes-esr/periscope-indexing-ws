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

    /**
     * Méthode permettant d'ajouter ou de supprimer des notices dans l'index et de mettre à jour la table PERISCOPE_INDEX
     * @param notice liste des notices solr à traiter
     */
    public void saveOrDelete(List<NoticeSolr> notice) {
        //liste des notices à supprimer de solr
        List noticeToDeleteSolr = new ArrayList();
        //liste des notices à créer / mettre à jour dans solr
        List noticeToUpdateSolr = new ArrayList();
        //liste des tuples à supprimer dans periscope_index
        List<PeriscopeIndex> indexToDelete = new ArrayList<>();
        //liste des tuples à ajouter / mettre à jour dans periscope_index
        List<PeriscopeIndex> indexToUpdate = new ArrayList<>();

        notice.forEach(n -> {
            if (n.isToDelete()) {
                //si notice à supprimer on crée une entre dans les deux listes correspondantes
                noticeToDeleteSolr.add(n);
                indexToDelete.add(new PeriscopeIndex(n.getPpn(), new Date()));
            } else {
                noticeToUpdateSolr.add(n);
                indexToUpdate.add(new PeriscopeIndex(n.getPpn(), new Date()));
            }
        });
        //sauvegarde des notices dans solr et dans periscope_index
        saveList(noticeToUpdateSolr, indexToUpdate);
        //suppression des notices dans l'index solr et dans periscope_index
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
        try {
            if (index.size() > 0) {
                noticeRepository.deleteAll(notice);
                periscopeIndexRepository.deleteAll(index);
            }
        } catch (DataAccessResourceFailureException ex) {
            log.error("Erreur de suppression dans l'index solr " + ex.getMessage());
        }
    }

    public void delete(NoticeSolr notice) {
        noticeRepository.delete(notice);
    }

    public NoticeSolr save(NoticeSolr notice) {
        return noticeRepository.save(notice);
    }

    public NoticeSolr findByPpn(String ppn) { return noticeRepository.findByPpn(ppn);}
}
