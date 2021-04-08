package fr.abes.periscope.configuration;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.convert.SolrJConverter;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

/**
 * Configuration du client SolR
 */
@Configuration
public class SolRConfig {

    @Value("${solr.baseurl}")
    private String solrBaseUrl;

    @Bean
    public SolrClient solrClient() {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.add("wt", "javabin");
        params.add("version","2.2");
        params.add("indent", "on");
        params.add("omitHeader","true");

        HttpSolrClient.Builder builder = new HttpSolrClient.Builder()
                .withBaseSolrUrl(this.solrBaseUrl)
                .withInvariantParams(params)
                .withResponseParser(new BinaryResponseParser());

        HttpSolrClient client = builder.build();

        return client;
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) {
        SolrTemplate template = new SolrTemplate(client);
        return template;
    }
}


