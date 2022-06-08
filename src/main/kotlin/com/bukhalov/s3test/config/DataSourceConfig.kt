package com.bukhalov.s3test.config

import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.type.JdbcType
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource
import org.springframework.context.annotation.Configuration as SpringConfiguration

@SpringConfiguration
@MapperScan(basePackages = ["com.bukhalov.s3test.persistence.mapper"])
class DataSourceConfig(private val dataSource: DataSource) {

    /**
     * Конфигурация DataSource.
     *
     * @return DataSourceTransactionManager
     */
    @Bean
    fun transactionManager(): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    /**
     * SqlSession factory.
     *
     * @return SqlSessionFactoryBean
     */
    @Bean
    fun sqlSessionFactory(): SqlSessionFactoryBean {
        val configuration = Configuration().apply {
            this.isMapUnderscoreToCamelCase = true
            this.jdbcTypeForNull = JdbcType.NULL
            this.autoMappingUnknownColumnBehavior = AutoMappingUnknownColumnBehavior.NONE
            this.isLazyLoadingEnabled = true
            this.isAggressiveLazyLoading = true
        }
        val sqlSessionFactoryBean = SqlSessionFactoryBean().apply {
            this.setDataSource(dataSource)
            this.setTypeHandlersPackage("com.bukhalov.s3test.handlers")
            this.setConfiguration(configuration)
        }
        return sqlSessionFactoryBean
    }
}
