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
 * 测试activitiAPI使用-任务多实例  
 * 会签功能
 * 1.一个任务需要多个角色进行审批或者表决，根据这些审批结果来决定流程的走向。实现以上任务，activiti已经提供了支持，可以使用BPMN规范的多实例活动来实现
 * 2.多实例是在一个普通的节点上添加了额外的属性定义 （所以叫做'多实例特性'），这样运行时节点就会执行多次。相当不同的人循环的执行多次相同的任务， 下面的节点都可以成为一个多实例节点：
 * User Task Script Task Java Service Task Web Service Task  Business Rule Task Emil Task
 * Manual Task Receive Task (Embedded) Sub-Process
 * 
 * 3.根据规范的要求，每个上级流程为每个实例创建分支时都要提供如下变量：
nrOfInstances：实例总数

nrOfActiveInstances：当前活动的，比如，还没完成的，实例数量。 对于顺序执行的多实例，值一直为1。

nrOfCompletedInstances：已经完成实例的数目。

可以通过execution.getVariable(x)方法获得这些变量。

另外，每个创建的分支都会有分支级别的本地变量（比如，其他实例不可见， 不会保存到流程实例级别）：

loopCounter：表示特定实例的在循环的索引值。可以使用activiti的elementIndexVariable属性修改loopCounter的变量名。
 *
 * 4.图形标记    如果节点是多实例的，会在节点底部显示三条短线。 三条竖线表示实例会并行执行。 三条横线表示顺序执行。
 * 多实例类型 : parallel  和sequential 并行和顺序执行
 * 
 * 5. xml元素
 * 
   <userTask id="departmentReview" name="业务部门[审核]" activiti:candidateGroups="${department}">
      <extensionElements>
        <activiti:taskListener event="create" class="com.ly.business.controller.myApplication.listener.BussinessDeptApprovalTaskCreateListener"/>
      </extensionElements>
      <multiInstanceLoopCharacteristics isSequential="false" activiti:collection="${departments}" activiti:elementVariable="department"/>
    </userTask>
    
<userTask id="miTasks" name="My Task" activiti:assignee="${assignee}">
  <multiInstanceLoopCharacteristics isSequential="false"
     activiti:collection="${assigneeList}" activiti:elementVariable="assignee" >
    <completionCondition>${nrOfCompletedInstances/nrOfInstances >= 0.6 }</completionCondition>
  </multiInstanceLoopCharacteristics>
</userTask>

在这里例子中，会为assigneeList集合的每个元素创建一个并行的实例。 当60%的任务完成时，其他任务就会删除，流程继续执行。

 * 6.流程实现
6.1流程图为：

properties:

XML：
<userTask id="usertask1" name="会签" activiti:assignee="${assignee}">
      <multiInstanceLoopCharacteristics isSequential="false" activiti:collection="${assigneeList}" activiti:elementVariable="assignee">
        <completionCondition>${nrOfCompletedInstances/nrOfInstances >= 0.6 }</completionCondition>
      </multiInstanceLoopCharacteristics>
</userTask>

6.2发布流程：
Deployment deployment = repositoryService.createDeployment()
 .addClasspathResource("huiqian.bpmn20.xml")
 .deploy();
6.3启动流程：
List<String> assigneeList=new ArrayList<String>(); //分配任务的人员
assigneeList.add("tom");
assigneeList.add("jeck");
assigneeList.add("mary");
Map<String, Object> vars = new HashMap<String, Object>(); //参数
vars.put("assigneeList", assigneeList);

ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("huiqian",vars);

6.4查询任务：
这时查询以上三个人的任务，都会显示有“会签”这个任务
List<Task> tasks = taskService.createTaskQuery().taskAssignee("mary").list();
完成任务：
会签节点设置的：${nrOfCompletedInstances/nrOfInstances >= 0.6 } 所有任务完成超过60%，剩余的任务就会删除，表示会签通过。
上面一共设置了三个任务，当两个完成时，超过60%。流程结束

 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest12 {
	
	

	@Test
	public  void processTest1() {
       
		
	}
	
	

	

		


}
