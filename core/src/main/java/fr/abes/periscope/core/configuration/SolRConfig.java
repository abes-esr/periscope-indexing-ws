package fr.abes.periscope.core.configuration;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

/**
 * Configuration du client SolR
 */
@Configuration
public class SolRConfig {

    @Value("${solr.baseurl}")
    private String solrBaseUrl;

    @Bean
    public SolrClient solrClient() {

        if (solrBaseUrl.isEmpty()) {
            throw  new SolrException(SolrException.ErrorCode.SERVER_ERROR,"baseURL is empty");
        }

        ModifiableSolrParams params = new ModifiableSolrParams();
        params.add("wt", "Après");
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


