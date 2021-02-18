package fr.abes.periscope.core.repository;

import fr.abes.periscope.core.entity.solr.NoticeSolrExtended;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends SolrCrudRepository<NoticeSolrExtended, String> {

}
