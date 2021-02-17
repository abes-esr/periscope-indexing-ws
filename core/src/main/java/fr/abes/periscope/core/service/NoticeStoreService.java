package fr.abes.periscope.core.service;

import fr.abes.periscope.core.entity.solr.NoticeSolr;
import fr.abes.periscope.core.repository.NoticeRepository;
import fr.abes.periscope.core.util.NoticeMapper;
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

    public NoticeSolr save(NoticeSolr notice) {
        return noticeRepository.save(notice);
    }
}
