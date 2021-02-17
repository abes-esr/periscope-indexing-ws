package fr.abes.periscope.web.controller;

import fr.abes.periscope.core.entity.solr.NoticeSolr;
import fr.abes.periscope.core.entity.xml.NoticeXml;
import fr.abes.periscope.core.service.NoticeStoreService;
import fr.abes.periscope.core.util.NoticeMapper;
import fr.abes.periscope.web.util.DtoMapper;
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

        NoticeSolr noticeSolr = noticeMapper.map(notice, NoticeSolr.class);
        noticeStoreService.save(noticeSolr);
        return "OK";
    }
}
