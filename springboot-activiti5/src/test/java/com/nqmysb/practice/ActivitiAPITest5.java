package com.nqmysb.practice;

import java.io.FileNotFoundException;
import java.util.List;

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
 * 测试activitiAPI使用-执行流 流程实例
 * 1.ACT_RU_EXECUTION  启动的流程实例表
 * 流程实例 - 一般情况主执行流和流程实例重合  一定存在
 * 子执行流  -流程有多少个分支就有做少个执行流记录
 * processInstance execution的子类
 * execution 
 * 在Activiti中Execution和ProcessInstance都用于获取当前流程实例的相关信息。
 * 当流程中没有分支时，Execution等同于ProcessInstance，甚至连ID也相同；
 * 当流程中存在分支(fork, parallel gateway)，则在分支口会形成子Execution，在下一个gateway才会合并(joined)
 * 
 * ACT_RU_EXECUTION 中存在分支  通过父执行流id 这样形成一个树结构
 * 流程的分支然后又会出现子分支 这样的树行结构
 * ID_  执行流程id 
 * PROC_INST_ID_  流程实例id
 * PARENT_ID_     父执行流程id 
 * PROC_DEF_ID_  流程定义id
 * 当流程没有分支是  执行流和流程实例重合   只有一条 只有一个根数据  
 * 注意区别
 * activiti6版本中 只有一条分支时 会生成一条流程实例和一条执行流程数据  先对5版本中多了一条执行流记录 
 * activiti5版本 两条记录时重合的只有一条 
 * 两条分支以上时一样 
 * 1+n条记录
 * 
 * 有多个分支时 就会生成多少条执行流execution记录 父节点就是父分支的id
 * 
 * ACT_RU_EXECUTION  分支执行流执行完了合并时 执行流记录会删除 历史表 ACT_HI_PROCINST中只存流程实例记录 主执行流
 * ACT_RU_TASK   任务开始时会加入到历史数据ACT_HI_TASKINST中去
 * ACT_RU_VARIABLE  本地变量当前节点(执行流)时存在，下一个节点（执行流）时数据删除，所以拿不到，全局变量流程实例存在即可以拿到
 * 
 * 2.流程启动 方式
 * startProcessInstanceById
 * 
 * startProcessInstanceByKey
 * 
 * startProcessInstanceByMessage
 * 
 * 
 * 
 * 3.流程参数（流程变量） 本地参数和全局参数
 * ACT_RU_VARIABLE  参数变量表
 * ID_  变量id
 * TYPE_  变量类型  string boolean serialize 
 * EXECUTION_ID_  执行流
 * PROC_INST_ID_  流程实例
 * TASK_ID_  任务
 * BYTEARRAY_ID_  serialize类型id 
 * DOUBLE_  变量值
 * LONG_
 * TEXT_
 * TEXT2_
 * 
 * 局部参数特点
 * 如果是EXECUTION_ID_  则是和执行流绑定  执行流在参数在 不然就删除了
 * 如果是TASK_ID_  则是和任务节点  当前任务节点参数在 不然就删除了
 * 

 * 
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest5 {
	
	
	/**
	 * 一条执行路径流程实例
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest1() throws FileNotFoundException {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService rs = engine.getRepositoryService();
        // 运行时服务
        RuntimeService runService = engine.getRuntimeService();
        // 任务服务
        TaskService taskService = engine.getTaskService();
        // 部署
        Deployment dep = rs.createDeployment().addClasspathResource("test/single.bpmn").deploy();
        ProcessDefinition pd = rs.createProcessDefinitionQuery().deploymentId(dep.getId()).singleResult();
        // 启动流程
        ProcessInstance pi = runService.startProcessInstanceById(pd.getId());
        System.out.println(pi.getId());


	}
	
	
	/**
	 * 
	 * 多条执行路径流程实例
	 *   id			p_s_id      pid         p_d_id
	 * 	47505	1	47505				myProcess:9:47504		parallelgateway1	0	0	1	0	1	2			
		47508	1	47505		47505	myProcess:9:47504		usertask1	1	1	0	0	1	7			
		47509	1	47505		47505	myProcess:9:47504		usertask2	1	1	0	0	1	7			

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
        // 部署
        Deployment dep = rs.createDeployment().addClasspathResource("test/multi.bpmn").deploy();
        ProcessDefinition pd = rs.createProcessDefinitionQuery().deploymentId(dep.getId()).singleResult();
        // 启动流程
        ProcessInstance pi = runService.startProcessInstanceById(pd.getId());
        System.out.println(pi.getId());


	}
	
	/**
	 * 启动流程实例
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
        // 部署
        Deployment dep = rs.createDeployment().addClasspathResource("test/start.bpmn").deploy();
        ProcessDefinition pd = rs.createProcessDefinitionQuery().deploymentId(dep.getId()).singleResult();
        
        
        // 启动流程   设置businesskey
        ProcessInstance pi = runService.startProcessInstanceById(pd.getId(), "abc");
        
        
        System.out.println(pi.getId());


	}
	
	
	/**
	 * 流程全局参数 和本地参数
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
	        // 部署
	        Deployment dep = rs.createDeployment().addClasspathResource("test/scope.bpmn").deploy();
	        ProcessDefinition pd = rs.createProcessDefinitionQuery().deploymentId(dep.getId()).singleResult();
	        // 启动流程
	        ProcessInstance pi = runService.startProcessInstanceById(pd.getId());
	        
	        //查询当前流程实例所有任务
	        List<Task> tasks = taskService.createTaskQuery().processInstanceId(pi.getId()).list();
	        for(Task task : tasks) {
	        	//根据任务查询对应的子执行流
	            Execution exe = runService.createExecutionQuery()
	                    .executionId(task.getExecutionId()).singleResult();
	            if("TaskA".equals(task.getName())) {
	            	//本地参数
	                runService.setVariableLocal(exe.getId(), "taskVarA", "varA");
	            } else {
	            	//全局参数
	                runService.setVariable(exe.getId(), "taskVarB", "varB");
	            }
	        }
	        
	        
	        for(Task task : tasks) {
	            taskService.complete(task.getId());
	        }
	        
	        
	        Task taskC = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
	        System.out.println(runService.getVariable(pi.getId(), "taskVarA"));
	        System.out.println(runService.getVariable(pi.getId(), "taskVarB"));
	        
	        
	        System.out.println(pi.getId());


	}
	
	

	@Test
	public  void deployTest5() throws FileNotFoundException {
		 ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        // 任务服务
	        TaskService taskService = engine.getTaskService();
	       
	        taskService.complete("67521");
	       
	        
	}
	
	
	

		


}
