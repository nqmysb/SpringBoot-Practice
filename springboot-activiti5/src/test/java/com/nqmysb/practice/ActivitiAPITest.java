package com.nqmysb.practice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试activitiAPI使用
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest {
	
	@Test
	public  void deployTest() throws FileNotFoundException {
	    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService rs = engine.getRepositoryService();
        
        DeploymentBuilder builder = rs.createDeployment();
        
        FileInputStream fis = new FileInputStream(new File("/test/datas.zip"));
        ZipInputStream zis = new ZipInputStream(fis);
        
        builder.addZipInputStream(zis);
        
        builder.deploy();
		
	}
	
	
	

}
