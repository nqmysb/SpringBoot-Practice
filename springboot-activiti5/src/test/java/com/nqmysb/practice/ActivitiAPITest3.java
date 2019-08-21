package com.nqmysb.practice;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试activitiAPI使用-任务
 * 任务候选人（组）  多对多  一个人有多个候选人和组  ACT_RU_IDENTITYLINK  保存了 task deployment 之前的关系 数据权限
 * 任务持有人  一对多  一个人owner多个任务
 * 任务代理人  一对多   一个人assigner多个任务
 * 任务是和实例绑定的
 * ACT_RU_TASK 中 
 * ASSIGNEE_  OWNER_ 
 * complete 完成任务 就会到下一个流程节点
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest3 {
	
	
	/**
	 *  任务候选人  和任务 多对多 addCandidateUser
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest1() throws FileNotFoundException {
		
		 ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        TaskService ts = engine.getTaskService();
	        IdentityService is = engine.getIdentityService();
	        // 创建任务
	        String taskId = UUID.randomUUID().toString();
	        Task task = ts.newTask(taskId);
	        task.setName("测试任务");
	        ts.saveTask(task);
	        //  创建用户
	        String userId = UUID.randomUUID().toString();
	        User user = is.newUser(userId);
	        user.setFirstName("angus");
	        is.saveUser(user);
	        // 设置任务的候选用户组
	        ts.addCandidateUser(taskId, userId);
	        
	        
	        List<Task> tasks = ts.createTaskQuery().taskCandidateUser(userId).list();
	        System.out.println(userId + " 这个用户有权限处理的任务有：");
	        for(Task t : tasks) {
	            System.out.println(t.getName());
	        }
	         //ACT_RU_IDENTITYLINK  保存了 task deployment 之前的关系 数据权限
	        // userid  taskid
	        //27501	1		candidate	20c6f82e-6906-4fb2-bb47-9ea42bb563d2	589b0d28-b8b0-47e7-a5ff-5fdcad6f8d23			

		
	}
	
	
	/**
	 *  任务代理人  和任务 一对多 assigner 
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest2() throws FileNotFoundException {
		
	    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService ts = engine.getTaskService();
        IdentityService is = engine.getIdentityService();
        // 创建任务
        String taskId = UUID.randomUUID().toString();
        Task task = ts.newTask(taskId);
        task.setName("测试任务");
        ts.saveTask(task);
        //  创建用户
        String userId = UUID.randomUUID().toString();
        User user = is.newUser(userId);
        user.setFirstName("angus");
        is.saveUser(user);
        
        ts.claim(taskId, userId);
        //ts.claim(taskId, "100");会报错
        
        List<Task> tasks = ts.createTaskQuery().taskAssignee(userId).list();
        for(Task t : tasks) {
            System.out.println(t.getName());
        }

	}
	
	
	/**
	 *  任务持有人  和任务 一对多 Owner
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest3() throws FileNotFoundException {
		
		 ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        TaskService ts = engine.getTaskService();
	        IdentityService is = engine.getIdentityService();
	        // 创建任务
	        String taskId = UUID.randomUUID().toString();
	        Task task = ts.newTask(taskId);
	        task.setName("测试任务");
	        ts.saveTask(task);
	        //  创建用户
	        String userId = UUID.randomUUID().toString();
	        User user = is.newUser(userId);
	        user.setFirstName("angus");
	        is.saveUser(user);
	        // 设置任务的候选用户组
	        ts.setOwner(taskId, userId);
	        
	        // 根据用户来查询他所持有的任务
	        List<Task> tasks = ts.createTaskQuery().taskOwner(userId).list();
	        for(Task t : tasks) {
	            System.out.println(t.getName());
	        }

		
	}
	
	
	/**
	 *  任务候选人  和任务 多对多 addCandidateUser
	 * @throws FileNotFoundException
	 */
	@Test
	
	public  void deployTest4() throws FileNotFoundException {
		
		 ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        TaskService ts = engine.getTaskService();
	        IdentityService is = engine.getIdentityService();
	        // 创建任务
	        String taskId = UUID.randomUUID().toString();
	        Task task = ts.newTask(taskId);
	        task.setName("测试任务111");
	        ts.saveTask(task);
	        //  创建用户
	        String group = UUID.randomUUID().toString();
	        Group group1 = is.newGroup(group);
	        group1.setName("fdsf111");
	       
	        is.saveGroup(group1);
	        // 设置任务的候选用户组
	        ts.addCandidateGroup(taskId, group);;
	        String userId = UUID.randomUUID().toString();
	        User user = is.newUser(userId);
	        user.setFirstName("sdd");
	        is.saveUser(user);
	        is.createMembership(userId,group);
	      
	        
	        List<Task> tasks = ts.createTaskQuery().taskCandidateUser(userId).list();
	        System.out.println(userId + " 这个用户有权限处理的任务有：");
	        for(Task t : tasks) {
	            System.out.println(t.getName());
	        }
	        
	        /*
	         * f9f6b50c-b5ed-4b04-a908-af57740206d7 这个用户有权限处理的任务有：
				测试任务111
	         */

		
	}
	
	

	


}
