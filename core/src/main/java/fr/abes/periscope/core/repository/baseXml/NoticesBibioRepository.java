package fr.abes.periscope.core.repository.baseXml;

import fr.abes.periscope.core.entity.xml.NoticesBibio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticesBibioRepository extends JpaRepository<NoticesBibio, Integer> {
    List<NoticesBibio> findByIdBetween(Integer min, Integer max);
}
