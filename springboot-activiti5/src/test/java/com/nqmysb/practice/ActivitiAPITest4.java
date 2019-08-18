package com.nqmysb.practice;

import java.io.FileNotFoundException;
import java.util.UUID;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nqmysb.practice.entity.user.User;

/**
 * 测试activitiAPI使用-任务参数与附件
 * 
 * 参数类型：
 * 基本类型参数 ACT_RU_VARIABLE 存放变量  
 * 序列化参数，如对象等 需要多存放ACT_GE_BYTEARRAY表
 * 
 * 参数的作用域：
 * 本地参数  当前任务节点有用   setVariableLocal getVariableLocal  方法不要混用
 * 全局参数  下一节点任务有效   getVariable setVariable
 * 
 * 
 * 四种设置全局变量
 * ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);	
 * 		
 * taskService.complete(taskId, variables);
 * 
 * taskService.setVariable("2903", "price", 2000);
 * 
 * runtimeService.setVariable("503", "price", 10000);
 * 
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest4 {
	
	
	/**
	 * 任务参数-多种数据类型 string  boolean 对象
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest1() throws FileNotFoundException {
		
		   ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        TaskService ts = engine.getTaskService();
	        
	        Task task = ts.newTask(UUID.randomUUID().toString());
	        task.setName("测试任务");
	        ts.saveTask(task);
	        
	        ts.setVariable(task.getId(), "var1", "hello");
	        ts.setVariable(task.getId(), "var2", true);
	        User u = new User();
	        u.setUserid("1");
	        u.setUsername("lc");
	        ts.setVariable(task.getId(), "var3", u);
	        
	        
	        String s1 = ts.getVariable(task.getId(), "var1", String.class);
	        System.out.println("---" + s1);
	        Boolean b1 = ts.getVariable(task.getId(), "var2", Boolean.class);
	        System.out.println("---" + b1);
	        User u1 = ts.getVariable(task.getId(), "var3", User.class);
	        System.out.println(u1.getUserid() + "---" + u1.getUsername());
	        
	        /*	        
	         * ACT_RU_VARIABLE 表中存变量
	         * 基本类型  type  string boolean serializable
	         *  text_ 存放值  
	         *  long 0或者1   
	         *  BYTEARRAY_ID	ACT_GE_BYTEARRAY 存放序列化值  
	         *  
	         *  1	57503	1	boolean			var2			d5215a2d-1506-4d05-b509-8426ee263cf1			1			AAASz4AAUAAAANFAAA
				2	57506	1	serializable	var3			d5215a2d-1506-4d05-b509-8426ee263cf1	57505					AAASz4AAUAAAANFAAB
				3	57501	1	string			var1			d5215a2d-1506-4d05-b509-8426ee263cf1				hello		AAASz4AAUAAAANFAAC

	        */
	               
	               
		
	}
	
	
	/**
	 * 本地参数  当前任务节点  本地有效  存数据库ACT_RU_VARIABLE 下一个节点删除
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest2() throws FileNotFoundException {
		
	    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	    // 存储服务
	    RepositoryService rs = engine.getRepositoryService();
	    // 运行时服务
	    RuntimeService runService = engine.getRuntimeService();
	    // 任务服务
	    TaskService taskService = engine.getTaskService();
	    
	    Deployment dep = rs.createDeployment().addClasspathResource("test/var_local.bpmn").deploy();
	    ProcessDefinition pd = rs.createProcessDefinitionQuery().deploymentId(dep.getId()).singleResult();
	    
	    //启动流程实例  通过流程定义id 不是部署ID  可以根据流程定义id 和流程定义key启动流程实例
	    ProcessInstance pi = runService.startProcessInstanceById(pd.getId());
	    
	    Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
	    taskService.setVariableLocal(task.getId(), "days", 3);
	    System.out.println("当前任务：" + task.getName() + ", days参数：" + taskService.getVariableLocal(task.getId(), "days"));
	    
	    taskService.complete(task.getId());
	    
	    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
	    System.out.println("当前任务：" + task.getName() + ", days参数：" + taskService.getVariableLocal(task.getId(), "days"));
		
		 /*   
		  * 当前任务：task1, days参数：3
		  * 当前任务：task2, days参数：null
		  * */
	               
	               
		
	}
	
	
	/**
	 * 全局参数  下一节点有效  会存数据库ACT_RU_VARIABLE
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest3() throws FileNotFoundException {
		
	    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	    // 存储服务
	    RepositoryService rs = engine.getRepositoryService();
	    // 运行时服务
	    RuntimeService runService = engine.getRuntimeService();
	    // 任务服务
	    TaskService taskService = engine.getTaskService();
	    
	    Deployment dep = rs.createDeployment().addClasspathResource("test/var_local.bpmn").deploy();
	    ProcessDefinition pd = rs.createProcessDefinitionQuery().deploymentId(dep.getId()).singleResult();
	    
	    //启动流程实例  通过流程定义id 不是部署ID  可以根据流程定义id 和流程定义key启动流程实例
	    ProcessInstance pi = runService.startProcessInstanceById(pd.getId());
	    
	    Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
	    taskService.setVariable(task.getId(), "days", 3);
	    System.out.println("当前任务：" + task.getName() + ", days参数：" + taskService.getVariable(task.getId(), "days"));
	    
	    taskService.complete(task.getId());
	    
	    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
	    System.out.println("当前任务：" + task.getName() + ", days参数：" + taskService.getVariable(task.getId(), "days"));
		
/*	    当前任务：task1, days参数：3
	    当前任务：task2, days参数：3*/
               

	}
	
	
	
	/**
	 *  数据对象  在bpmn中设置
	 *   <dataObject id="personName" name="personName"
            itemSubjectRef="xsd:string">
            <extensionElements>
                <activiti:value>Crazyit</activiti:value>
            </extensionElements>
        </dataObject>
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest4() throws FileNotFoundException {
		
		  ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        // 存储服务
	        RepositoryService rs = engine.getRepositoryService();
	        // 运行时服务
	        RuntimeService runService = engine.getRuntimeService();
	        // 任务服务
	        TaskService taskService = engine.getTaskService();
	        
	        Deployment dep = rs.createDeployment().addClasspathResource("test/dataobject.bpmn").deploy();
	        ProcessDefinition pd = rs.createProcessDefinitionQuery().deploymentId(dep.getId()).singleResult();
	        
	        ProcessInstance pi = runService.startProcessInstanceById(pd.getId());
	        
	        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
	        String var = taskService.getVariable(task.getId(), "personName", String.class);
	        System.out.println(var);
	}
	
	
	
	/**
	 * 任务附件参数
	 * 存数据  ACT_GE_BYTEARRAY  ACT_HI_ATTACHMENT
	 * 取数据
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest5() throws FileNotFoundException {
		
	
	}
	
	

	
	

	


}
