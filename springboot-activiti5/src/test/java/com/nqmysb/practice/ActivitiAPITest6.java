package com.nqmysb.practice;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试activitiAPI使用-流程操作与数据查看
 * 
 * 6版本的内容 
 * 5版本没有？
 * 
 * 1.流程往下执行的方法
 * task.complete()
 * runservice.trigger(execution1.getId())
 * 
 * runService.signal(execution1.getId());
 * 2.
 * 信号 singal
 * 消息 message
 * 事情定义
 * 捕获事件（Catching） 接受信号才会往下走
 * 
 * 抛出事件（Throwing） 往下走发出一个信号
 * 
 * 

 * 
 
 * 
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest6 {
	
	
	/**
	 * 6版本 流程往下执行
	 */
	@Test
	public  void processTest1() {
		   ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        // 存储服务
	        RepositoryService rs = engine.getRepositoryService();
	        // 运行时服务
	        RuntimeService runService = engine.getRuntimeService();
	        // 任务服务
	        TaskService taskService = engine.getTaskService();
	        // 部署
	        Deployment dep = rs.createDeployment()
	                .addClasspathResource("receiveTask.bpmn").deploy();
	        ProcessDefinition pd = rs.createProcessDefinitionQuery()
	                .deploymentId(dep.getId()).singleResult();
	        // 启动流程
	        ProcessInstance pi = runService.startProcessInstanceById(pd.getId());
	        // 查当前的子执行流（只有一个）
//	        Execution exe = runService.createExecutionQuery()
//	                .processInstanceId(pi.getId()).onlyChildExecutions()
//	                .singleResult();
//
//	        System.out.println(pi.getId() + ", 当前节点：" + exe.getActivityId());
//	        
//	        // 让它往前走
//	        runService.trigger(exe.getId());
//	        
//	        exe = runService.createExecutionQuery()
//	                .processInstanceId(pi.getId()).onlyChildExecutions()
//	                .singleResult();
//	        System.out.println(pi.getId() + ", 当前节点：" + exe.getActivityId());


	}
	
	
	
	/**
	 * 5版本 流程往下执行
	 */
	@Test
	public  void processTest2() {
		   ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        // 存储服务
	        RepositoryService rs = engine.getRepositoryService();
	        // 运行时服务
	        RuntimeService runService = engine.getRuntimeService();
	        // 任务服务
	        TaskService taskService = engine.getTaskService();
	        // 部署
	        Deployment dep = rs.createDeployment()
	                .addClasspathResource("test/receiveTask.bpmn").deploy();
	        ProcessDefinition pd = rs.createProcessDefinitionQuery()
	                .deploymentId(dep.getId()).singleResult();
	        // 启动流程
	        ProcessInstance pi = runService.startProcessInstanceById(pd.getId());
	        
	      
	        
	        Execution execution1 = runService
					.createExecutionQuery()//创建执行对象查询
					.processInstanceId(pi.getId())//使用流程实例ID查询
//					.activityId()//当前活动的id，对应receiveTask.bpmn文件中的活动节点的id的属性值
					.singleResult();
//	        Task task1 = taskService.createTaskQuery().executionId(execution1.getId()).singleResult();
//	        System.out.println("当前节点："+task1.getName());
	        runService.signal(execution1.getId());
	        Task task2 = taskService.createTaskQuery().executionId(execution1.getId()).singleResult();

	        System.out.println("当前节点："+task2.getName());


	}
	
	
	
	
	

	

		


}
