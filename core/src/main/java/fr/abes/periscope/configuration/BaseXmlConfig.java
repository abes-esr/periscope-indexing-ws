package fr.abes.periscope.configuration;


import fr.abes.periscope.util.BaseXMLConfiguration;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "baseXmlEntityManager",
        basePackages = "fr.abes.periscope.repository.baseXml")
@NoArgsConstructor
@BaseXMLConfiguration
@Slf4j
public class BaseXmlConfig {
    @Value("${basexml.datasource.url}")
    private String url;
    @Value("${basexml.datasource.username}")
    private String username;
    @Value("${basexml.datasource.password}")
    private String password;
    @Value("${basexml.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.jpa.show-sql}")
    protected String showsql;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    protected String dialect;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    protected String ddlAuto;
    @Value("${spring.jpa.database-platform}")
    protected String platform;

    protected void configHibernate(LocalContainerEntityManagerFactoryBean em) {
        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("spring.jpa.database-platform", platform);
        properties.put("hibernate.show_sql", showsql);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.dialect", dialect);
        properties.put("logging.level.org.hibernate", "DEBUG");
        properties.put("hibernate.type", "trace");
        em.setJpaPropertyMap(properties);
    }

    @Bean
    public DataSource baseXmlDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        log.debug("Base de données : " + url);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean baseXmlEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(baseXmlDataSource());
        em.setPackagesToScan(
                new String[]{"fr.abes.periscope.entity.xml"});
        configHibernate(em);
        return em;
    }
}