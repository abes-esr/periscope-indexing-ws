package fr.abes.periscope.controller;

import fr.abes.periscope.entity.solr.NoticeSolr;
import fr.abes.periscope.entity.xml.NoticeXml;
import fr.abes.periscope.service.NoticeStoreService;
import fr.abes.periscope.util.NoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class PublicController {

    private final NoticeStoreService noticeStoreService;

    /** Service pour le mapping DTO */
    private NoticeMapper noticeMapper;

    @Autowired
    public PublicController(NoticeStoreService noticeStoreService, NoticeMapper mapper) {
        this.noticeStoreService = noticeStoreService;
        this.noticeMapper = mapper;
    }

    @PutMapping(value = "/notices")
    public String addNotice(@RequestBody @Valid NoticeXml notice) {

        if (notice.isRessourceContinue()) {
            NoticeSolr noticeSolr = noticeMapper.map(notice, NoticeSolr.class);
            noticeStoreService.save(noticeSolr);
            return "OK";
        }
        return "SKIPPED";
    }

    @PostMapping(value = "/notices")
    public String updateNotice(@RequestBody @Valid NoticeXml notice) {

        if (notice.isRessourceContinue()) {
            NoticeSolr noticeSolr = noticeMapper.map(notice, NoticeSolr.class);
            noticeStoreService.save(noticeSolr);
            return "OK";
        }
        return "SKIPPED";
    }

    @DeleteMapping(value = "/notices")
    public String deleteNotice(@RequestBody @Valid NoticeXml notice) {

        if (notice.isRessourceContinue()) {
            NoticeSolr noticeSolr = noticeMapper.map(notice, NoticeSolr.class);
            noticeStoreService.delete(noticeSolr);
            return "OK";
        }
        return "SKIPPED";
    }
}
