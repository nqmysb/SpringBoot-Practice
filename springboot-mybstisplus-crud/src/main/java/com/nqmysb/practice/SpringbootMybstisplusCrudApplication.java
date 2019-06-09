package com.nqmysb.practice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.nqmysb.scaffold.mapper.*.*")
@ServletComponentScan
public class SpringbootMybstisplusCrudApplication {



	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybstisplusCrudApplication.class, args);
	}

}
