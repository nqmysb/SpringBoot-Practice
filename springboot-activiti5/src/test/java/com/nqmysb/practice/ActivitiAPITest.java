package com.nqmysb.practice;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
 * 测试activitiAPI使用-部署
 * 
 * addInputStream 根据流进行部署。
 * addClasspathResource 根据resource部署。
 * addString根据字符串部署。
 * addZipInputStream根据zip流进行部署。
 * addBpmnModel 根据BpmnModel进行部署。这种方式使用的场景就是我们自己设计一个流程设计器画布，自己去解析成bpmn规范文件。适合动态的拓展。自定义
 * addBytes 新增 字节部署方式
 * 
 * 
 * 五种方式的总结：
 * 1.如果需要自己开发一套流程设计的话就使用addBpmnModel这种方法吧。这种方式更加灵活，缺点就是需要了解每一个对象的含义，需要对bpmnMode对象中的各个子对象都有所了解。
 * 2.如果项目中的流程图是固定的但是一些候选组或者人或者名称不是固定的，需要从数据库中查询出来赋值在部署使用addString这种方法，配合velocity等叶面静态化工具一起使用。
 * 3.如果需要用户自己上传文件部署的话，可以使用addInputStream和addZipInputStream这两种方式。
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest {
	
	
	/**
	 * 部署流程 zip包 -addZipInputStream
	 * 会解压zip文件进行分别存在ACT_GE_BYTEARRAY表中 用二进制保存
	 * ACT_RE_DEPLOYMENT
	 *  
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest1() throws FileNotFoundException {
	    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService rs = engine.getRepositoryService();
        
        DeploymentBuilder builder = rs.createDeployment();
        
//        FileInputStream fis = new FileInputStream(new File("/test/datas.zip"));
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("test/datas.zip");//不需要/
        ZipInputStream zis = new ZipInputStream(is);
        
        builder.addZipInputStream(zis);
        
        builder.deploy();
		
	}
	
	/**
	 * 部署流程 -addInputStream
	 *  
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest2() throws FileNotFoundException {
	    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService rs = engine.getRepositoryService();
        
        DeploymentBuilder builder = rs.createDeployment();
        
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("test/datas.zip");//不需要/
        
        builder.addInputStream("数据申请流程", is);
        
        builder.deploy();
		
	}
	
	
	

}
