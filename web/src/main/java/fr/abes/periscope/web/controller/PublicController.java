package fr.abes.periscope.web.controller;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import fr.abes.periscope.entity.xml.NoticeXml;
import fr.abes.periscope.service.NoticeStoreService;
import fr.abes.periscope.service.NoticeXmlService;
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

    private final NoticeXmlService noticeXmlService;

    /** Service pour le mapping DTO */
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    public PublicController(NoticeStoreService noticeStoreService, NoticeXmlService noticeXmlService) {
        this.noticeStoreService = noticeStoreService;
        this.noticeXmlService = noticeXmlService;
    }

    @PostMapping(value = "/notices")
    public String addNotice(@RequestBody @Valid NoticeXml notice) {
        if (noticeXmlService.isRessourceContinue(notice)) {
            NoticeSolrExtended noticeSolr = noticeMapper.map(notice, NoticeSolrExtended.class);
            noticeStoreService.save(noticeSolr);
            return "OK";
        }
        return "SKIPPED";
    }
}
