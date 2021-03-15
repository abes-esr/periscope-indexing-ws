package fr.abes.periscope;

import fr.abes.periscope.partitioner.RangePartitioner;
import fr.abes.periscope.processor.NoticeProcessor;
import fr.abes.periscope.entity.solr.NoticeSolr;
import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import fr.abes.periscope.entity.xml.NoticesBibio;
import fr.abes.periscope.service.FooService;
import fr.abes.periscope.service.NoticesBibioService;
import fr.abes.periscope.util.UtilHibernate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    public static final int GRID_SIZE=8;
    public static final int CHUNK_SIZE=10;

    @Autowired
    private JobBuilderFactory jobs;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private EntityManagerFactory baseXmlEntityManager;
    @Autowired
    private NoticesBibioService noticesBibioService;
    @Autowired
    private FooService fooService;

    @Autowired
    protected DataSource baseXmlDataSource;

    @Bean
    public BatchConfigurer configurer() {
        return new IndexingBatchConfigurer(baseXmlEntityManager);
    }

    @Bean
    public Job jobIndexerTableNoticesBibio() {
        return this.jobs.get("indexerTableNoticesBibio").incrementer(incrementer())
                .start(managerStep())
                .build();
    }

    @Bean
    public Step managerStep() {
        return stepBuilderFactory.get("managerStep").partitioner(slaveStep().getName(), rangePartitioner())
                .partitionHandler(masterSlaveHandler()).build();
    }

    @Bean
    public PartitionHandler masterSlaveHandler() {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setGridSize(GRID_SIZE);
        handler.setTaskExecutor(taskExecutor());
        handler.setStep(slaveStep());
        try {
            handler.afterPropertiesSet();
        } catch (Exception e) {
            log.error("Erreur de configuration du partition handler : " + e.getMessage());
        }
        return handler;
    }

    @Bean(name = "slave")
    public Step slaveStep() {
        return stepBuilderFactory.get("slave").<NoticesBibio, NoticeSolrExtended>chunk(CHUNK_SIZE)
                .reader(slaveReader(null, null))
                .processor(slaveProcessor(null)).writer(slaveWriter()).build();
    }

    @Bean
    public RangePartitioner rangePartitioner() {
        return new RangePartitioner(noticesBibioService);
    }

    @Bean
    public SimpleAsyncTaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    @StepScope
    public NoticeProcessor slaveProcessor(@Value("#{stepExecutionContext[name]}") String name) {
        log.info("Appel au slaveProcessor");
        NoticeProcessor noticeProcessor = new NoticeProcessor();
        noticeProcessor.setThreadName(name);
        return noticeProcessor;
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<NoticesBibio> slaveReader(
            @Value("#{stepExecutionContext['minValue']}") Integer minValue,
            @Value("#{stepExecutionContext['maxValue']}") Integer maxValue) {
        log.info("slaveReader start " + minValue + " " + maxValue);
        JpaPagingItemReader reader = new JpaPagingItemReader();
        reader.setEntityManagerFactory(baseXmlEntityManager);
        reader.setQueryString(UtilHibernate.findNamedQuery(baseXmlEntityManager, "findByIdBetween").query());
        Map parameters = new HashMap();
        parameters.put("minValue", minValue);
        parameters.put("maxValue", maxValue);
        reader.setParameterValues(parameters);
        reader.setPageSize(CHUNK_SIZE);
        reader.setSaveState(false);
        log.info("slaveReader end " + minValue + " " + maxValue);
        return reader;
    }


    private ItemWriterAdapter<NoticeSolr> slaveWriter() {
        ItemWriterAdapter<NoticeSolr> itemWriterAdapter = new ItemWriterAdapter<>();
        itemWriterAdapter.setTargetObject(fooService);
        itemWriterAdapter.setTargetMethod("save");
        itemWriterAdapter.setArguments(new BeanWrapperFieldExtractor[]{new BeanWrapperFieldExtractor<NoticeSolr>()});
        return itemWriterAdapter;
    }

    protected JobParametersIncrementer incrementer() {
        return new TimeIncrementer();
    }
}
