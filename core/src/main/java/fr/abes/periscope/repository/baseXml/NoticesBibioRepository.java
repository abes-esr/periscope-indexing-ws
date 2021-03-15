package fr.abes.periscope.repository.baseXml;

import fr.abes.periscope.entity.xml.NoticesBibio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticesBibioRepository extends JpaRepository<NoticesBibio, Integer> {
    @Query(value = "select max(id) from autorites.noticesbibio", nativeQuery = true)
    Integer findMaxId();

    @Query(value = "select min(id) from autorites.noticesbibio", nativeQuery = true)
    Integer findMinId();
}
