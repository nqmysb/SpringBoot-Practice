package com.nqmysb.practice;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
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
 * addBytes 6版本新增 字节部署方式 
 * 
 * 
 * 五种方式的总结：
 * 1.如果需要自己开发一套流程设计的话就使用addBpmnModel这种方法吧。这种方式更加灵活，缺点就是需要了解每一个对象的含义，需要对bpmnMode对象中的各个子对象都有所了解。
 * 2.如果项目中的流程图是固定的但是一些候选组或者人或者名称不是固定的，需要从数据库中查询出来赋值在部署使用addString这种方法，配合velocity等叶面静态化工具一起使用。
 * 3.如果需要用户自己上传文件部署的话，可以使用addInputStream和addZipInputStream这两种方式。
 * 
 * 
 日志表：
1） act_evt_log         表示EVENT，目前只有一张表ACT_EVT_LOG，存储事件处理日志，方便管理员跟踪处理。
通用数据表
2)  act_ge_bytearray    二进制数据表  
1.存放流程图png ,流程文件bpmn 
act_ge_bytearray表中的ID_和流程模型表act_re_model中EDITOR_SOURCE_VALUE_ID_，EDITOR_SOURCE_EXTRA_VALUE_ID_ 对应
2.存放流程二进制（）变量
act_ge_bytearray表中的ID_ 和历史变量表 ACT_HI_VARINST ，运行变量表ACT_RU_VARIABLE中 BYTEARRAY_ID_  对应 当var_type_或者type_为serializable时

二进制变量
3） act_ge_property     属性数据表存储整个流程引擎级别的数据,初始化表结构时，会默认插入三条记录
1	schema.version	5.22.0.0	1
2	schema.history	create(5.22.0.0)	1
3	next.dbid	1102501	442

历史数据库表
4） act_hi_actinst        历史节点表  ：存放所有历史流程节点信息包括：流程定义id,流程实例id,执行流id,节点id,任务id(如果没有任务如网关则为空),节点名称(网关，信息中心审批)
节点类型：exclusiveGateway，userTask，endEvent，startEvent, 任务代理人 assignee（任务的时候才有） ，节点开始时间 ，结束时间，耗时 

5） act_hi_attachment     历史附件表 ： 附件的内容应该存在act_ge_bytearray 的CONTENT_ID_字段

6） act_hi_comment        历史意见表

7） act_hi_detail         历史详情表，提供历史变量的查询   包括所有流程变量  注意和act_hi_varinst历史变量表  区分 前者要更详细

8） act_hi_identitylink 历史流程人员表 
type_： 
participant  参与者  个人userid
starter  启动者 发起者    个人userid
candidate 候选者   groupid不为空


9） act_hi_procinst        历史流程实例表 

10）act_hi_taskinst     历史任务实例表 

11）act_hi_varinst        历史变量表 

组织机构表
12）act_id_group        用户组信息表     
13）act_id_info         用户扩展信息表
14）act_id_membership   用户与用户组对应信息表
15）act_id_user            用户信息表  

用户组可以用部门 角色 等替代 ，可以自己实现，都代表多个用户的集合
可以通过同名视图覆盖activiti本地的表来对接直接的用户，角色，部门
授权任务给（任务候选人candidate）角色和部门时会到act_id_group表中去找 对应的有权向的用户
   
资源库流程规则表
16）act_procdef_info    流程定义信息
17）act_re_deployment   部署信息表
18）act_re_model        流程设计模型部署表 
19）act_re_procdef        流程定义数据表  流程定义id（流程定义key:版本号：自然数）,流程定义名称，流程定义key，部署id(DEPLOYMENT_ID_),部署bpmn名称，部署流程图名称
SUSPENSION_STATE_ 是否挂起 1 激活 2挂起  通过部署id可以到act_re_deployment找到部署信息 部署时间 部署名称  在act_ge_bytearray中找到部署资源信息 
运行时数据库表
20）act_ru_event_subscr 监听表
21）act_ru_execution    运行时流程执行实例表
22）act_ru_identitylink 运行时流程人员表，主要存储任务节点与参与者的相关信息   任务id  任务的候选人userID 任务的候选组groupid  流程实例id 流程定义id
type_： 
participant  参与者  个人userid
starter  启动者 发起者    个人userid
candidate 候选者   个人userid 或者 groupid不为空


23）act_ru_job            运行时定时任务数据表
24）act_ru_task            运行时任务节点表  
ASSIGNEE_任务的代理人，受理人
Task task=taskService.createTaskQuery().singleResult();
//签收
taskService.claim(task.getId(), "billy");
logger.info(taskService.createTaskQuery().singleResult().getAssignee());
//结果：billy
OWNER_任务的持有人，拥有者 一般是为空，只有当任务设置委托人时才会有值，受理人委托其他人操作该TASK的时候，受理人就成了委托人OWNER_，其他人就成了受理人ASSIGNEE_
Task task=taskService.createTaskQuery().singleResult();
//委托
taskService.delegateTask(task.getId(), "cc");
logger.info(taskService.createTaskQuery().singleResult().getOwner());
logger.info(taskService.createTaskQuery().singleResult().getAssignee());
//结果：owner是Billy，assignee是cc
// 设置任务的拥有者
ts.setOwner(taskId, userId);

Candidate 任务候选人  和候选组   act_ru_identitylink 存放任务和候选人和候选组的关系
ts.addCandidateUser(taskId, userId);
ts.addCandidateGroup(taskId, group);

25）act_ru_variable        运行时流程变量数据表 流程实例id,任务id,   var_type_ 变量类型  integer string serializable , name_ 变量名 ,
BYTEARRAY_ID_ 二进制数据id ，long ,double ,text等变量类型的值
--------------------- 
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest1 {
	
	
	/**
	 * 流程部署 zip包 -addZipInputStream
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
	 * 流程部署 -addInputStream
	 *  
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest2() throws FileNotFoundException {
	    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService rs = engine.getRepositoryService();
        
        DeploymentBuilder builder = rs.createDeployment();
        
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("test/gen.bpmn");
        
        builder.addInputStream("数据申请流程", is);
        
        builder.deploy();
        
        //2	1	数据申请流程	1	<BLOB>	0	
        //1				14-AUG-19 10.27.16.827000 PM	

        

	}
	
	/**
	 * 流程部署-addBpmnModel
	 *  
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest3() throws FileNotFoundException {
	    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	    // 存储服务
	    RepositoryService rs = engine.getRepositoryService();
	    
	    DeploymentBuilder builder = rs.createDeployment();
	    builder.addBpmnModel("My Process", createProcessModel());
	    
	    builder.deploy();


	}
	
	
	/**
	 * 流程部署验证-格式问题
	 *  
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest4() throws FileNotFoundException {
	  ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService rs = engine.getRepositoryService();
        
        DeploymentBuilder builder = rs.createDeployment();
        builder.addClasspathResource("test/schema_error.bpmn");
        builder.disableSchemaValidation();
        builder.deploy();
        
        

	}
	
	/**
	 * 流程部署验证-流程问题
	 *  
	 * @throws FileNotFoundException
	 */
	@Test
	public  void deployTest5() throws FileNotFoundException {
		   ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        // 存储服务
	        RepositoryService rs = engine.getRepositoryService();
	        
	        DeploymentBuilder builder = rs.createDeployment();
	        builder.addClasspathResource("test/bpmn_error.bpmn");
	        //junit没有用？ 还是会验证
	        builder.disableBpmnValidation();
	        Deployment dep = builder.deploy();

        

	}
	
    private static BpmnModel createProcessModel() {
        // 创建BPMN模型对象
        BpmnModel model = new BpmnModel();
        // 创建一个流程定义
        org.activiti.bpmn.model.Process process = new org.activiti.bpmn.model.Process();
        model.addProcess(process);
        process.setId("myProcess");
        process.setName("My Process");
        // 开始事件
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        process.addFlowElement(startEvent);
        // 用户任务
        UserTask userTask = new UserTask();
        userTask.setName("User Task");
        userTask.setId("userTask");
        process.addFlowElement(userTask);
        // 结束事件
        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        process.addFlowElement(endEvent);
        // 添加流程顺序
        process.addFlowElement(new SequenceFlow("startEvent", "userTask"));
        process.addFlowElement(new SequenceFlow("userTask", "endEvent"));
        return model;
    }
	
    
    
	/**
	 * 部署查询-流程文件
	 * @throws IOException 
	 */
	@Test
	public  void deployTest6() throws IOException {


		 ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	        // 存储服务
	        RepositoryService rs = engine.getRepositoryService();
	        
	        DeploymentBuilder builder = rs.createDeployment();
	        builder.addClasspathResource("test/my_text.txt");
	        Deployment dep = builder.deploy();
	        // 数据查询
	        InputStream is = rs.getResourceAsStream(dep.getId(), "test/my_text.txt");
	        int count = is.available();
	        byte[] contents = new byte[count];
	        is.read(contents);
	        String result = new String(contents);
	        //输入结果
	        System.out.println(result); //test 1 content

	}
	
	
	/**
	 * 部署查询-流程图片
	 * 部署成功  ：act_ge_bytearray添加两条记录：两个文件bpmn 和流程图文件
	 *  ACT_RE_PROCDEF 流程定义表 一条记录 
	 *  1	myProcess:1:27504	1	http://www.activiti.org/test	My process	myProcess	1	27501	test/gen.bpmn	test/gen.myProcess.png		0	1	1		
	 * ACT_RE_DEPLOYMENT 部署信息
	 * 1	27501				14-AUG-19 11.16.50.698000 PM	
	 * 相同的流程定义key myProcess 不同的版本 
	 * @throws IOException 
	 */
	@Test
	public  void deployTest7() throws IOException {


        // 创建流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 得到流程存储服务对象
        RepositoryService repositoryService = engine.getRepositoryService();
        // 部署一份流程文件与相应的流程图文件
        Deployment dep = repositoryService.createDeployment()
                .addClasspathResource("test/gen.bpmn").deploy();
        // 查询流程定义
        ProcessDefinition def = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dep.getId()).singleResult();
        // 查询资源文件
        InputStream is = repositoryService.getProcessDiagram(def.getId());
        // 将输入流转换为图片对象  
        BufferedImage image = ImageIO.read(is);
        // 保存为图片文件
//        String path = this.getClass().getClassLoader().getResource("/").getPath();
//        System.out.println(path); 路径问题
        File file = new File("E:/workspace/SpringBoot-Practice/springboot-activiti5/springboot-activiti5/src/main/resources/test/result.png");
        if (!file.exists()) file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ImageIO.write(image, "png", fos);
        fos.close();
        is.close();

	}
	
	
	/**
	 * 部署查询-bpmn文件
	 * @throws IOException 
	 */
	@Test
	public  void deployTest8() throws IOException {


		 // 创建流程引擎
	    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	    // 得到流程存储服务对象
	    RepositoryService repositoryService = engine.getRepositoryService();
	    // 部署一份流程文件
	    Deployment dep = repositoryService.createDeployment()
	            .addClasspathResource("test/gen.bpmn").deploy();
	    
	    // 查询流程定义
	    //查询流程定义实体
	    ProcessDefinition def = repositoryService.createProcessDefinitionQuery()
	            .deploymentId(dep.getId()).singleResult();
	    // 查询资源文件
	    InputStream is = repositoryService.getProcessModel(def.getId());
	    // 读取输入流
	    int count = is.available();
	    byte[] contents = new byte[count];
	    is.read(contents);
	    String result = new String(contents);
	    //输入输出结果
	    System.out.println(result);

	}
	
	
	
	/**
	 * 删除部署数据
	 * @throws IOException 
	 */
	@Test
	public  void deployTest9() throws IOException {


		 // 创建流程引擎
	    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	    // 得到流程存储服务对象
	    RepositoryService repositoryService = engine.getRepositoryService();
	    //不级联删除  部署DeploymentId  一定会删除四类数据：流程身份数据  流程定义 流程部署数据 流程资源数据 
	    repositoryService.deleteDeployment("32501");
	    //级联删除 会删除相关流程实例数据  不级联删除  但是有流程实例则会报错
//	    repositoryService.deleteDeployment("30001", true);
//	    repositoryService.deleteDeploymentCascade(arg0);

	}
	
	
   

}
