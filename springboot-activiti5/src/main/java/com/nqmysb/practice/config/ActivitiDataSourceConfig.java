package com.nqmysb.practice.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
/**
 * 一.activiti流程引擎配置类
 * 主要配置类容有：
 * 1.数据源：DataSource 我这里用DruidDataSource，可以用不同的实现c3p0 dbcp
 * 2.事务管理 transactionManager
 * 3.setDatabaseSchemaUpdate ：
 * flase： 默认值。activiti在启动时，会对比数据库表中保存的版本，如果没有表或者版本不匹配，将抛出异常。
 * true： activiti会对数据库中所有表进行更新操作。如果表不存在，则自动创建。
 * create_drop： 在activiti启动时创建表，在关闭时删除表（必须手动关闭引擎，才能删除表）。
 * drop-create： 在activiti启动时删除原来的旧表，然后在创建新表（不需要手动关闭引擎）。
 * 4.history-level：历史记录等级 full最高级 用于回溯流程历史数据 HistoryLevel.FULL
 * 5.check-process-definitions  是否自动验证部署  false  true 会自动找process文件下的bpmn文件进行部署
 * 二.服务组件
 * 通过流程引擎可以获取七大服务组件
 * 1. RepositoryService : 提供一系列管理流程定义和流程部署的API
 * 2. RuntimeService: 在流程运行时对流程实例进行管理与控制
 * 3. TaskService: 对流程任务进行管理，如任务提醒、任务完成和创建任务等
 * 4. IdentityService: 提供对流程角色数据进行管理的API， 这些角色数据包括用户组、用户和它们之间的关系
 * 5. ManagementService: 提供对流程引擎进行管理和维护的服务
 * 6. HistoryService: 对流程的历史数据进行操作，包括查询、删除这些历史数据
 * 7. DynamicBpmnService: 使用该服务，可以不需要重新部署流程模型，就可以实现对流程模型的部分修改 
 * 
 * 三.数据库表结构
 * 
 * 
 * 
 * @author liaocan
 *
 */
@Configuration
public class ActivitiDataSourceConfig extends AbstractProcessEngineAutoConfiguration {
    @Resource
    private ActivitiDataSourceProperties activitiDataSourceProperties;

    @Bean
    @Primary
    public DataSource activitiDataSource() {
        DruidDataSource DruiddataSource = new DruidDataSource();
        
        DruiddataSource.setUrl(activitiDataSourceProperties.getUrl());
        DruiddataSource.setDriverClassName(activitiDataSourceProperties.getDriverClassName());
        DruiddataSource.setPassword(activitiDataSourceProperties.getPassword());
        DruiddataSource.setUsername(activitiDataSourceProperties.getUsername());
        return DruiddataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(activitiDataSource());
    }

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration() {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(activitiDataSource());
//        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
        configuration.setJobExecutorActivate(true);
        configuration.setTransactionManager(transactionManager());
        configuration.setHistoryLevel(HistoryLevel.FULL);
        configuration.setDbIdentityUsed(false);
        //防止生成流程图乱码
        configuration.setActivityFontName("宋体");
        configuration.setLabelFontName("宋体");
        configuration.setAnnotationFontName("宋体");
        return configuration;
    }


}

