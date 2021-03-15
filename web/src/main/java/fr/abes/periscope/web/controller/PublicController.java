package fr.abes.periscope.web.controller;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
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
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    public PublicController(NoticeStoreService service) {
        noticeStoreService = service;
    }

    @PostMapping(value = "/notices")
    public String addNotice(@RequestBody @Valid NoticeXml notice) {

        NoticeSolrExtended noticeSolr = noticeMapper.map(notice, NoticeSolrExtended.class);
        noticeStoreService.save(noticeSolr);
        return "OK";
    }
}
