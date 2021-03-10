package fr.abes.periscope.core.service;

import fr.abes.periscope.core.entity.xml.NoticesBibio;
import fr.abes.periscope.core.repository.baseXml.NoticesBibioRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticesBibioService {
    @Autowired
    private NoticesBibioRepository repository;

    public List<NoticesBibio> findAll() {
        return repository.findAll();
    }

    public NoticesBibio findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<NoticesBibio> findByIdBetween(Integer min, Integer max) {
        return repository.findByIdBetween(min, max);
    }
}
