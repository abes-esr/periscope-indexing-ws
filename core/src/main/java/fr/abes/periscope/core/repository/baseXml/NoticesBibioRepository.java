package fr.abes.periscope.core.repository.baseXml;

import fr.abes.periscope.core.entity.xml.NoticesBibio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticesBibioRepository extends JpaRepository<NoticesBibio, Integer> {

    @Query(value="select id,data_xml from NOTICESBIBIO where id < ?1 ORDER BY id DESC OFFSET 0 ROWS FETCH NEXT ?2 ROWS ONLY;", nativeQuery = true)
    List<NoticesBibio> findByIdBetween(Integer lastIndex, Integer limit);

    @Query(value="Select max(id) from NOTICESBIBIO", nativeQuery = true)
    Integer getMaxId();
}
