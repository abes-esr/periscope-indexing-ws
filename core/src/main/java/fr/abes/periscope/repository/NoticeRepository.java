package fr.abes.periscope.repository;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends SolrCrudRepository<NoticeSolrExtended, String> {

}
