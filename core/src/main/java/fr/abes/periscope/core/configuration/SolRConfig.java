package fr.abes.periscope.core.configuration;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

/**
 * Configuration du client SolR
 */
@Configuration
public class SolRConfig {

    @Bean
    public SolrClient solrClient() {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.add("wt", "xml");
        params.add("version","2.2");
        params.add("indent", "on");
        params.add("omitHeader","true");

        HttpSolrClient.Builder builder = new HttpSolrClient.Builder()
                .withBaseSolrUrl("http://localhost:8983/solr/")
                .withInvariantParams(params)
                .withResponseParser(new XMLResponseParser());

        HttpSolrClient client = builder.build();

        return client;
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        SolrTemplate template = new SolrTemplate(client);
        return template;
    }
}


