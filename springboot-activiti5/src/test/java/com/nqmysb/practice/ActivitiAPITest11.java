package com.nqmysb.practice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试activitiAPI使用-bpmn规范-任务
 * 
 * 任务类型
 * 	用户任务：由人参与的任务
 * 
 *  服务任务：
 *  
 *  
 *  
 * 分配任务的候选人,代理人
 * 1.直接写死在流程图中
 * activiti:candidateUsers="userA"
 * 2.在任务监听器中设置
 * delegateTask.setAssignee(candidate_ids);
 * delegateTask.addCandidateGroups(groups);
 * delegateTask.addCandidateUsers(users);
 * 3.JUEL表达式动态获取 
 * activiti:candidateUsers="${authService.getCandidateUsers()}"
 * activiti:candidateGroups="${department}"
 * 
 * 
 * 
 * 
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest11 {
	
	
	/**
	 * 
 <userTask id="usertask1" name="Task A">
            <potentialOwner>
                <resourceAssignmentExpression>
                    <formalExpression>user(angus), group(management), boss
                    </formalExpression>
                </resourceAssignmentExpression>
            </potentialOwner>
        </userTask>
	 * 
	 * 通过关键字user 和group来制定id  没有默认是group 的ID
	 * 
	 * 
	 */
	@Test
	public  void processTest1() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService rs = engine.getRepositoryService();
        // 运行时服务
        RuntimeService runService = engine.getRuntimeService();
        IdentityService is = engine.getIdentityService();
        // 任务服务
        TaskService taskService = engine.getTaskService();

        // 创建用户组
        Group bossG = is.newGroup("boss");
        bossG.setName("boss");

        Group mangG = is.newGroup("management");
        mangG.setName("management");
        
        is.saveGroup(bossG);
        is.saveGroup(mangG);
        
        User user = is.newUser("angus");
        user.setFirstName("Angus");
        is.saveUser(user);

        Deployment dep = rs.createDeployment()
                .addClasspathResource("test/candidate.bpmn").deploy();
        ProcessDefinition pd = rs.createProcessDefinitionQuery()
                .deploymentId(dep.getId()).singleResult();
        ProcessInstance pi = runService.startProcessInstanceById(pd.getId());
        
        // 查询各个用户下面有权限看到的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("boss").list();
        System.out.println(tasks.size());
        System.out.println("boss用户组，可以认领的任务：" + tasks.get(0).getName());
        
        taskService.createTaskQuery().taskCandidateGroup("management").list();
        System.out.println("management用户组，可以认领的任务：" + tasks.size() + "---" + tasks.get(0).getName());
        
        taskService.createTaskQuery().taskCandidateUser("angus").list();
        System.out.println("angus用户，可以认领的任务：" + tasks.size() + "---" + tasks.get(0).getName());
        
/*        boss用户组，可以认领的任务：Task A
        management用户组，可以认领的任务：1---Task A
        angus用户，可以认领的任务：1---Task A*/
		
	}
	
	/**
	 * 
	 * 
	 *     <userTask id="usertask1" name="My Task" activiti:candidateUsers="${authService.getCandidateUsers()}"></userTask>
	 *     
	 *     
	 *     
	 * 
	 */
	@Test
	public  void processTest2() {
	     ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        // 存储服务
	        RepositoryService rs = engine.getRepositoryService();
	        // 运行时服务
	        RuntimeService runService = engine.getRuntimeService();
	        IdentityService is = engine.getIdentityService();
	        // 任务服务
	        TaskService taskService = engine.getTaskService();

//	        User userA = is.newUser("userA");
//	        userA.setFirstName("Angus");
//	        is.saveUser(userA);
//	        
//	        User userB = is.newUser("userB");
//	        userB.setFirstName("Angus");
//	        is.saveUser(userB);

	        Deployment dep = rs.createDeployment()
	                .addClasspathResource("test/juel.bpmn").deploy();
	        ProcessDefinition pd = rs.createProcessDefinitionQuery()
	                .deploymentId(dep.getId()).singleResult();
	        
	        Map<String, Object> vars = new HashMap<String, Object>();
	        //设置授权用户组
	        vars.put("authService", new AuthService());

	        ProcessInstance pi = runService.startProcessInstanceById(pd.getId(), vars);
	        
	        // 查询各个用户下面有权限看到的任务
	        
	        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("userA").list();
	        System.out.println("userA 用户，可以认领的任务：" + tasks.size() + "---" + tasks.get(0).getName());
	        
	        tasks = taskService.createTaskQuery().taskCandidateUser("userB").list();
	        System.out.println("userB 用户，可以认领的任务：" + tasks.size() + "---" + tasks.get(0).getName());
	}
	
	
	

	
	
	
	

	

		


}
