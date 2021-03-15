package fr.abes.periscope.service;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import fr.abes.periscope.repository.NoticeRepository;
import fr.abes.periscope.util.NoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public NoticeSolrExtended save(NoticeSolrExtended notice) {
        return noticeRepository.save(notice);
    }

    public void delete(NoticeSolrExtended notice) {
        noticeRepository.delete(notice);
    }
}
