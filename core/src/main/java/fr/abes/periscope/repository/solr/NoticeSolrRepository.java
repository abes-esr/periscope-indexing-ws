package fr.abes.periscope.repository.solr;

import fr.abes.periscope.entity.solr.NoticeSolr;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeSolrRepository extends SolrCrudRepository<NoticeSolr, String> {

}
