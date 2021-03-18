package fr.abes.periscope.service;

import fr.abes.periscope.entity.xml.NoticesBibio;
import fr.abes.periscope.repository.baseXml.NoticesBibioRepository;
import fr.abes.periscope.util.TrackExecutionTime;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


@Data
@Service
public class NoticesBibioService {
    @Autowired
    private NoticesBibioRepository repository;

    @Autowired
    private EntityManager baseXmlEntityManager;

    public NoticesBibio findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @TrackExecutionTime
    public Integer getMaxId() {
        return repository.findMaxId();
    }

    public Integer getMinId() {
        return repository.findMinId();
    }

    public List<NoticesBibio> findByIdBetween(Integer min, Integer max) {
        Query query = baseXmlEntityManager.createNamedQuery("findByIdBetween");
        query.setParameter("minValue", min);
        query.setParameter("maxValue", max);
        return query.getResultList();
    }
}
