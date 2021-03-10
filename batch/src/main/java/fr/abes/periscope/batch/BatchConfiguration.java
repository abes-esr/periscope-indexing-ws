package fr.abes.periscope.batch;

import lombok.Data;
import org.springframework.batch.core.Entity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableRetry
@EnableBatchProcessing
@Data
public class BatchConfiguration {
    protected final JobBuilderFactory jobs;
    protected final StepBuilderFactory stepBuilderFactory;
    protected final DataSource baseXmlDataSource;

    @Bean
    public BatchConfigurer configurer(EntityManagerFactory entityManagerFactory) {
        return new IndexingBatchConfigurer(entityManagerFactory);
    }

    /*@Bean
    public Job jobIndexerTableNoticesBibio() {
        return this.jobs.get("indexerTableNoticesBibio")
                .start(managerStep())
                .build();
    }*/



}
