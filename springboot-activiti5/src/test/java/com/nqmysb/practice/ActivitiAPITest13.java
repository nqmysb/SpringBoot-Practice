package com.nqmysb.practice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试activitiAPI使用-任务监听器和流程监听器 
 * 
 * Activit i 提供了任务监昕器，从而允许在任务执行的过程中执行特定的 Java 程序或者表达
式，目前任务监听器只能在 User Task 中使用，为 BPMN 2.0 元素 extensionElements 添加
activiti :tas kListener 元素来定义一个任务监听器 。任务监昕器并不属于 BPMN 规范的内容，属
于 Activiti 对 BPMN 规范扩展的部分。 
 三种方式：
1.在使用 activiti:taskListener 元素配直监昕器时，可以使用 class 属性指定监昕器的 Java 类，
使用这种方式指定监听器， Java 类必须实现 org. acti viti.engine. delegate. TaskListener 接口的 notify
方法
2.使用 expression 指定监昕器
3. 使用 delegateExpression 指定监昕器 动态监听器 动态的第一种方式


监听器触发
create
complete
任务监昕器会在任务的不同事件中被触发，包括任务创建事件 （ create ）、指定任务代理人
事件（ assignment ）和任务完成事件（ complete ） 。 
如果既提供了 create 事件的监听器，也提供了 assignment 事件的监听器，
会先执行后者，任务创建事件（ create ）监昕器会在任务完成创
建的最后才执行，而指定任务代理人，也是属于任务创建的一部分 。


<userTask id=”usertaskl ” name=”User Task" activiti:assignee=” crazyit”>
	<extensionElements> 
	<activiti:taskListener event="create” 
		class=”org.crazyit.activiti.TaskListenerA”>/activiti:taskListener> 
	<activiti:taskListener event=”assignment” 
		class＝”org.crazyit.activiti.TaskListenerB ” >/activiti:taskListener>
	<activiti:taskListener event=”complete” 
		class ＝”org.crazyit.activiti.TaskListenerC ” >/activiti:taskListener>
	</extensionElements> 
</userTask> 

流程监昕器

触发时机
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest13 {
	
	

	@Test
	public  void processTest1() {
       
		
	}
	
	

	

		


}
