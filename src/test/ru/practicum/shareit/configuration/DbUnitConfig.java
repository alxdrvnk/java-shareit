package ru.practicum.shareit.configuration;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

@TestConfiguration
public class DbUnitConfig {

    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection(DataSource dataSource) {
        DatabaseConfigBean bean = new DatabaseConfigBean();
        bean.setDatatypeFactory(new H2DataTypeFactory());

        DatabaseDataSourceConnectionFactoryBean factoryBean = new DatabaseDataSourceConnectionFactoryBean();
        factoryBean.setDataSource(dataSource);
        bean.setQualifiedTableNames(true);
        factoryBean.setDatabaseConfig(bean);
        return factoryBean;
    }
}
