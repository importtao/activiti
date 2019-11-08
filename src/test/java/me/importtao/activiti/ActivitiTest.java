package me.importtao.activiti;

import me.importtao.BaseTest;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program activiti
 * @description:
 * @author: changhu
 * @create: 2019/08/29 15:33
 */
public class ActivitiTest extends BaseTest {
    @Resource
    ProcessEngine processEngine ;//注入流程引擎

    @Test
    public void test(){

        // 部署流程文件
        DeploymentBuilder builder = processEngine.getRepositoryService().createDeployment();

        Deployment deploy = builder.addClasspathResource("processes/Test.bpmn")
                .name("请假流程")
                .key("leave")
                .deploy();
        DeploymentBuilder builder1 = processEngine.getRepositoryService().createDeployment();
        Deployment deploy1 = builder1.addClasspathResource("processes/EvaluateProgress.bpmn")
                .name("测评流程")
                .key("evaluate")
                .deploy();

        System.out.println("部署完成\n"+deploy.getId());
        System.out.println("部署完成\n"+deploy1.getId());
        System.out.println("----------------");
        // 启动流程

        startProcess();
        queryTask();
    }

    @Test
    public void startProcess(){

        //指定执行我们刚才部署的工作流程
        String processDefiKey="leave";
        //取运行时服务
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //取得流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefiKey);//通过流程定义的key 来执行流程
        System.out.println("流程执行对象的id:"+pi.getId());//流程实例id
        System.out.println("流程定义id:"+pi.getProcessDefinitionId());
        System.out.println("流程实例id:"+pi.getProcessInstanceId());
    }
    //查询任务
    @Test
    public void queryTask(){
        //任务的办理人
        String assignee="teacher";
        //取得任务服务
        TaskService taskService = processEngine.getTaskService();
        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //办理人的任务列表
        List<Task> list = taskQuery.taskAssignee(assignee)//指定办理人
                .list();
        //遍历任务列表
        if(list!=null&&list.size()>0){
            for(Task task:list){
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("任务的id："+task.getId());
                System.out.println("任务的名称："+task.getName());
                compileTask(task.getId());
            }
        }
    }

    //完成任务
    public void compileTask(String taskId){
        //taskId：任务id
        processEngine.getTaskService().complete(taskId);
        System.out.println("当前任务执行完毕");
    }
    //完成任务
    @Test
    public void compileTask(){
        String taskId="62502";
        //taskId：任务id
        processEngine.getTaskService().complete(taskId);
        System.out.println("当前任务执行完毕");
    }

    //查看流程定义
    @Test
    public void queryProcessDefination(){
        String processDefiKey="leave";//流程定义key
        //获取流程定义列表
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery()
                //查询 ，好比where
//		.processDefinitionId(proDefiId) //流程定义id
                // 流程定义id  ： buyBill:2:704   组成 ： proDefikey（流程定义key）+version(版本)+自动生成id
                //.processDefinitionKey(processDefiKey)//流程定义key 由bpmn 的 process 的  id属性决定
//		.processDefinitionName(name)//流程定义名称  由bpmn 的 process 的  name属性决定
//		.processDefinitionVersion(version)//流程定义的版本
                .latestVersion()//最新版本

                //排序
                .orderByProcessDefinitionVersion().desc()//按版本的降序排序

                //结果
//		.count()//统计结果
//		.listPage(arg0, arg1)//分页查询
                .list();


        //遍历结果
        if(list!=null&&list.size()>0){
            for(ProcessDefinition temp:list){
                System.out.println("流程定义的id: "+temp.getId());
                System.out.println("流程定义的key: "+temp.getKey());
                System.out.println("流程定义的版本: "+temp.getVersion());
                System.out.println("流程定义部署的id: "+temp.getDeploymentId());
                System.out.println("流程定义的名称: "+temp.getName());
            }
        }
    }
    //删除流程定义
    @Test
    public void deleteProcessDefi(){
        //通过部署id来删除流程定义
        String deploymentId="20001";
        processEngine.getRepositoryService().deleteDeployment(deploymentId);
    }

    //获取流程实例的状态
    @Test
    public void getProcessInstanceState(){
        String processInstanceId="72501";
        ProcessInstance pi = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();//返回的数据要么是单行，要么是空 ，其他情况报错
        //判断流程实例的状态
        if(pi!=null){
            System.out.println("该流程实例"+processInstanceId+"正在运行...  "+"当前活动的任务:"+pi.getActivityId());
        }else{
            System.out.println("当前的流程实例"+processInstanceId+" 已经结束！");
        }

    }

    //查看历史执行流程实例信息
    @Test
    public void queryHistoryProcInst(){
        List<HistoricProcessInstance> list = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .list();
        if(list!=null&&list.size()>0){
            for(HistoricProcessInstance temp:list){
                System.out.println("历史流程实例id:"+temp.getId());
                System.out.println("历史流程定义的id:"+temp.getProcessDefinitionId());
                System.out.println("历史流程实例开始时间--结束时间:"+temp.getStartTime()+"-->"+temp.getEndTime());
            }
        }
    }

    //模拟流程变量设置
    @Test
    public void  getAndSetProcessVariable(){
        //有两种服务可以设置流程变量
		TaskService taskService = processEngine.getTaskService();
		RuntimeService runtimeService = processEngine.getRuntimeService();

        /**1.通过 runtimeService 来设置流程变量
         * executionId: 执行对象
         * variableName：变量名
         * values：变量值
         */
		/*runtimeService.setVariable(executionId, variableName, values);
		runtimeService.setVariableLocal(executionId, variableName, values);
        *///设置本执行对象的变量 ，该变量的作用域只在当前的execution对象
//		runtimeService.setVariables(executionId, variables);
        //可以设置多个变量  放在 Map<key,value>  Map<String,Object>

        /**2. 通过TaskService来设置流程变量
         * taskId：任务id
         */
//		taskService.setVariable(taskId, variableName, values);
//		taskService.setVariableLocal(taskId, variableName, values);
////		设置本执行对象的变量 ，该变量的作用域只在当前的execution对象
//		taskService.setVariables(taskId, variables); //设置的是Map<key,values>

        /**3. 当流程开始执行的时候，设置变量参数
         * processDefiKey: 流程定义的key
         * variables： 设置多个变量  Map<key,values>
         */
//		processEngine.getRuntimeService()
//		.startProcessInstanceByKey(processDefiKey, variables)

        /**4. 当任务完成时候，可以设置流程变量
         * taskId:任务id
         * variables： 设置多个变量  Map<key,values>
         */
//		processEngine.getTaskService().complete(taskId, variables);


        /** 5. 通过RuntimeService取变量值
         * exxcutionId： 执行对象
         *
         */
//		runtimeService.getVariable(executionId, variableName);//取变量
//		runtimeService.getVariableLocal(executionId, variableName);//取本执行对象的某个变量
//		runtimeService.getVariables(variablesName);//取当前执行对象的所有变量
        /** 6. 通过TaskService取变量值
         * TaskId： 执行对象
         *
         */
//		taskService.getVariable(taskId, variableName);//取变量
//		taskService.getVariableLocal(taskId, variableName);//取本执行对象的某个变量
//		taskService.getVariables(taskId);//取当前执行对象的所有变量
    }










}
