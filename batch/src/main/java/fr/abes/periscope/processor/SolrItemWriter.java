package fr.abes.periscope.processor;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import fr.abes.periscope.service.FooService;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;


public class SolrItemWriter extends AbstractItemStreamItemWriter<NoticeSolrExtended>
        implements ItemStreamWriter<NoticeSolrExtended>, InitializingBean {
    private FooService service;

    public SolrItemWriter(FooService fooService) {
        this.service = fooService;
    }

    @Override
    public void write(List<? extends NoticeSolrExtended> list) throws Exception {
        List<NoticeSolrExtended> listToSave = new ArrayList<>();
        listToSave.addAll(list);
        service.saveOrDelete(listToSave);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
