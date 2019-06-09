package com.nqmysb.practice.config;

import java.util.Properties;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;

/**
 * @author liaocan
 * @since 2018-08-10
 */
@Configuration
@MapperScan("com.nqmysb.practice.mapper.*.*")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
    
    
    /**
     * SQL执行效率插件
     * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
     */
    @Bean
//    @Profile({"dev","pro"})// 设置 dev pro 环境开启
    public PerformanceInterceptor performanceInterceptor() {
    	  PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
          //格式化sql语句
          Properties properties = new Properties();
          properties.setProperty("format", "true");
          performanceInterceptor.setProperties(properties);
          return performanceInterceptor;
    }
    
    /**
     * sequence主键，需要配置一个主键生成器
     * 配合实体类注解 {@link KeySequence} + {@link TableId} type=INPUT
     * @return
     */
    @Bean
    public OracleKeyGenerator oracleKeyGenerator(){
      return new OracleKeyGenerator();
    }
}
