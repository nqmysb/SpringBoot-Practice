package com.nqmysb.practice;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("com.nqmysb.practice.mapper.*.*")
@ServletComponentScan
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class})
public class ServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

}
