package com.nqmysb.practice;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试activitiAPI使用-流程定义
 * 
 * 流程定义和流程实例 
 * 请假流程定义   申请  --》部门领导 --》公司领导 --》人事确认 --》结束
 * 
 * 请假流程实例 张三请假 -- 王总 --- 李总 -- 小刘 --结束
 * 
 * 
 * suspendProcessDefinitionByXXX
 * activateProcessDefinitionByXXX
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest2 {
	
	
	/**
	 *  部署 bpmn 默认 部署 bpmn+png两个文件
	 *  添加图片 不会用默认的图片
	 *  
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest1() throws FileNotFoundException {
		  ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        // 存储服务
	        RepositoryService rs = engine.getRepositoryService();
	        
	        DeploymentBuilder builder = rs.createDeployment();
	        builder = builder.addClasspathResource("test/test1.bpmn").addClasspathResource("test/test2.png");
	        Deployment deployment = builder.deploy();
		
	}
	
	
	/**
	 *  流程定义终止  无法启动流程实例  相当于废除法律
	 *  SUSPENSION_STATE_ 是否挂起 1 激活 2挂起 
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest2() throws FileNotFoundException {
		  ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        // 存储服务
	        RepositoryService rs = engine.getRepositoryService();
	        
	        DeploymentBuilder builder = rs.createDeployment();
	        builder.addClasspathResource("test/test3.bpmn");
	        Deployment dep = builder.deploy();
	        
	        ProcessDefinition def = rs.createProcessDefinitionQuery()
	                .deploymentId(dep.getId()).singleResult();  

	        System.out.println("id: " + def.getId());
	        rs.suspendProcessDefinitionByKey(def.getKey());
	        // 将会抛出异常，因为流程定义被中止了 org.activiti.engine.ActivitiException: Cannot start process instance. Process definition My process (id = myProcess:3:12504) 
	        //is suspended
	        RuntimeService runService = engine.getRuntimeService();
	        runService.startProcessInstanceByKey(def.getKey());
		
	}
	
	
	/**
	 *  流程定义授权
	 *  
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest3() throws FileNotFoundException {

		   ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        // 存储服务
	        RepositoryService rs = engine.getRepositoryService();
	        IdentityService is = engine.getIdentityService();
	        
	        User user = is.newUser(UUID.randomUUID().toString());
	        user.setFirstName("Angus");
	        is.saveUser(user);
	        
	        DeploymentBuilder builder = rs.createDeployment();
	        builder.addClasspathResource("test/test3.bpmn");
	        Deployment dep = builder.deploy();
	        
	        
	        ProcessDefinition def = rs.createProcessDefinitionQuery()
	                .deploymentId(dep.getId()).singleResult();
	        // 用户和流程定义中间表 ACT_RU_IDENTITYLINK  用户id  流程定义id
	        //20005	1		candidate	50052b66-c112-463b-a7c1-d056aed7b01b			myProcess:6:20004
	        rs.addCandidateStarterUser(def.getId(), user.getId());
	        // 流程定义的用户权限  数据权限功能
	        //根据用户id查询他可以启动的流程定义有哪些？
	        List<ProcessDefinition> defs = rs.createProcessDefinitionQuery().startableByUser(user.getId()).list();
	        for(ProcessDefinition de : defs) {
	            System.out.println(de.getId());
	        }
	}
	


}
