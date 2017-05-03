//package com.leeo.web.controller;
//
//import java.io.UnsupportedEncodingException;
//import java.math.BigDecimal;
//import java.net.URLDecoder;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.activiti.engine.HistoryService;
//import org.activiti.engine.RepositoryService;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.history.HistoricActivityInstance;
//import org.activiti.engine.history.HistoricProcessInstance;
//import org.activiti.engine.impl.RepositoryServiceImpl;
//import org.activiti.engine.impl.TaskServiceImpl;
//import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
//import org.activiti.engine.impl.persistence.entity.TaskEntity;
//import org.activiti.engine.impl.pvm.PvmTransition;
//import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
//import org.activiti.engine.impl.pvm.process.ActivityImpl;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.activiti.engine.task.TaskQuery;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//import org.hibernate.SQLQuery;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.app.biz.tLoanApplicationCheckHis.entity.TLoanApplicationCheckHisEntity;
//import com.app.biz.tLoanApplicationCheckHis.service.TLoanApplicationCheckHisService;
//import com.app.biz.tLoanAssignTaskHis.entity.TLoanAssignTaskHisEntity;
//import com.app.biz.tLoanCredit.entity.TLoanCreditEntity;
//import com.app.biz.tLoanCreditProduct.entity.TLoanCreditProductEntity;
//import com.app.biz.tLoanCreditPublishResult.entity.TLoanCreditPublishResultEntity;
//import com.app.biz.tLoanCreditResult.entity.TLoanCreditResultEntity;
//import com.app.biz.tLoanDepartEmail.entity.TLoanDepartEmailEntity;
//import com.app.biz.tLoanLendRequest.entity.TLoanLendRequestEntity;
//import com.app.biz.tLoanQualityCheck.entity.TLoanQualityCheckEntity;
//import com.app.biz.tLoanSendEmailHis.entity.TLoanSendEmailHisEntity;
//import com.app.biz.tLoanSystermConfig.entity.TLoanSystermConfigEntity;
//import com.app.biz.tLoanTaskRecord.entity.TLoanTaskRecordEntity;
//import com.app.biz.tLoanTaskRecord.service.TLoanTaskRecordService;
//import com.app.biz.tMdChannel.entity.TMdChannelEntity;
//import com.app.biz.tMdLoanCla.entity.TMdLoanClaEntity;
//import com.app.biz.tMdRegionalRiskLevel.entity.TMdRegionalRiskLevelEntity;
//import com.app.biz.userTaskPool.entity.UserTaskPoolEntity;
//import com.app.biz.userTaskPool.service.UserTaskPoolService;
//import com.app.message.dwr.MessageDwr;
//import com.common.HttpUtils;
//import com.common.StaticUtils;
//import com.framework.core.annotation.SystemLog;
//import com.framework.core.common.hibernate.qbc.CriteriaQuery;
//import com.framework.core.common.model.json.DataGrid;
//import com.framework.core.util.DateUtils;
//import com.framework.core.util.ResourceUtil;
//import com.framework.web.system.pojo.base.TSDepart;
//import com.framework.web.system.pojo.base.TSUser;
//import com.framework.web.system.service.SystemService;
//import com.oasis.monitor.ModelEnum;
//import com.oasis.monitor.MonitorEvent;
//import com.oasis.monitor.MonitorEventPublisher;
//import com.oasis.precreditaudit.ScoreCardProxyInterface;
//import com.oasis.precreditaudit.VincioInterface;
//import com.process.configuration.AbstractServiceImpl;
//import com.process.configuration.EndTaskCmd;
//import com.process.configuration.GetNextTaskCandidateUsersCmd;
//import com.process.configuration.JumpActivityCmd;
//import com.process.configuration.RejectTaskCmd;
//
//@Service("taskUtilsService")
//@Transactional
//public class TaskUtilsServiceImpl extends AbstractServiceImpl implements
//        TaskUtilsServiceI {
//    private static final Logger logger = Logger
//        .getLogger(TaskUtilsServiceImpl.class);
//
//    @Autowired
//    private TaskServiceImpl taskService;
//    @Autowired
//    private RuntimeService runtimeService;
//    @Autowired
//    private HistoryService historyService;
//    @Autowired
//    private RepositoryService repositoryService;
//    @Autowired
//    private UserTaskPoolService userTaskPoolService;
//    @Autowired
//    private SystemService systemService;
//    @Autowired
//    private VincioInterface vincioInterface;
//    @Autowired
//    private TLoanApplicationCheckHisService tLoanApplicationCheckHisService;
//    @Autowired
//    private TLoanTaskRecordService tLoanTaskRecordService;
//    @Autowired
//    private ProcessConfigServiceI processConfigService;
//    @Autowired
//    private MessageDwr messageDwr;
//    @Autowired
//    private RejectTaskCmd rejectTaskCmd;
//    @Autowired
//    private EndTaskCmd endTaskCmd;
//    @Autowired
//    private ScoreCardProxyInterface scoreCardProxy;
//
//    /**
//     * 根据processInstanceId查找用户任务
//     *
//     * @param userName
//     * @param processInstanceId
//     * @return
//     */
//    @Override
//    public Task getTask(String userName, String processInstanceId) {
//        return this.taskService.createTaskQuery()
//            .processInstanceId(processInstanceId).taskCandidateUser(userName)
//            .singleResult();
//    }
//
//    /**
//     * 根据用户名称查找待办任务
//     *
//     * @param userName
//     * @param dataGrid
//     * @return
//     */
//    @Override
//    public DataGrid listToDoTasks(String userName, DataGrid dataGrid) {
//
//        final TaskQuery query = this.taskService.createTaskQuery()
//            .taskAssignee(userName).orderByTaskCreateTime().desc();
//        final List<Task> results = query.listPage((dataGrid.getPage() - 1)
//            * dataGrid.getRows(), dataGrid.getRows());
//        dataGrid.setResults(results);
//        dataGrid.setTotal((int) query.count());
//
//        return dataGrid;
//    }
//
//    /**
//     * 根据用户名称查找待认领任务
//     *
//     * @param userName
//     * @param dataGrid
//     * @return
//     */
//    @Override
//    public DataGrid listToCandidateTasks(String userName, DataGrid dataGrid) {
//
//        final TaskQuery query = this.taskService.createTaskQuery()
//            .taskCandidateUser(userName)  //待认领任务
//            .orderByTaskCreateTime().desc();
//        final List<Task> results = query.listPage((dataGrid.getPage() - 1)
//            * dataGrid.getRows(), dataGrid.getRows());
//        dataGrid.setResults(results);
//        dataGrid.setTotal((int) query.count());
//
//        return dataGrid;
//    }
//
//    /**
//     * 查找下一节点任务候选人
//     *
//     * @param taskId
//     * @return
//     */
//    @Override
//    public List<String> listNextTaskCandidateUsers(String taskId) {
//        final Task task = this.taskService.createTaskQuery().taskId(taskId)
//            .singleResult();
//        final Map<String, List<String>> map = this
//            .executeCommand(new GetNextTaskCandidateUsersCmd(taskId));
//        if (map != null && !map.isEmpty()) {
//            if (map.size() == 1) {
//                return map.get(task.getTaskDefinitionKey());
//            }
//
//        }
//        return null;
//    }
//
//    /**
//     * 提交任务
//     *
//     * @param taskId
//     * @param nextUser
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public String completeTask(String taskId, String message) {
//
//        //删除任务池中此任务记录
//        this.userTaskPoolService.deleteByTaskId(taskId);
//
//        //Map<String,List<String>> map= executeCommand(new GetNextTaskCandidateUsersCmd(taskId));
//        final Task task = this.taskService.createTaskQuery().taskId(taskId)
//            .singleResult();
//
//        final Map<String, Object> variables = this.runtimeService
//            .getVariables(task.getExecutionId());
//
//        final List<String> returnTasks = (List<String>) variables
//            .get("returnTasks");
//        if (CollectionUtils.isNotEmpty(returnTasks)) {
//            //回退任务任务列表如果不为空，则执行当前任务，并在回退任务列表中删除此任务
//            returnTasks.remove(task.getTaskDefinitionKey());
//
//            final int num = (Integer) (variables.get("num") == null ? 0
//                : variables.get("num"));
//            final List<String> firstCheckKeys = (List<String>) variables
//                .get("firstCheckKeys");
//
//            if (CollectionUtils.isNotEmpty(firstCheckKeys)) {
//                //如果初审并行结点不为空
//                for (int i = 0; i < firstCheckKeys.size(); i++) {
//                    if (task.getTaskDefinitionKey().equals(
//                        firstCheckKeys.get(i))) {
//                        final int count = (Integer) variables.get("count") + 1;
//                        variables.put("count", count);
//                        variables.put("returnTasks", returnTasks);
//
//                        if (count < num) {
//                            this.runtimeService.setVariables(
//                                task.getExecutionId(), variables);
//                            //如果不能达到汇聚条件，则结束此并行节点任务
//                            this.endTaskCmd.setEndTask(taskId, message);
//                            return this.executeCommand(this.endTaskCmd);
//                        } else {
//                            variables.put("count", 0);
//                            this.runtimeService.setVariables(
//                                task.getExecutionId(), variables);
//                        }
//                        break;
//                    }
//                }
//            } else {
//                this.runtimeService.setVariables(task.getExecutionId(),
//                    variables);
//            }
//
//            //如果达到汇聚条件或者没有并行节点，则跳转到下一自定义任务
//            if (CollectionUtils.isNotEmpty(returnTasks)) {
//                String returnTaskCode = returnTasks.get(0);
//                final List<String> nextTasks = new ArrayList<String>();
//                nextTasks.add(returnTaskCode);
//
//                //查询此节点审批历史
//                final Map<String, Object> vars = this.runtimeService
//                    .getVariables(task.getProcessInstanceId());
//                String applyId = (String) vars
//                    .get(StaticUtils.PROCESS_BUSINESS_KEY);
//                String hisSql = "SELECT * FROM t_loan_application_check_his t WHERE t.publish_type=0 AND t.lend_request_no=? AND t.activity_code=? ORDER BY t.check_time DESC";
//                final SQLQuery query = this.systemService.getSession()
//                    .createSQLQuery(hisSql);
//                query.setParameter(0, applyId);
//                query.setParameter(1, returnTaskCode);
//                query.addEntity(TLoanApplicationCheckHisEntity.class);
//                final List<TLoanApplicationCheckHisEntity> hisTaskList = query
//                    .list();
//                if (CollectionUtils.isNotEmpty(hisTaskList)) {
//                    final TLoanApplicationCheckHisEntity hisTask = hisTaskList
//                        .get(0);
//                    final String assignee = hisTask.getCheckUser();
//                    //退回给上次审批人员
//                    this.rejectTaskCmd
//                        .setRejectTask(taskId, nextTasks, message);
//                    final String processInstanceId = this
//                        .executeCommand(this.rejectTaskCmd);
//                    final Task rejectTask = this.taskService.createTaskQuery()
//                        .active()
//                        .processInstanceId(task.getProcessInstanceId())
//                        .taskMinPriority(Task.DEFAULT_PRIORITY)
//                        .taskDefinitionKey(returnTaskCode).singleResult();
//                    boolean flag = this.userReceiveTask(assignee);//是否可接收任务
//                    if (flag) {
////                        try {
//                        this.taskService.claim(rejectTask.getId(), assignee);
//
//                        final UserTaskPoolEntity pool = new UserTaskPoolEntity();
//                        pool.setActivityCode(rejectTask.getTaskDefinitionKey());
//                        pool.setActivityName(rejectTask.getName());
//                        pool.setCreateTime(new Date());
//                        pool.setTaskId(rejectTask.getId());
//                        pool.setUserName(assignee);
//                        pool.setTaskType(StaticUtils.TAST_TYPE_RETURN);
//                        pool.setProcInstId(rejectTask.getProcessInstanceId());
//                        pool.setApplyId(applyId);
//                        this.userTaskPoolService.save(pool);
////                        } catch (final Exception e) {
////                            e.printStackTrace();
////                            TaskUtilsServiceImpl.logger.error("taskId: "
////                                    + rejectTask.getId() + "任务退回给上次审批人员失败，失败原因："
////                                    + e.getMessage());
////                        }
//                    }
//
//                    return processInstanceId;
//                } else {
//                    final List<String> nextTask = new ArrayList<String>();
//                    nextTask.add(returnTasks.get(0));
//                    this.rejectTaskCmd.setRejectTask(taskId, nextTask, message);
//                    return this.executeCommand(this.rejectTaskCmd);
//                }
//
//            }
//        }
//
//        //如果回退任务列表为空或者回退任务列表中的任务都完成，则按流程定义进行流转
//        final String processInstanceId = task.getProcessInstanceId();
////        try {
////            this.taskService.addComment(taskId, processInstanceId, message);
//        this.taskService.complete(taskId);
////        } catch (Exception e) {
////            e.printStackTrace();
////            TaskUtilsServiceImpl.logger.error("提交任务失败：",e);
////        }
//
//        return processInstanceId;
//
//    }
//
//    /**
//     * 统计退回到初审并行节点的数量
//     *
//     * @param processKey
//     * @param firstCheckKeys
//     * @return
//     */
//    private static int countFristCheckNoodesNum(List<String> rejectTaskDefKeys,
//            List<String> firstCheckKeys) {
//        int num = 0;
//        if (CollectionUtils.isNotEmpty(rejectTaskDefKeys)) {
//            for (int i = 0; i < rejectTaskDefKeys.size(); i++) {
//                final String activityName = rejectTaskDefKeys.get(i);
//                for (int j = 0; j < firstCheckKeys.size(); j++) {
//                    if (activityName != null
//                        && activityName.equals(firstCheckKeys.get(j))) {
//                        num++;
//                        break;
//                    }
//                }
//
//            }
//        }
//
//        return num;
//    }
//
//    /**
//     * 退回任务
//     *
//     * @param taskId
//     * @param rejectTaskDefKeys
//     *        退回到任务的节点key
//     * @param message
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public String rejectTask(String taskId, List<String> rejectTaskDefKeys,
//            String message) {
//        //删除任务池中此任务记录
//        this.userTaskPoolService.deleteByTaskId(taskId);
//
//        final Task task = this.taskService.createTaskQuery().taskId(taskId)
//            .singleResult();
//        final Map<String, Object> variables = this.runtimeService
//            .getVariables(task.getExecutionId());
//        final List<String> firstCheckKeys = (List<String>) variables
//            .get("firstCheckKeys");
//
//        final List<String> taskKeys = new ArrayList<String>(firstCheckKeys);
//        taskKeys.add(StaticUtils.TASK_PHONE_SURVEY);
//        taskKeys.add(StaticUtils.TASK_SCORE_CARD);
//        taskKeys.add(StaticUtils.TASK_ASSIGNMENT);
//        taskKeys.add(StaticUtils.TASK_LAST_INSTANCE);
//        taskKeys.add(StaticUtils.TASK_STORE);
//        taskKeys.add(StaticUtils.TASK_ANTIFRAUD);
//        final Iterator<String> it = rejectTaskDefKeys.iterator();
//        while (it.hasNext()) {
//            final String key = it.next();
//            if (!taskKeys.contains(key)) {
//                it.remove();
//            }
//        }
//
//        final int num = TaskUtilsServiceImpl.countFristCheckNoodesNum(
//            rejectTaskDefKeys, firstCheckKeys);
//        variables.put("num", num);
//        variables.put("returnTasks", rejectTaskDefKeys);
//        variables.put("count", 0);
//        this.runtimeService.setVariables(task.getExecutionId(), variables);
//
//        final List<String> firstCheckTasks = new ArrayList<String>();
//
//        if (CollectionUtils.isNotEmpty(firstCheckKeys)
//            && CollectionUtils.isNotEmpty(rejectTaskDefKeys)) {
//            //如果初审并行结点不为空
//            for (int i = 0; i < rejectTaskDefKeys.size(); i++) {
//                for (int j = 0; j < firstCheckKeys.size(); j++) {
//                    if (rejectTaskDefKeys.get(i).equals(firstCheckKeys.get(j))) {
//                        firstCheckTasks.add(rejectTaskDefKeys.get(i));
//                        break;
//                    }
//                }
//            }
//            if (firstCheckTasks.isEmpty()) {
//                firstCheckTasks.add(rejectTaskDefKeys.get(0));
//            }
//            this.rejectTaskCmd.setRejectTask(taskId, firstCheckTasks, message);
//            final String processInstanceId = this
//                .executeCommand(this.rejectTaskCmd);
//            //给流程实例的任务指定认领人
//            this.allotAssignee(processInstanceId);
//
//        } else {
//            //如果未选择退回节点，则按流程图往下执行
//            this.taskService.complete(taskId);
//        }
//        return task.getProcessInstanceId();
//
//    }
//
//    /**
//     * 退回门店
//     *
//     * @param taskId
//     * @param rejectTaskDefKeys
//     * @param message
//     * @return
//     */
//    @Override
//    public String returnToStore(String taskId, List<String> rejectTaskDefKeys,
//            String message) {
//
//        //删除任务池中此任务记录
//        this.userTaskPoolService.deleteByTaskId(taskId);
//
//        //跳转至门店
//        final List<String> tasks = new ArrayList<String>();
//        tasks.add(StaticUtils.TASK_STORE);
//        this.rejectTaskCmd.setRejectTask(taskId, tasks, message);
//        final String processInstanceId = this
//            .executeCommand(this.rejectTaskCmd);
//
//        final List<Task> list = this.taskService.createTaskQuery().active()
//            .processInstanceId(processInstanceId)
//            .taskDefinitionKey(StaticUtils.TASK_STORE).list();
//        if (CollectionUtils.isNotEmpty(list)) {
//            final Task task = list.get(0);
//            this.taskService.setAssignee(task.getId(), "system");
//
//            //设置返回时的任务节点
//            final Map<String, Object> variables = this.runtimeService
//                .getVariables(task.getExecutionId());
//            variables.put("returnTasks", rejectTaskDefKeys);
//            this.runtimeService.setVariables(task.getExecutionId(), variables);
//        }
//        return processInstanceId;
//    }
//
//    /**
//     * 再次进件触发流程
//     *
//     * @param lendRequestNo
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public String requestAgain(String lendRequestNo) throws Exception {
//        String processInstanceId = null;
//        final List<Task> list = this.taskService.createTaskQuery().active()
//            .taskAssignee("system").processInstanceBusinessKey(lendRequestNo)
//            .list();
//        if (CollectionUtils.isNotEmpty(list)) {
//            final Task task = list.get(0);
//
//            //更新流程实例变量
//            boolean urgent = false;
//            final TLoanLendRequestEntity personalDetai = this.systemService
//                .findUniqueByProperty(TLoanLendRequestEntity.class, "id",
//                    lendRequestNo);
//            if (null != personalDetai) {
//                final String channelId = personalDetai.getLendChannelId();
//                final String productType = personalDetai
//                    .getAppliedProductType();
//                final String city = personalDetai.getDistrictGroupId() == null ? ""
//                    : personalDetai.getDistrictGroupId();
//                String taskAttribute = personalDetai.getTaskAttribute();
//                taskAttribute = "1".equals(taskAttribute) ? "首次进件" : "再次进件";
//                // 2015-10-09  Eshell Wong Update
////              urgent = personalDetai.getUrgent() == 0 ? false : true;
//                urgent = personalDetai.getUrgent() == 0
//                    || personalDetai.getUrgent() == null ? false : true;
//                // 2015-10-09  Eshell Wong Update End
//                //渠道
//                final TMdChannelEntity channel = this.systemService
//                    .findUniqueByProperty(TMdChannelEntity.class, "orgCode",
//                        channelId);
//                String channelName = "";
//                if (null != channel) {
//                    channelName = channel.getOrgName();
//                }
//                //产品
//                String productId = "";
//                String productName = "";
//                final TMdLoanClaEntity productCla = this.systemService
//                    .findUniqueByProperty(TMdLoanClaEntity.class, "proClaCode",
//                        productType);
//                if (null != productCla) {//产品大类
//                    productId = productCla.getProClaCode();
//                    productName = productCla.getProClaName();
//                }
//                //城市风险等级
//                String cityRiskLevel = "";
//                final CriteriaQuery cq = new CriteriaQuery(
//                    TMdRegionalRiskLevelEntity.class);
//                cq.eq("territoryCode", city);
//                cq.eq("proClaCode", productId);
//                cq.add();
//                final List<TMdRegionalRiskLevelEntity> risks = this.systemService
//                    .getListByCriteriaQuery(cq, false);
//                if (CollectionUtils.isNotEmpty(risks)) {
//                    final TMdRegionalRiskLevelEntity entity = risks.get(0);
//                    cityRiskLevel = entity.getRiskLevel();
//                }
//                final Map<String, Object> vars = this.runtimeService
//                    .getVariables(task.getProcessInstanceId());
//
//                final Map<String, String> params = new HashMap<String, String>(
//                    HttpUtils.RULE_PARAMS);
//                params.put("_processflowname_",
//                    ResourceUtil.getConfigByName("ruleName.startProcess"));
//                params.put("_outputs_", "WorkFlowID");
//                params.put(StaticUtils.PROCESS_CHANNEL_ID, channelId);
//                params.put(StaticUtils.PROCESS_PRODUCT_ID, productId);
//                params.put(StaticUtils.PROCESS_CITY_RISK_LEVEL, cityRiskLevel);
//                params.put(StaticUtils.PROCESS_BUSINESS_KEY, lendRequestNo);
//                params.put(StaticUtils.PROCESS_TASK_PROPERTY,
//                    personalDetai.getTaskAttribute() == null ? ""
//                        : personalDetai.getTaskAttribute());
//                params.put(StaticUtils.PROCESS_REQUEST_USER, personalDetai
//                    .getName() == null ? "" : personalDetai.getName());
//                params.put(StaticUtils.PROCESS_CHANNEL_NAME, channelName);
//                params.put(StaticUtils.PROCESS_PRODUCT_NAME, productName);
//                String result;
//                try{
//                    result = HttpUtils.doPost(HttpUtils.RULE_ENGINE_URI,
//                    params);
//                }catch(Exception e) {
//                    MonitorEventPublisher.getInstance().publish(
//                        new MonitorEvent(ModelEnum.Vincio, e,lendRequestNo,null,"调用规则引擎出错->通讯错误"));
//                    throw e;
//                }
//                result = HttpUtils.resposeAnalyzeRule(result);
//                if (StringUtils.isBlank(result)) {
//                    MonitorEventPublisher.getInstance().publish(
//                        new MonitorEvent(ModelEnum.Vincio, new Exception(
//                                "调用规则引擎出错，请检查规则引擎配置是否正确。"),lendRequestNo,null,"调用规则引擎出错->解析结果为空"));
//                    Exception exception = new Exception(
//                        "调用规则引擎出错，请检查规则引擎配置是否正确。");
//                    throw exception;
//                }
//                final String processDefinitionKey = task
//                    .getProcessDefinitionId().substring(0,
//                        task.getProcessDefinitionId().indexOf(":"));
//                if (!result.equals(processDefinitionKey)) {
//                    this.endProcessInstance(task.getId());
//                    final ProcessInstance processInstance = this.processConfigService
//                        .startProcess(lendRequestNo);
//                    if (null != processInstance) {
//                        final TLoanApplicationCheckHisEntity checkHis = new TLoanApplicationCheckHisEntity();
//                        checkHis.setLendRequestNo(lendRequestNo);
//                        checkHis.setCheckUser("门店");
//                        checkHis.setCheckBeginTime(personalDetai
//                            .getCreateTime());
//                        checkHis.setCheckTime(personalDetai.getCreateTime());
//                        checkHis.setRemarks("由"
//                            + vars.get(StaticUtils.PROCESS_PRODUCT_NAME)
//                            + "产品改为" + productName + "产品");
//                        checkHis
//                            .setPublishType(StaticUtils.APP_CHECK_HIS_CHANGE_PRODUCT);
//                        this.systemService.save(checkHis);
//                        return processInstance.getProcessInstanceId();
//                    }
//                } else {
//                    final List<String> firstCheckKeys = (List<String>) vars
//                        .get("firstCheckKeys");
//                    final List<String> rejectTaskDefKeys = (List<String>) vars
//                        .get("returnTasks");
//
//                    final int num = TaskUtilsServiceImpl
//                        .countFristCheckNoodesNum(rejectTaskDefKeys,
//                            firstCheckKeys);
//                    vars.put("num", num);
//                    vars.put("returnTasks", rejectTaskDefKeys);
//                    vars.put("count", 0);
//                    vars.put(StaticUtils.PROCESS_CHANNEL_ID, channelId);
//                    vars.put(StaticUtils.PROCESS_PRODUCT_ID, productId);
//                    vars.put(StaticUtils.PROCESS_CITY_RISK_LEVEL, cityRiskLevel);
//                    vars.put(StaticUtils.PROCESS_BUSINESS_KEY, lendRequestNo);
//                    vars.put(StaticUtils.PROCESS_REQUEST_USER, personalDetai
//                        .getName() == null ? "" : personalDetai.getName());
//                    vars.put(StaticUtils.PROCESS_CHANNEL_NAME, channelName);
//                    vars.put(StaticUtils.PROCESS_PRODUCT_NAME, productName);
//                    vars.put(StaticUtils.PROCESS_TASK_PROPERTY, taskAttribute);
//                    vars.put(StaticUtils.PROCESS_REQUEST_ID,
//                        personalDetai.getSystemResourceId() == null ? ""
//                            : personalDetai.getSystemResourceId());
//                    vars.put(StaticUtils.PROCESS_URGENT, urgent);
//                    this.runtimeService.setVariables(
//                        task.getProcessInstanceId(), vars);
//
//                    final List<String> firstCheckTasks = new ArrayList<String>();
//                    if (CollectionUtils.isNotEmpty(firstCheckKeys)
//                        && CollectionUtils.isNotEmpty(rejectTaskDefKeys)) {
//                        //如果初审并行结点不为空
//                        for (int i = 0; i < rejectTaskDefKeys.size(); i++) {
//                            for (int j = 0; j < firstCheckKeys.size(); j++) {
//                                if (rejectTaskDefKeys.get(i).equals(
//                                    firstCheckKeys.get(j))) {
//                                    firstCheckTasks.add(rejectTaskDefKeys
//                                        .get(i));
//                                    break;
//                                }
//                            }
//                        }
//                        if (firstCheckTasks.isEmpty()) {
//                            firstCheckTasks.add(rejectTaskDefKeys.get(0));
//                        }
//                        this.rejectTaskCmd.setRejectTask(task.getId(),
//                            firstCheckTasks, "再次进件");
//                        processInstanceId = this
//                            .executeCommand(this.rejectTaskCmd);
//                        //给流程实例的任务指定认领人
//                        this.allotAssignee(processInstanceId);
//                    } else {
//                        //如果未选择退回节点，则按流程图往下执行
//                        this.taskService.complete(task.getId());
//                    }
//                }
//
//            }
//        }
//
//        return processInstanceId;
//    }
//
//    /**
//     * 跳转到指定节点
//     *
//     * @param taskId
//     * @param activityKey
//     * @param message
//     * @return
//     */
//    @Override
//    public String turnToActivity(String taskId, String activityKey,
//            String message) {
//        //删除任务池中此任务记录
//        this.userTaskPoolService.deleteByTaskId(taskId);
//        final Task task = this.taskService.createTaskQuery().taskId(taskId)
//            .singleResult();
//
//        final List<String> firstCheckTasks = new ArrayList<String>();
//        firstCheckTasks.add(activityKey);
//
//        String processInstanceId = "";
//        if (StaticUtils.TASK_ANTIFRAUD.equals(activityKey)) {
//            this.rejectTaskCmd.setRejectTask(taskId, firstCheckTasks, message);
//            processInstanceId = this.executeCommand(this.rejectTaskCmd);
//            //设置驳回时的任务节点
//            List<Task> tasks = this.taskService.createTaskQuery()
//                .processInstanceId(processInstanceId)
//                .taskDefinitionKey(StaticUtils.TASK_ANTIFRAUD)
//                .taskMinPriority(Task.DEFAULT_PRIORITY).orderByTaskCreateTime()
//                .desc().list();
//            if (CollectionUtils.isNotEmpty(tasks)) {
//                final Map<String, Object> variables = this.runtimeService
//                    .getVariables(task.getExecutionId());
//                variables.put("submitTaskCode" + tasks.get(0).getId(),
//                    task.getTaskDefinitionKey());
//                this.runtimeService.setVariables(task.getExecutionId(),
//                    variables);
//            }
//        } else {
//            this.rejectTaskCmd.setRejectTask(taskId, firstCheckTasks, message);
//            processInstanceId = this.executeCommand(this.rejectTaskCmd);
//        }
//        //给流程实例的任务指定认领人
//        this.allotAssignee(processInstanceId);
//
//        return task.getProcessInstanceId();
//
//    }
//
//    /**
//     * 更新待办任务池，如果待办任务不足5条，则向池中添加任务,并取回一个任务
//     *
//     * @param userName
//     */
//    @Override
//    public UserTaskPoolEntity fetchAScheduleTaskPool(String userName) {
//        UserTaskPoolEntity taskPool = null;
//
//        final List<Task> tasks = this.taskService.createTaskQuery()
//            .taskAssignee(userName) // 已领任务
//            .orderByTaskPriority().desc().orderByTaskCreateTime().asc().list();
//        boolean flag = false;
//        if (CollectionUtils.isNotEmpty(tasks)) {
//            UserTaskPoolEntity fristUserTask = null;
//            int taskSize = tasks.size();
//            for (int i = 0; i < taskSize; i++) {
//                final UserTaskPoolEntity userTask = this.userTaskPoolService
//                    .getTaskPoolByTaskId(tasks.get(i).getId());
//                if (null != userTask
//                    && StaticUtils.TAST_TYPE_HANDING.equals(userTask
//                        .getTaskType())) {
//                    taskPool = userTask;
//                    break;
//                } else if (null != userTask
//                    && StaticUtils.TAST_TYPE_PRECAST.equals(userTask
//                        .getTaskType())) {
//                    continue;
//                } else {
//                    if (null == fristUserTask && null != userTask) {
//                        fristUserTask = userTask;
//                    }
//                    if (i == taskSize - 1 && null == taskPool) {
//                        taskPool = fristUserTask;
//                    }
//                    continue;
//                }
//            }
//            if (null == taskPool) {
//                flag = true;
//            }
//        } else {
//            flag = true;
//        }
//        if (flag) {
//            boolean isAllowSusp = this.isAllowGetTask(
//                StaticUtils.TAST_TYPE_SUSPEND, StaticUtils.ALLOW_SUSPEND_COUNT);
//            if (!isAllowSusp) {
//                return taskPool;
//            }
//            final String[] taskType = {StaticUtils.TAST_TYPE_HANDING, StaticUtils.TAST_TYPE_SCHEDULE,StaticUtils.TAST_TYPE_RETURN, StaticUtils.TAST_TYPE_SUSPEND, StaticUtils.TAST_TYPE_PRECAST};
//            int taskCount = this.userTaskPoolService.countTasks(userName, taskType);
//            String sql = "SELECT SUM(s.value) totalCount FROM t_loan_systerm_config s WHERE s.parameter IN ('precastCount','suspendCount')";
//            List<Map<String, Object>> list = this.systemService.findForJdbc(sql);
//            int totalCount = 0;
//            if(CollectionUtils.isNotEmpty(list)){
//            	Double count = (Double) list.get(0).get("totalCount");
//            	totalCount = count.intValue();
//            }
//            if(taskCount >= totalCount){
//            	return taskPool;
//            }
//            final TSUser user = this.systemService.findUniqueByProperty(
//                TSUser.class, "userName", userName);
//            if (null != user) {
//                if (!this.userReceiveTask(user.getUserName())) {//是否可接收任务
//                    return taskPool;
//                }
//                if (StaticUtils.USER_TASK_DISABLE.equals(user.getStatusBit())) {//是否可接单
//                    return taskPool;
//                }
////                final TaskQuery query = this.taskService.createTaskQuery()
////                        .taskCandidateUser(userName).taskUnassigned()
////                        .taskMinPriority(Task.DEFAULT_PRIORITY) // 待认领任务
////                        .orderByTaskPriority().orderByTaskCreateTime().desc();
////                final List<Task> list = query.listPage(0, 1);
////                if (list != null && !list.isEmpty()) {
////                    for (int i = 0; i < list.size(); i++) {
////                        //认领任务
////                        this.taskService.claim(list.get(i).getId(), userName);
////                        taskPool = this
////                            .claimTask(list.get(i).getId(), userName);
////                    }
////                }
//                synchronized (this) {
//                    final Task task = this.queryTask(userName, 1);
//                    if (null != task) {
////                        this.taskService.claim(task.getId(), userName);
//                        taskPool = this.claimTask(task.getId(), userName);
//                    }
//                }
//            }
//
//        }
//
//        return taskPool;
//    }
//
//    /**
//     * 获取任务，人行报告结果未拿到时，不获取电调任务
//     *
//     * @param userName
//     * @param index
//     * @return
//     */
//    public Task queryTask(String userName, int index) {
//        final TaskQuery query = this.taskService.createTaskQuery()
//            .taskCandidateUser(userName).taskUnassigned()
//            .taskMinPriority(Task.DEFAULT_PRIORITY).orderByTaskPriority()
//            .orderByTaskCreateTime().asc(); // 待认领任务
//        final List<Task> list = query.listPage(index - 1, index);
//        if (CollectionUtils.isNotEmpty(list)) {
//            final Task task = list.get(0);/*
//                                           * if
//                                           * (StaticUtils.TASK_PEOPLEBANK_REPORT
//                                           * .equals(task
//                                           * .getTaskDefinitionKey())) {
//                                           * String businessKey = (String)
//                                           * this.runtimeService
//                                           * .getVariables(task
//                                           * .getProcessInstanceId())
//                                           * .get(StaticUtils.PROCESS_BUSINESS_KEY
//                                           * );
//                                           * boolean flag = false;
//                                           * //
//                                           * if(StaticUtils.CUR_ENVIRONMENT_OASIS
//                                           * .
//                                           * equals(ResourceUtil.getConfigByName
//                                           * (
//                                           * "cur_environment").trim().toLowerCase
//                                           * ())){
//                                           * final long count =
//                                           * this.systemService
//                                           * .getCountForJdbc(
//                                           * "SELECT COUNT(1) FROM pbc_credit_report_summary t WHERE t.apply_id='"
//                                           * + businessKey + "'");
//                                           * if (count > 0) {
//                                           * flag = true;
//                                           * } else {
//                                           * String hql =
//                                           * "from TLoanReportSettingsHisEntity s order by s.createTime desc"
//                                           * ;
//                                           * final
//                                           * List<TLoanReportSettingsHisEntity>
//                                           * result =
//                                           * this.systemService.findHql(hql);
//                                           * int isOpenReport = 0;
//                                           * if
//                                           * (CollectionUtils.isNotEmpty(result
//                                           * )) {
//                                           * isOpenReport =
//                                           * result.get(0).getIsOpenReport();
//                                           * }
//                                           * if (1 == isOpenReport) {
//                                           * final long uploadCount =
//                                           * this.systemService
//                                           * .getCountForJdbc(
//                                           * "SELECT COUNT(1) FROM t_loan_lend_upload WHERE person_info IN ('CREDITREPORT_SELF','CREDITREPORT') AND apply_id='"
//                                           * + businessKey + "'");
//                                           * if (uploadCount > 0) {
//                                           * flag = true;
//                                           * }
//                                           * }
//                                           * }
//                                           * // }else{
//                                           * // flag = true;
//                                           * // }
//                                           * if (flag) {
//                                           * return task;
//                                           * } else {
//                                           * this.taskService.claim(task.getId(),
//                                           * "reportNotReturn");
//                                           * return this.queryTask(userName, 1);
//                                           * // return this.queryTask(userName,
//                                           * index + 1);
//                                           * }
//                                           * } else
//                                           */
//            if (StaticUtils.TASK_ASSIGNMENT.equals(task.getTaskDefinitionKey())) {
////                return this.queryTask(userName, index + 1);
//                this.taskService.claim(task.getId(), "assignment");
//                return this.queryTask(userName, 1);
//            } else if (StaticUtils.TASK_REVIEW_POOL.equals(task
//                .getTaskDefinitionKey())) {
//                this.taskService.claim(task.getId(), "reviewPool");
//                return this.queryTask(userName, 1);
//            } else {
//                return task;
//            }
//        }
//
//        return null;
//    }
//
//    /**
//     * 认领任务
//     *
//     * @param taskId
//     * @param userName
//     */
//    @Override
//    public UserTaskPoolEntity claimTask(String taskId, String userName) {
//        this.taskService.claim(taskId, userName);
//        final Task task = this.taskService.createTaskQuery().taskId(taskId)
//            .singleResult();
//        String procInstId = task.getProcessInstanceId();
//        final Map<String, Object> vars = this.runtimeService
//            .getVariables(procInstId);
//        String applyId = (String) vars.get(StaticUtils.PROCESS_BUSINESS_KEY);
//        final UserTaskPoolEntity pool = new UserTaskPoolEntity();
//        pool.setActivityCode(task.getTaskDefinitionKey());
//        pool.setActivityName(task.getName());
//        pool.setCreateTime(new Date());
//        pool.setTaskId(task.getId());
//        pool.setUserName(userName);
//        pool.setProcInstId(procInstId);
//        pool.setApplyId(applyId);
//        final Long hisNum = this.historyService
//            .createHistoricTaskInstanceQuery().taskId(taskId)
//            .processInstanceId(task.getProcessInstanceId())
//            .taskAssignee(userName).finished().count();
//        if (hisNum > 0) {
//            //退件
//            pool.setTaskType(StaticUtils.TAST_TYPE_RETURN);
//        } else {
//            pool.setTaskType(StaticUtils.TAST_TYPE_SCHEDULE);
//        }
//        this.userTaskPoolService.save(pool);
//
//        return pool;
//    }
//
//    /**
//     * 根据当前任务，结束流程实例
//     *
//     * @param taskId
//     */
//    @Override
//    public void endProcessInstance(String taskId) {
//        final Task task = this.taskService.createTaskQuery().taskId(taskId)
//            .singleResult();
//        if (null != task) {
//            String processInstanceId = task.getProcessInstanceId();
//            this.runtimeService.deleteProcessInstance(processInstanceId, "");
//            this.userTaskPoolService
//                .deleteByProcessInstanceId(processInstanceId);
//        }
//    }
//
//    /**
//     * 更新进件状态
//     *
//     * @param execution
//     */
//    @Override
//    public void modifyCaseState(ActivityExecution execution) {
//        final String activityId = execution.getCurrentActivityId();
//        final String applyId = execution.getProcessBusinessKey();
//        String sql = "UPDATE t_loan_lend_request r SET r.activity_code = ? WHERE r.id = ?";
//        this.systemService.executeSql(sql, activityId, applyId);
//    }
//
//    /**
//     * 调用评分卡
//     *
//     * @param execution
//     */
//    @Override
//    @SystemLog
//    public void callScoreCard(ActivityExecution execution) {
//        final String applyId = execution.getProcessBusinessKey();
//        try {
//            String result = null;
//            String sysconfig = this.scoreCardProxy.getSystemConfigByApplyId(applyId);
//            if("1".equals(sysconfig)){
//                result = this.scoreCardProxy.doVincioScoreCardByApplyId(applyId);
//            }else{
//                result = this.vincioInterface.doVincioScoreCardByApplyId(applyId);
//            }
//            TaskUtilsServiceImpl.logger.info(applyId + "--评分卡返回的结果: " + result);
//        } catch (final Exception e) {
//            TaskUtilsServiceImpl.logger.error(applyId + "--调用评分卡失败：", e);
//        }
//    }
//
//    /**
//     * 返回反欺诈提交的节点
//     *
//     * @param task
//     */
//    @Override
//    public void returnSubmitTask(Task task) {
//        final Map<String, Object> variables = this.runtimeService
//            .getVariables(task.getExecutionId());
////        String taskId = (String) variables.get("submitTaskId");
//        final String activityKey = (String) variables.get("submitTaskCode"
//            + task.getId());
//        this.turnToActivity(task.getId(), activityKey, "欺诈驳回");
//    }
//
//    /**
//     * 提交任务
//     *
//     * @param param
//     */
////    @Transactional(rollbackFor = ActivitiException.class)
//    @Override
//    @SystemLog
//    public void submitTask(Map<String,String> param){
//        final String taskId = param.get("taskId");
//        final String requestNo = param.get("requestNo");
//        final String approve = param.get("approve");
//        final String returnTasks = param.get("returnTasks");
//        final String message = param.get("message");
////        final String taskDefKey = param.get("taskDefKey");
////        final String processInstanceId = param.get("processInstanceId");
////        final String requestId = param.get("requestId");
//        final String extMainCode = param.get("extMainCode");
//        final String extMainName = param.get("extMainName");
//        final String extSubCode = param.get("extSubCode");
//        final String extSubName = param.get("extSubName");
//
//        String taskLastDefKey = param.get("taskLastDefKey");
//        final String rejectExpiryDate = param.get("rejectExpiryDate");
////        Date newVersionDate = DateUtils.parseDate("2015-10-17 18:00:00");
//
//        final TSUser user = ResourceUtil.getSessionUserName();
//        final List<String> tasks = new ArrayList<String>();
//        final Task task = this.taskService.createTaskQuery().taskId(taskId)
//            .taskAssignee(user.getUserName())//.taskMinPriority(Task.DEFAULT_PRIORITY)
//            .singleResult();
//        String processId = "";
////        String taskDefinitionKey = "";
//        if (null != task) {
//
//            processId = task.getProcessInstanceId();
////            taskDefinitionKey = task.getTaskDefinitionKey();
////            Date processDate = this.historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getStartTime();
//            String phoneSurveyFlag = (String) this.runtimeService
//                .getVariableLocal(processId, "phoneSurveyFlag");
//            if (StringUtils.isBlank(phoneSurveyFlag)) {
//                this.runtimeService.setVariableLocal(processId,
//                    "phoneSurveyFlag", "true");
//            }
//            if (StringUtils.isNotBlank(returnTasks)) {
//                tasks.addAll(Arrays.asList(returnTasks.split(",")));
//            }
//            this.runtimeService.setVariableLocal(processId, "approve", approve);
//            if (!StaticUtils.APPROVE_SUGGEST_004.equals(approve)) {//不为通过
//                if (StaticUtils.APPROVE_SUGGEST_009.equals(approve)) {//内部退回（从终审退回至指定节点）
////                    if(processDate.after(newVersionDate)){//临时解决方案，兼容新老流程
//                    if (CollectionUtils.isNotEmpty(tasks)) {
//                        if (tasks.contains(StaticUtils.TASK_PHONE_SURVEY)) {
//                            tasks.remove(StaticUtils.TASK_PHONE_SURVEY);
//                            this.runtimeService.setVariableLocal(processId,
//                                "phoneSurveyFlag", "true");
//                        } else {
//                            this.runtimeService.setVariableLocal(processId,
//                                "phoneSurveyFlag", "false");
//                        }
//                    }
////                    }
////                    this.rejectTask(taskId, tasks, "内部退回");
//                    if (CollectionUtils.isNotEmpty(tasks)) {
//                        this.jumpTask(processId, tasks.get(0));
//                    }
//                } else if (StaticUtils.APPROVE_SUGGEST_005.equals(approve)
//                    || StaticUtils.APPROVE_SUGGEST_006.equals(approve)) {//退件至终审、拒绝至终审，到终审
//                    this.runtimeService.setVariableLocal(processId,
//                        "phoneSurveyFlag", "false");
////                    this.sendNotice(processId, requestId, approve, null, null);
////                    this.deleteIncompleteTask(task);
////                    this.turnToActivity(taskId, taskLastDefKey, "退件或拒绝至终审");
//                    this.jumpTask(processId, taskLastDefKey);
//                } else if (StaticUtils.APPROVE_SUGGEST_003.equals(approve)
//                    || StaticUtils.APPROVE_SUGGEST_008.equals(approve)) {//退件发布、实地发布，到门店（可选择回来要走的节点，发消息、邮件）
////                    if(processDate.after(newVersionDate)){//临时解决方案，兼容新老流程
////                        if (CollectionUtils.isNotEmpty(tasks)) {
////                            if (tasks.contains(StaticUtils.TASK_PHONE_SURVEY)) {
////                                tasks.remove(StaticUtils.TASK_PHONE_SURVEY);
////                                this.runtimeService.setVariableLocal(processId, "phoneSurveyFlag", "true");
////                            } else {
////                                this.runtimeService.setVariableLocal(processId, "phoneSurveyFlag", "false");
////                            }
////                        }
////                    }
////                    this.deleteIncompleteTask(task);
////                    //1、退回门店
////                    this.returnToStore(taskId, tasks, "退回门店");
//                    //采用结束流程实例方式，注释原来到门店节点的处理逻辑
////                    this.jumpTask(processId, StaticUtils.TASK_STORE);
////                    final List<Task> list = this.taskService.createTaskQuery().active()
////                            .processInstanceId(processInstanceId)
////                            .taskDefinitionKey(StaticUtils.TASK_STORE).list();
////                    if (CollectionUtils.isNotEmpty(list)) {
////                        final Task ta = list.get(0);
////                        this.taskService.setAssignee(ta.getId(), "system");
////                    }
//                    //1、结束流程
//                    this.endProcessInstance(taskId);
//                    //2、发消息
//                    if (StaticUtils.APPROVE_SUGGEST_003.equals(approve)) {
//                        this.saveSendMsg(requestNo,
//                            StaticUtils.CASE_STATE_RETURN, message,
//                            extMainCode, extMainName, extSubCode, extSubName, rejectExpiryDate);
//                    } else if (StaticUtils.APPROVE_SUGGEST_008.equals(approve)) {
//                        this.saveSendMsg(requestNo,
//                            StaticUtils.CASE_STATE_SPOT, message, extMainCode,
//                            extMainName, extSubCode, extSubName, rejectExpiryDate);
//                    }
//                    //3、发邮件
//                    this.saveSendEmail(requestNo, approve);
//                    //更新进件状态，批核发布、拒绝发布时授信
//                    this.modifyLendRequestState(approve, requestNo);
//                } else if (StaticUtils.APPROVE_SUGGEST_001.equals(approve)
//                    || StaticUtils.APPROVE_SUGGEST_002.equals(approve)) {//拒绝发布、批核发布（结束流程、授信、发消息）追加发邮件
////                    if (StaticUtils.APPROVE_SUGGEST_001.equals(approve)) {
////                        this.sendNotice(processId, requestId, approve, null, null);
////                    }
//                    //1、结束流程
//                    this.endProcessInstance(taskId);
//                    //2、授信
//                    //更新进件状态，批核发布、拒绝发布时授信
//                    this.modifyLendRequestState(approve, requestNo);
//                    //3、发消息
//                    if (StaticUtils.APPROVE_SUGGEST_001.equals(approve)) {
//                        this.saveSendMsg(requestNo,
//                            StaticUtils.CASE_STATE_REJECT, message,
//                            extMainCode, extMainName, extSubCode, extSubName, rejectExpiryDate);
//                    } else if (StaticUtils.APPROVE_SUGGEST_002.equals(approve)) {
//                        this.saveSendMsg(requestNo,
//                            StaticUtils.CASE_STATE_APPROVED, message,
//                            extMainCode, extMainName, extSubCode, extSubName, rejectExpiryDate);
//                    }
//                    //4、发邮件
//                    this.saveSendEmail(requestNo, approve);
//
//                }
//            } else {
////                //完成任务
////                this.completeTask(taskId, "提交任务");
//                this.taskService.complete(taskId);
//                this.userTaskPoolService.deleteByTaskId(taskId);
//            }
//            this.allotAssignee(processId);
//        }
//        //添加审核记录
//        this.addApplicationCheckHis(param);
//        //更新审核结果状态
//        this.updateCreditResult(taskId);
//    }
//
//    /**
//     * 添加审核记录
//     *
//     * @param param
//     */
//    @Override
//    public void addApplicationCheckHis(Map<String, String> param) {
//        final String approveSuggest = param.get("approveSuggest");
//        final String mainReason = param.get("mainReason");
//        final String subReason = param.get("subReason");
//        final String subReasonCode = param.get("subReasonCode");
//        final String mainReasonCode = param.get("mainReasonCode");
//        final String extMainName = param.get("extMainName");
//        final String extSubName = param.get("extSubName");
//
//        final String taskDefinitionKey = param.get("taskDefKey");
//        final String requestNo = param.get("requestNo");
//        final String taskId = param.get("taskId");
//
//        final String processId = param.get("processInstanceId");
//        final String approve = param.get("approve");
//        final String message = param.get("message");
//        String taskName = param.get("taskName");
//        String remarks = param.get("remarks");
//
//        String qcRemarks = "";
//        if (StaticUtils.TASK_QUALITY_CHECK.equals(taskDefinitionKey)) {
//            String hql = "from TLoanApplicationCheckHisEntity t where t.lendRequestNo = ? and t.activityId = ? and t.publishType=2";
//            List<TLoanApplicationCheckHisEntity> hisList = this.systemService
//                .findHql(hql, requestNo, taskId);
//            if (CollectionUtils.isNotEmpty(hisList)) {
//                qcRemarks = hisList.get(0).getRemarks();
//                this.systemService.delete(hisList.get(0));
//            }
//        }
//        try {
//            taskName = URLDecoder.decode(taskName, "UTF-8");
//        } catch (final UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        final TSUser user = ResourceUtil.getSessionUserName();
//        final TLoanApplicationCheckHisEntity checkHis = new TLoanApplicationCheckHisEntity();
//        checkHis.setActivityId(taskId);
//        checkHis.setActivityCode(taskDefinitionKey);
//        checkHis.setActivityName(taskName);
//        checkHis.setProcInstId(processId);
//        checkHis.setLendRequestNo(requestNo);
//        checkHis.setCheckUser(user.getUserName());
//        checkHis.setCheckTime(new Date());
//        checkHis.setApproveCode(approve);
//        checkHis.setMainReasonCode(mainReasonCode);
//        checkHis.setSubReasonCode(subReasonCode);
//        try {
//            checkHis.setApproveSuggest(URLDecoder.decode(approveSuggest,
//                "UTF-8"));
//            if (StringUtils.isNotBlank(mainReason)) {
//                checkHis.setMainReason(URLDecoder.decode(mainReason, "UTF-8"));
//            }
//            if (StringUtils.isNotBlank(subReason)) {
//                checkHis.setSubReason(URLDecoder.decode(subReason, "UTF-8"));
//            }
//            if (StringUtils.isNotBlank(remarks)) {
//                if (StringUtils.isNotBlank(qcRemarks)) {
//                    qcRemarks = qcRemarks + "-";
//                }
//                remarks = URLDecoder.decode(remarks, "UTF-8");
//                checkHis.setRemarks(qcRemarks + remarks);
//            } else {
//                checkHis.setRemarks(qcRemarks);
//            }
//        } catch (final UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        final Date nowDate = new Date();
//        String sql = "SELECT * FROM act_hi_actinst t where t.PROC_INST_ID_=#{processId} and t.TASK_ID_=#{taskId}";
//        final HistoricActivityInstance historicActivityInstance = this.historyService
//            .createNativeHistoricActivityInstanceQuery().sql(sql)
//            .parameter("processId", processId).parameter("taskId", taskId)
//            .singleResult();
//        int duration = 0;
//        if (null != historicActivityInstance) {
//            Date startTime = historicActivityInstance.getStartTime();
//            long durationMS = 0;
//            if (null != historicActivityInstance.getDurationInMillis()) {
//                durationMS = historicActivityInstance.getDurationInMillis();
//            }
//            if (0 != durationMS) {
//                duration = Math.round((float) durationMS / 1000);
//            } else {
//                final long ms = nowDate.getTime() - startTime.getTime();
//                duration = Math.round((float) ms / 1000);
//            }
//            checkHis.setCheckBeginTime(startTime);
//        }
//
//        List<TLoanTaskRecordEntity> list = null;
//        TLoanTaskRecordEntity entity = null;
//        //更新环节结束时间
//        CriteriaQuery cq = new CriteriaQuery(TLoanTaskRecordEntity.class);
//        cq.eq("requestNo", requestNo);
//        cq.eq("taskId", taskId);
//        cq.eq("operateType", StaticUtils.TAST_OPERATETYPE_LINK);
//        cq.add();
//        list = this.tLoanTaskRecordService.getListByCriteriaQuery(cq, false);
//        if (CollectionUtils.isNotEmpty(list)) {
//            entity = list.get(0);
//            entity.setEndTime(nowDate);
//            this.tLoanTaskRecordService.updateEntitie(entity);
//        }
//        //更新作业结束时间
//        cq = new CriteriaQuery(TLoanTaskRecordEntity.class);
//        cq.eq("requestNo", requestNo);
//        cq.eq("taskId", taskId);
//        cq.eq("operateType", StaticUtils.TAST_OPERATETYPE_JOB);
//        cq.add();
//        list = this.tLoanTaskRecordService.getListByCriteriaQuery(cq, false);
//        if (CollectionUtils.isNotEmpty(list)) {
//            entity = list.get(0);
//            entity.setEndTime(nowDate);
//            this.tLoanTaskRecordService.updateEntitie(entity);
//        }
//
//        cq = new CriteriaQuery(TLoanTaskRecordEntity.class);
//        cq.eq("requestNo", requestNo);
//        cq.eq("taskId", taskId);
//        cq.eq("operateType", StaticUtils.TAST_OPERATETYPE_JOB);
//        cq.isNotNull("beginTime");
//        cq.isNotNull("endTime");
//        cq.add();
//        TLoanTaskRecordEntity jobTask = null;
//        list = this.tLoanTaskRecordService.getListByCriteriaQuery(cq, false);
//        if (CollectionUtils.isNotEmpty(list)) {
//            jobTask = list.get(0);
//        }
//        cq = new CriteriaQuery(TLoanTaskRecordEntity.class);
//        cq.eq("requestNo", requestNo);
//        cq.eq("taskId", taskId);
//        cq.eq("operateType", StaticUtils.TAST_OPERATETYPE_SUSPEND);
//        cq.isNotNull("beginTime");
//        cq.isNotNull("endTime");
//        cq.add();
//        final List<TLoanTaskRecordEntity> suspTaskList = this.tLoanTaskRecordService
//            .getListByCriteriaQuery(cq, false);
//        int jobTimeDuration = 0;
//        int realityTimeDuration = 0;
//        //作业时长
//        if (null != jobTask) {
//            final long jobMS = jobTask.getEndTime().getTime()
//                - jobTask.getBeginTime().getTime();
//            jobTimeDuration = Math.round((float) jobMS / 1000);
//        }
//        //挂起时长
//        int suspendDuration = 0;
//        if (CollectionUtils.isNotEmpty(suspTaskList)) {
//            for (final TLoanTaskRecordEntity ent : suspTaskList) {
//                final long suspendMS = ent.getEndTime().getTime()
//                    - ent.getBeginTime().getTime();
//                suspendDuration += Math.round((float) suspendMS / 1000);
//            }
//        }
//        realityTimeDuration = jobTimeDuration - suspendDuration;
//        checkHis.setCheckTimeDuration(duration);
//        checkHis.setJobTimeDuration(jobTimeDuration);
//        checkHis.setRealityTimeDuration(realityTimeDuration);
//        checkHis.setPublishType(StaticUtils.APP_CHECK_HIS_TO_IN);
//        //发布状态的新增对外发布的主子原因记录
//        if (StaticUtils.APPROVE_SUGGEST_001.equals(approve)
//            || StaticUtils.APPROVE_SUGGEST_002.equals(approve)
//            || StaticUtils.APPROVE_SUGGEST_003.equals(approve)
//            || StaticUtils.APPROVE_SUGGEST_008.equals(approve)) {
//            try {
//                final TLoanApplicationCheckHisEntity extCheckHis = (TLoanApplicationCheckHisEntity) checkHis
//                    .clone();
//                if (StringUtils.isNotBlank(extMainName)) {
//                    extCheckHis.setMainReason(URLDecoder.decode(extMainName,
//                        "UTF-8"));
//                } else {
//                    extCheckHis.setMainReason(null);
//                }
//                if (StringUtils.isNotBlank(extSubName)) {
//                    extCheckHis.setSubReason(URLDecoder.decode(extSubName,
//                        "UTF-8"));
//                } else {
//                    extCheckHis.setSubReason(null);
//                }
//                if (StringUtils.isNotBlank(message)) {
//                    extCheckHis.setRemarks(URLDecoder.decode(message, "UTF-8"));
//                } else {
//                    extCheckHis.setRemarks(null);
//                }
//                extCheckHis.setPublishType(StaticUtils.APP_CHECK_HIS_TO_OUT);
//                this.tLoanApplicationCheckHisService.save(extCheckHis);
//            } catch (final CloneNotSupportedException e) {
//                e.printStackTrace();
//            } catch (final UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//        //提交审核结果
//        this.tLoanApplicationCheckHisService.save(checkHis);
//    }
//
//    /**
//     * 更新审核结果状态为生效
//     *
//     * @param taskId
//     */
//    @Override
//    public void updateCreditResult(String taskId) {
//        String sql = "UPDATE t_loan_credit_result r SET r.status = ? WHERE r.task_id = ? and r.status = ?";
//        this.systemService.executeSql(sql, TLoanCreditResultEntity.STATUS_2,
//            taskId, TLoanCreditResultEntity.STATUS_1);
//    }
//
//    /**
//     * 根据流程实例ID跳转至指定的流程节点
//     *
//     * @param processInstanceId
//     * @param activityId
//     */
//    private void jumpTask(String processInstanceId, String activityId) {
//        final TaskServiceImpl taskServiceImpl = this.taskService;
//        taskServiceImpl.getCommandExecutor().execute(
//            new JumpActivityCmd(processInstanceId, activityId));
//        this.userTaskPoolService.deleteByProcessInstanceId(processInstanceId);
//    }
//
//    /**
//     * 删除未完成的任务
//     *
//     * @param task
//     */
//    @Deprecated
//    @SuppressWarnings("unused")
//    private void deleteIncompleteTask(Task task) {
//        final String processInstanceId = task.getProcessInstanceId();
//        final List<Task> tasks = this.taskService.createTaskQuery()
//            .processInstanceId(processInstanceId).active().list();
//        if (CollectionUtils.isNotEmpty(tasks)) {
//            for (final Task ta : tasks) {
//                final String taskId = ta.getId();
//                final String assignee = ta.getAssignee();
//                if (!taskId.equals(task.getId())) {
//                    TaskEntity te = (TaskEntity) ta;
//                    te.setExecutionId(null);
//                    this.taskService.saveTask(te);
//                    this.taskService.deleteTask(taskId, true);
//                    if (StringUtils.isNotBlank(assignee)) {
//                        this.userTaskPoolService.deleteByTaskId(taskId);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 设置加急件
//     *
//     * @param task
//     */
//    @Deprecated
//    @SuppressWarnings("unused")
//    private void setUrgent(Task task) {
//        final boolean urgent = (Boolean) this.runtimeService.getVariableLocal(
//            task.getProcessInstanceId(), StaticUtils.PROCESS_URGENT);
//        final List<Task> taskList = this.taskService.createTaskQuery().active()
//            .processInstanceId(task.getProcessInstanceId()).list();
//        if (CollectionUtils.isNotEmpty(taskList)) {
//            for (final Task t : taskList) {
//                if (urgent) {//加急件
//                    if (StaticUtils.TASK_PRIORITY_60 != t.getPriority()) {
//                        this.taskService.setPriority(t.getId(),
//                            StaticUtils.TASK_PRIORITY_60);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 设置任务的优先级别
//     *
//     * @param task
//     */
//    public void setTaskPriority(Task task) {
//        final boolean urgent = (Boolean) this.runtimeService.getVariables(
//            task.getProcessInstanceId()).get(StaticUtils.PROCESS_URGENT);
//
//        final List<Task> taskList = this.taskService.createTaskQuery().active()
//            .processInstanceId(task.getProcessInstanceId()).list();
//        if (CollectionUtils.isNotEmpty(taskList)) {
//            for (final Task t : taskList) {
//                if (urgent) {//加急件
//                    if (StaticUtils.TASK_PRIORITY_60 != t.getPriority()) {
//                        this.taskService.setPriority(t.getId(),
//                            StaticUtils.TASK_PRIORITY_60);
//                    }
//                } else {
//                    if (Task.DEFAULT_PRIORITY != t.getPriority()) {
//                        this.taskService.setPriority(t.getId(),
//                            Task.DEFAULT_PRIORITY);
//                    }
//                }
//                if (t.getPriority() == 0
//                    && StringUtils.isNotBlank(t.getAssignee())) {
//                    final Map<String, Object> vars = this.runtimeService
//                        .getVariables(t.getProcessInstanceId());
//                    String applyId = (String) vars
//                        .get(StaticUtils.PROCESS_BUSINESS_KEY);
//                    final UserTaskPoolEntity pool = new UserTaskPoolEntity();
//                    pool.setActivityCode(t.getTaskDefinitionKey());
//                    pool.setActivityName(t.getName());
//                    pool.setCreateTime(new Date());
//                    pool.setTaskId(t.getId());
//                    pool.setTaskType(StaticUtils.TAST_TYPE_SCHEDULE);
//                    pool.setUserName(t.getAssignee());
//                    pool.setProcInstId(t.getProcessInstanceId());
//                    pool.setApplyId(applyId);
//                    this.systemService.save(pool);
//                }
//            }
//        }
//    }
//
//    /**
//     * 保存要发送的消息
//     *
//     * @param requestNo
//     * @param type
//     * @param message
//     */
//    @Override
//    public void saveSendMsg(String requestNo, String type, String content,
//            String... param) {
//        final TSUser user = ResourceUtil.getSessionUserName();
//        final TLoanCreditPublishResultEntity entity = new TLoanCreditPublishResultEntity();
//        entity.setApplyId(requestNo);
//        entity.setCreditMainReasonCode(param[0]);
//        entity.setCreditSubReasonCode(param[2]);
//        try {
//            if (StringUtils.isNotBlank(content)) {
//                entity.setCreditContent(URLDecoder.decode(content, "UTF-8"));
//            }
//            if (StringUtils.isNotBlank(param[1])) {
//                entity
//                    .setCreditMainReason(URLDecoder.decode(param[1], "UTF-8"));
//            }
//            if (StringUtils.isNotBlank(param[3])) {
//                entity.setCreditSubReason(URLDecoder.decode(param[3], "UTF-8"));
//            }
//        } catch (final UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        entity.setCreditType(type);
//        entity.setCreditSysUser(user.getUserName());
//        entity.setCreditTime(new Date());
//        if (StringUtils.isNotBlank(param[4]) && !"0".equals(param[4])) {
//            Date rejectExpiryDate = null;
//            if ("-1".equals(param[4])) {
//                rejectExpiryDate = DateUtils.parseDate("2199-12-31");
//            } else {
//            	int unit = Calendar.MONTH;
//            	String expiryDateUnit = param[5];
//            	if("D".equals(expiryDateUnit)){
//            		unit = Calendar.DAY_OF_YEAR;
//            	}else if("Y".equals(expiryDateUnit)){
//            		unit = Calendar.YEAR;
//            	}
//                Calendar calendar = Calendar.getInstance();
//                calendar.add(unit, Integer.valueOf(param[4]));
//                rejectExpiryDate = calendar.getTime();
//            }
//            entity.setRejectExpiryDate(rejectExpiryDate);
//        }
//        this.systemService.save(entity);
//    }
//
//    /**
//     * 查询门店
//     *
//     * @param departCode
//     * @return
//     */
//    @Override
//    public TSDepart queryStoreCode(String departCode) {
//        TSDepart depart = this.systemService.findUniqueByProperty(
//            TSDepart.class, "departCode", departCode);
//        if (null != depart) {
//            if (StaticUtils.DEPART_TYPE_STORE.equals(depart.getDepartType())) {
//                return depart;
//            } else {
//                TSDepart parentDepart = depart.getTSPDepart();
//                if (null != parentDepart
//                    && StringUtils.isNotBlank(parentDepart.getDepartCode())) {
//                    return this.queryStoreCode(parentDepart.getDepartCode());
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 保存邮件
//     *
//     * @param requestNo
//     * @param approve
//     */
//    @Override
//    public void saveSendEmail(String requestNo, String approve) {
//        final TSUser user = ResourceUtil.getSessionUserName();
//        final TLoanLendRequestEntity entity = this.systemService
//            .findUniqueByProperty(TLoanLendRequestEntity.class, "id", requestNo);
//        List<TLoanDepartEmailEntity> result = null;
//        String subject = "";
//        String msg = "";
//
//        if (null != entity) {
//            final TMdLoanClaEntity cla = this.systemService
//                .findUniqueByProperty(TMdLoanClaEntity.class, "proClaCode",
//                    entity.getAppliedProductType());
//            String productName = "";
//            if (null != cla) {
//                productName = cla.getProClaName();
//            }
//            if (StaticUtils.APPROVE_SUGGEST_003.equals(approve)) {//退件发布
//                subject = "【终定提醒】进件号：" + entity.getSystemResourceId()
//                    + " /客户姓名：" + entity.getName() + "/退件";
//                msg = "退件";
//            } else if (StaticUtils.APPROVE_SUGGEST_008.equals(approve)) {//实地发布
//                subject = "【终定提醒】进件号：" + entity.getSystemResourceId()
//                    + " /客户姓名：" + entity.getName() + "/实地";
//                msg = "实地";
//            } else if (StaticUtils.APPROVE_SUGGEST_001.equals(approve)) {//拒绝发布
//                subject = "【终定提醒】进件号：" + entity.getSystemResourceId()
//                    + " /客户姓名：" + entity.getName() + "/拒贷";
//                msg = "拒贷";
//            } else if (StaticUtils.APPROVE_SUGGEST_002.equals(approve)) {//批核发布
//                subject = "【终定提醒】进件号：" + entity.getSystemResourceId()
//                    + " /客户姓名：" + entity.getName() + "/批核/" + productName + "/"
//                    + entity.getApprovalMoneyAmount();
//                msg = "批核/" + productName + "/"
//                    + entity.getApprovalMoneyAmount();
//            } else {
//                subject = "【拦截提醒】进件号：" + entity.getSystemResourceId()
//                    + " /客户姓名：" + entity.getName() + "/拦截";
//                msg = "拦截";
//            }
//            TSDepart tSDepart = this.queryStoreCode(String.valueOf(entity
//                .getGroupId()));
//            result = this.systemService.findByProperty(
//                TLoanDepartEmailEntity.class, "departId", null == tSDepart ? ""
//                    : tSDepart.getDepartCode());
//            if (CollectionUtils.isNotEmpty(result)) {
//                for (final TLoanDepartEmailEntity depart : result) {
//                    final String email = depart.getEmail();
//                    try {
//                        if (StringUtils.isNotBlank(email)) {
////                            SendMailUtil.sendCommonMail(email, subject, msg);
//                            //添加发送历史
//                            TLoanSendEmailHisEntity sendEmailHis = new TLoanSendEmailHisEntity();
//                            sendEmailHis.setApplyId(requestNo);
//                            sendEmailHis.setDepartId(null == tSDepart ? ""
//                                : tSDepart.getDepartCode());
//                            sendEmailHis.setDepartName(null == tSDepart ? ""
//                                : tSDepart.getDepartname());
//                            sendEmailHis.setEmailAddress(email);
//                            sendEmailHis.setEmailSubject(subject);
//                            sendEmailHis.setEmailMessage(msg);
//                            sendEmailHis.setCreateUser(user.getUserName());
//                            sendEmailHis.setCreateTime(new Date());
//                            this.systemService.save(sendEmailHis);
//                        }
//                    } catch (final Exception e) {
//                        TaskUtilsServiceImpl.logger.error("发送邮件失败", e);
//                    }
//
//                }
//            }
//        }
//
//    }
//
//    /**
//     * 挂起任务
//     *
//     * @param taskId
//     * @param applyId
//     */
//    @Override
//    public void suspendTask(String taskId, String applyId) {
//        final TSUser user = ResourceUtil.getSessionUserName();
//        //挂起当前任务
//        this.userTaskPoolService.updateTaskType(taskId,
//            StaticUtils.TAST_TYPE_SUSPEND);
//        //更新任务操作记录
//        final Task task = this.taskService.createTaskQuery().taskId(taskId)
//            .singleResult();
//        if (null != task) {
//            final TLoanTaskRecordEntity entity = new TLoanTaskRecordEntity();
//            entity.setRequestNo(applyId);
//            entity.setOperateUser(user.getUserName());
//            entity.setOperateType(StaticUtils.TAST_OPERATETYPE_SUSPEND);
//            entity.setTaskId(task.getId());
//            entity.setTaskKey(task.getTaskDefinitionKey());
//            entity.setTaskName(task.getName());
//            entity.setBeginTime(new Date());
//            this.systemService.save(entity);
//        }
//    }
//
//    /**
//     * 拦截任务
//     *
//     * @param taskId
//     * @param applyId
//     */
//    @Override
//    public void interceptTask(String taskId, String applyId) {
//        this.saveSendEmail(applyId, taskId);
//    }
//
//    /**
//     * 系统自动批核
//     *
//     * @param param
//     *        approve 批核建议编码 001拒绝发布、002批核发布
//     *        approveSuggest 批核建议
//     *        applyId 进件表ID
//     *        message 对外发布的备注
//     *        extMainCode 对外发布的主原因编码
//     *        extMainName 对外发布的主原因
//     *        extSubCode 对外发布的子原因编码
//     *        extSubName 对外发布的子原因
//     */
//    @Deprecated
//    @Override
//    public void systemApprove(Map<String, String> param) {
//        String approve = param.get("approve");
//        String approveSuggest = param.get("approveSuggest");
//        String applyId = param.get("applyId");
//        String message = param.get("message");
//        String extMainCode = param.get("extMainCode");
//        String extMainName = param.get("extMainName");
//        String extSubCode = param.get("extSubCode");
//        String extSubName = param.get("extSubName");
//
//        final TLoanApplicationCheckHisEntity checkHis = new TLoanApplicationCheckHisEntity();
//        List<Task> tasks = this.taskService.createTaskQuery()
//            .processInstanceBusinessKey(applyId).active().listPage(0, 1);
//        if (CollectionUtils.isNotEmpty(tasks)) {
//            //1、结束流程
//            this.endProcessInstance(tasks.get(0).getId());
//            checkHis.setProcInstId(tasks.get(0).getProcessInstanceId());
//        }
//        //2、更新进件状态，批核发布、拒绝发布时授信
//        this.modifyLendRequestState(approve, applyId);
//        //3、发消息
//        if (StaticUtils.APPROVE_SUGGEST_001.equals(approve)) {
//            this.saveSendMsg(applyId, StaticUtils.CASE_STATE_REJECT, message,
//                extMainCode, extMainName, extSubCode, extSubName);
//        } else if (StaticUtils.APPROVE_SUGGEST_002.equals(approve)) {
//            this.saveSendMsg(applyId, StaticUtils.CASE_STATE_APPROVED, message,
//                extMainCode, extMainName, extSubCode, extSubName);
//        }
//        //4、发邮件
//        this.saveSendEmail(applyId, approve);
//
////        checkHis.setActivityId(taskId);
//        checkHis.setActivityCode(StaticUtils.TASK_LAST_INSTANCE);
//        checkHis.setActivityName("终审");
//        checkHis.setLendRequestNo(applyId);
//        checkHis.setCheckUser("system");
//        checkHis.setCheckTime(new Date());
//        checkHis.setApproveCode(approve);
//        checkHis.setApproveSuggest(approveSuggest);
//        try {
//            if (StringUtils.isNotBlank(extMainName)) {
//                checkHis.setMainReason(URLDecoder.decode(extMainName, "UTF-8"));
//            }
//            if (StringUtils.isNotBlank(extSubName)) {
//                checkHis.setSubReason(URLDecoder.decode(extSubName, "UTF-8"));
//            }
//            if (StringUtils.isNotBlank(message)) {
//                checkHis.setRemarks(URLDecoder.decode(message, "UTF-8"));
//            }
//        } catch (final UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        checkHis.setCheckBeginTime(new Date());
////        checkHis.setCheckTimeDuration(duration);
////        checkHis.setJobTimeDuration(jobTimeDuration);
////        checkHis.setRealityTimeDuration(realityTimeDuration);
//        checkHis.setPublishType(1);
//        //提交审核结果
//        this.tLoanApplicationCheckHisService.saveOrUpdate(checkHis);
//    }
//
//    /**
//     * 欺诈提交
//     *
//     * @param user
//     * @param map
//     */
//    @Deprecated
//    @SuppressWarnings("unused")
//    private void fraudSubmit(TSUser user, Map<String, String> map) {
//        final String taskId = map.get("taskId");
//        final String requestNo = map.get("requestNo");
//        final String remarks = map.get("remarks");
//        TSDepart depart = user.getTSDepart();
//        final TLoanCreditResultEntity entity = new TLoanCreditResultEntity();
//        entity.setApplyId(requestNo);
//        entity.setChannel(null == depart ? null : depart.getDepartname());
//        entity.setTaskId(taskId);
//        try {
//            entity.setTaskName(URLDecoder.decode(map.get("taskName"), "UTF-8"));
//            String str = URLDecoder.decode(map.get("mainReason"), "UTF-8")
//                + "--" + URLDecoder.decode(map.get("subReason"), "UTF-8");
//            if (StringUtils.isNotBlank(remarks)) {
//                str += "--" + URLDecoder.decode(remarks, "UTF-8");
//            }
//            entity.setReason(URLDecoder.decode(str, "UTF-8"));
//            if (StringUtils.isNotBlank(map.get("subReason"))) {
//                entity.setOperation(URLDecoder.decode(
//                    map.get("approveSuggest"), "UTF-8"));
//            }
//        } catch (final UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        entity.setTaskNo(map.get("taskDefKey"));
//        entity.setType(TLoanCreditResultEntity.TYPE_COMPANY_PROCESS);
//        entity.setOprationItem(map.get("approve"));
//        entity.setStatus(TLoanCreditResultEntity.STATUS_2);
//        entity.setResult("-3");
//        entity.setLendRequestNo(map.get("requestId"));
//        entity.setCreateUser(user.getUserName());
//        entity.setCreateTime(new Date());
//        this.systemService.save(entity);
//    }
//
//    /**
//     * 更新进件状态，批核发布、拒绝发布时授信
//     *
//     * @param approve
//     * @param requestNo
//     */
//    private void modifyLendRequestState(String approve, String requestNo) {
//        final TLoanLendRequestEntity entity = this.systemService
//            .findUniqueByProperty(TLoanLendRequestEntity.class, "id", requestNo);
//        entity.setLastUpdateTime(new Date());
//        if (StaticUtils.APPROVE_SUGGEST_002.equals(approve)
//            || StaticUtils.APPROVE_SUGGEST_001.equals(approve)) {//批核发布、拒绝发布
//            if (StaticUtils.APPROVE_SUGGEST_001.equals(approve)) {//拒绝发布
//                entity.setState(StaticUtils.CASE_STATE_REJECT);
//                entity.setApprovalMoneyAmount(BigDecimal.ZERO);
//            } else {//批核发布
//                entity.setState(StaticUtils.CASE_STATE_APPROVED);
//                entity.setApprovalMoneyAmount(entity.getSuggestMoneyAmount());
//            }
//
//            final TLoanSystermConfigEntity config = this.systemService
//                .findUniqueByProperty(TLoanSystermConfigEntity.class,
//                    "parameter", "creditDuration");
//            int creditDuration = Integer.valueOf(config.getValue());
//            final Calendar cal = Calendar.getInstance();
//            cal.setTime(new Date());
//            cal.add(Calendar.DATE, creditDuration);
//
//            final TLoanSystermConfigEntity phasesConfig = this.systemService
//                .findUniqueByProperty(TLoanSystermConfigEntity.class,
//                    "parameter", entity.getAppliedProductType());
//            int loanPhases = 0;
//            if (null != phasesConfig) {
//                loanPhases = Integer.valueOf(phasesConfig.getValue());
//            } else {
//                TLoanSystermConfigEntity defaultProductTermConfig = this.systemService
//                    .findUniqueByProperty(TLoanSystermConfigEntity.class,
//                        "parameter", "defaultProductTerm");
//                if (null != defaultProductTermConfig) {
//                    loanPhases = Integer.valueOf(defaultProductTermConfig
//                        .getValue());
//                }
//            }
//            //临时增加代码  银领贷    20151010：22：00之前24期 之后36期
//            if ("WHITECOLLAR".equals(entity.getAppliedProductType())) {
//                SimpleDateFormat sdf = new SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm:ss");
//                Date date = null;
//                try {
//                    date = sdf.parse("2015-10-10 22:00:00");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    TaskUtilsServiceImpl.logger.error(entity.getId()
//                        + "临时增加代码  银领贷    20151010：22：00之前24期 之后36期 "
//                        + e.getMessage());
//                }
//                if (DateUtils.compare_date(entity.getXsCreateTime(), date) < 0) {
//                    loanPhases = 24;
//                }
//            }
//            BigDecimal floatingFeeRate = null;
//            Integer IsFloatingFeeRate = null;
//            Boolean isPushToQingdaoBank = null;
//            String sql = "SELECT s.is_floating_fee_rate,s.floating_fee_rate,s.loan_phases,s.is_push_to_qingdaobank  FROM (SELECT * FROM vincio_response where apply_id=? AND response_type=? ORDER BY create_time DESC) s GROUP BY apply_id";
//            List<Map<String, Object>> list = this.systemService.findForJdbc(
//                sql, entity.getId(), 2);
//            if (CollectionUtils.isNotEmpty(list)) {
//                floatingFeeRate = (BigDecimal) list.get(0).get(
//                    "floating_fee_rate");
//                IsFloatingFeeRate = (Integer) list.get(0).get(
//                    "is_floating_fee_rate");
//                Integer temp = (Integer) list.get(0).get(
//                        "is_push_to_qingdaobank");
//                if (temp!=null) {
//                    isPushToQingdaoBank = temp==1;
//                }else{
//                    //存量数据英才，银领贷默认规则新增返回字段为：“是”，其他产品为“否”
//                    if ("WHITECOLLAR".equals(entity.getAppliedProductType())
//                            ||"PERSON".equals(entity.getAppliedProductType())){
//                        isPushToQingdaoBank = Boolean.TRUE;
//                    }else{
//                        isPushToQingdaoBank = Boolean.FALSE;
//                    }
//                }
//                //临时代码，如果时间小于2015-12-08 22:00:00 则使用旧规则期数，否则使用规则引擎返回的期数
//                //银领和英才
//                if ("WHITECOLLAR".equals(entity.getAppliedProductType())
//                    || "PERSON".equals(entity.getAppliedProductType())) {
//                    SimpleDateFormat sdf = new SimpleDateFormat(
//                        "yyyy-MM-dd HH:mm:ss");
//                    Date date = null;
//                    try {
//                        date = sdf.parse("2015-12-08 22:00:00");
//                    } catch (Exception e) {
//                        TaskUtilsServiceImpl.logger
//                            .error(
//                                entity.getId()
//                                    + "临时代码，如果时间小于2015-12-08 22:00:00 则使用旧规则期数，否则使用规则引擎返回的期数",
//                                e);
//                    }
//                    if (DateUtils.compare_date(entity.getXsCreateTime(), date) > 0) {
//                        Object objLoanPhases = list.size() == 1
//                            && list.get(0) != null ? list.get(0).get(
//                            "loan_phases") : null;
//                        if (objLoanPhases == null) {
//                            TaskUtilsServiceImpl.logger.error(entity.getId()
//                                + "objLoanPhases == null,规则引擎返回期数值不正确！");
//                        } else {
//                            loanPhases = (Integer) list.get(0).get(
//                                "loan_phases");
//                        }
//                    }
//                }
//
//            }
//            //授信
//            final TLoanCreditEntity credit = new TLoanCreditEntity();
//            credit.setApplyId(requestNo);
//            if (StaticUtils.APPROVE_SUGGEST_001.equals(approve)) {//拒绝发布
//                credit.setCreditAmount(BigDecimal.ZERO);
//            } else {//批核发布
//                credit.setCreditAmount(entity.getSuggestMoneyAmount());
//            }
//            credit.setCreditDate(new Date());
//            credit.setCustomerCode(entity.getCustomerCode());
//            credit.setExpiryDate(cal.getTime());
//            credit.setIdCardNo(entity.getIdNo());
//            credit.setLoanClassification(entity.getAppliedProductType());
//            credit.setIsLoop(0);
//            credit.setIsLowerStage(0);
//            credit.setIsMultiple(0);
//            credit.setLoanPhases(loanPhases);
//            credit.setFloatingFeeRate(floatingFeeRate);
//            credit.setIsFloatingFeeRate(IsFloatingFeeRate);
//            credit.setIsPushToQingdaoBank(isPushToQingdaoBank);
//            this.systemService.save(credit);
//
//            final TLoanCreditProductEntity creditProduct = new TLoanCreditProductEntity();
//            creditProduct.setApplyId(requestNo);
//            creditProduct.setCreditId(credit.getId());
//            creditProduct.setLoanId(entity.getAppliedProductId());
//            this.systemService.save(creditProduct);
//        } else if (StaticUtils.APPROVE_SUGGEST_003.equals(approve)) {//退件发布
//            entity.setState(StaticUtils.CASE_STATE_RETURN);
//        } else if (StaticUtils.APPROVE_SUGGEST_008.equals(approve)) {//实地发布
//            entity.setState(StaticUtils.CASE_STATE_SPOT);
//        }
//        this.systemService.updateEntitie(entity);
//    }
//
//    /**
//     * 添加黑名单
//     *
//     * @param requestNo
//     */
//    @Deprecated
//    @SuppressWarnings("unused")
//    private void addBlackList(String requestNo) {
//        //添加黑名单
//        final TLoanLendRequestEntity reqEntity = this.systemService
//            .findUniqueByProperty(TLoanLendRequestEntity.class, "id", requestNo);
//        String val = "";
//        if (null != reqEntity) {
//            val = "identityNo=" + reqEntity.getIdNo() + "&phone="
//                + reqEntity.getMobile() + "&companyName="
//                + reqEntity.getCompanyName() + "&homeTel="
//                + reqEntity.getHomePhone() + "&workTel="
//                + reqEntity.getCompanyPhone() + "&lendRequestId="
//                + reqEntity.getSystemResourceId() + "&effectTime="
//                + DateUtils.formatDate(new Date(), "yyyy-MM-dd");
//        }
//        final String uri = ResourceUtil.getConfigByName("HermesScheme") + "://"
//            + ResourceUtil.getConfigByName("HermesIP") + ":"
//            + ResourceUtil.getConfigByName("HermesPort")
//            + "/hermes/rest/blackList/addBlackList?" + val;
//        try {
//            final String result = HttpUtils.doGetByBasic(uri,
//                ResourceUtil.getConfigByName("HermesIP"),
//                Integer.parseInt(ResourceUtil.getConfigByName("HermesPort")),
//                ResourceUtil.getConfigByName("HermesBasicUserName"),
//                ResourceUtil.getConfigByName("HermesBasicPassword"));
//            if ("true".equals(result)) {
//                TaskUtilsServiceImpl.logger.info("进件：" + requestNo
//                    + ",添加黑名单成功！" + result);
//            } else {
//                TaskUtilsServiceImpl.logger.info("进件：" + requestNo
//                    + ",添加黑名单失败！" + result);
//            }
//        } catch (final Exception e) {
//            e.printStackTrace();
//            TaskUtilsServiceImpl.logger.error("进件：" + requestNo
//                + ",添加黑名单失败！失败原因：" + e.getMessage());
//        }
//    }
//
//    /**
//     * 给流程实例的任务指定认领人
//     *
//     * @param processInstanceId
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public void allotAssignee(String processInstanceId) {
//        final List<Task> tasks = this.taskService.createTaskQuery().active()
//            .taskUnassigned().processInstanceId(processInstanceId)
//            .taskMinPriority(Task.DEFAULT_PRIORITY).orderByTaskCreateTime()
//            .desc().list();
//        if (CollectionUtils.isNotEmpty(tasks)) {
//            for (Task task : tasks) {
//                String applyId = (String) this.runtimeService.getVariableLocal(
//                    processInstanceId, StaticUtils.PROCESS_BUSINESS_KEY);
//                String hisSql = "SELECT * FROM t_loan_application_check_his t WHERE t.publish_type=0 AND t.lend_request_no=? AND t.activity_code=? ORDER BY t.check_time DESC";
//                final SQLQuery query = this.systemService.getSession()
//                    .createSQLQuery(hisSql);
//                query.setParameter(0, applyId);
//                query.setParameter(1, task.getTaskDefinitionKey());
//                query.addEntity(TLoanApplicationCheckHisEntity.class);
//                final List<TLoanApplicationCheckHisEntity> hisTaskList = query
//                    .list();
//                if (CollectionUtils.isNotEmpty(hisTaskList)) {
//                    final TLoanApplicationCheckHisEntity hisTask = hisTaskList
//                        .get(0);
//                    final String assignee = hisTask.getCheckUser();
//                    boolean flag = this.userReceiveTask(assignee);//是否可接收任务
//                    if (flag) {
//                        String sql = "SELECT COUNT(1) FROM t_loan_process_user_task_pool t WHERE t.task_id='"
//                            + task.getId() + "'";
//                        Long count = this.systemService.getCountForJdbc(sql);
////                        try {
//                        if (count == 0) {
//                            //退回给上次审批人员
//                            this.taskService.claim(task.getId(), assignee);
//                            final UserTaskPoolEntity pool = new UserTaskPoolEntity();
//                            pool.setActivityCode(task.getTaskDefinitionKey());
//                            pool.setActivityName(task.getName());
//                            pool.setCreateTime(new Date());
//                            pool.setTaskId(task.getId());
//                            pool.setUserName(assignee);
//                            pool.setTaskType(StaticUtils.TAST_TYPE_RETURN);
//                            pool.setProcInstId(processInstanceId);
//                            pool.setApplyId(applyId);
//                            this.userTaskPoolService.save(pool);
//                        }
////                        } catch (final Exception e) {
////                            e.printStackTrace();
////                            TaskUtilsServiceImpl.logger.error("taskId: "
////                                    + taskId + "任务退回给上次审批人员失败，失败原因："
////                                    + e.getMessage());
////                        }
//                    }
//
//                }
//            }
//        }
//    }
//
//    /**
//     * 用户是否可接收任务
//     *
//     * @param username
//     * @return
//     */
//    @Override
//    public boolean userReceiveTask(String username) {
//        List<TSUser> users = this.systemService.findByProperty(TSUser.class,
//            "userName", username);
//        if (CollectionUtils.isNotEmpty(users)) {
//            TSUser user = users.get(0);
//            String statusTask = user.getStatusTask();
//            if (StaticUtils.USER_TASK_ENABLE.equals(statusTask)) {
//                return true;
//            }
//        } else {
//            TaskUtilsServiceImpl.logger.info("用户 " + username + " 不存在！");
//        }
//
//        return false;
//    }
//
//    /**
//     * 取回已分派的任务
//     *
//     * @param taskIds
//     */
//    @Override
//    public void taskReclaim(String... taskIds) {
//        if (ArrayUtils.isNotEmpty(taskIds)) {
//            for (final String taskId : taskIds) {
//                this.taskService.unclaim(taskId);
//                String sql = "DELETE FROM t_loan_process_user_task_pool WHERE task_id=?";
//                this.systemService.executeSql(sql, taskId);
//            }
//        }
//    }
//
//    /**
//     * 重新分派任务
//     *
//     * @param taskIds
//     * @param userIds
//     */
//    @Override
//    public void taskReallocate(String[] taskIds, String[] userIds,
//            String... type) {
//        if (ArrayUtils.isNotEmpty(taskIds) && ArrayUtils.isNotEmpty(userIds)) {
//            int count = 0;
//            for (final String taskId : taskIds) {
//                final Task task = this.taskService.createTaskQuery()
//                    .taskId(taskId).active().singleResult();
//                if (null != task) {
//                    if (count >= userIds.length) {
//                        count = 0;
//                    }
//                    final String id = task.getId();
//                    final String user = userIds[count];
//                    this.taskService.setAssignee(id, user);
//                    final String assignee = task.getAssignee();
//                    final String procInstId = task.getProcessInstanceId();
//                    final Map<String, Object> vars = this.runtimeService
//                        .getVariables(procInstId);
//                    String applyId = (String) vars
//                        .get(StaticUtils.PROCESS_BUSINESS_KEY);
//                    String requestNo = (String) vars
//                        .get(StaticUtils.PROCESS_REQUEST_ID);
//                    final String countSql = "SELECT COUNT(1) FROM t_loan_process_user_task_pool t WHERE t.task_id=?";
//                    final Long total = this.systemService.getCountForJdbcParam(
//                        countSql, new Object[] { id });
//                    if (total > 0) {
//                        String sql = "UPDATE t_loan_process_user_task_pool t SET t.user_name=?,t.task_type=? WHERE t.task_id=?";
//                        this.systemService.executeSql(sql, user,
//                            StaticUtils.TAST_TYPE_SCHEDULE, id);
//                    } else {
//                        final UserTaskPoolEntity entity = new UserTaskPoolEntity();
//                        entity.setTaskId(id);
//                        entity.setActivityCode(task.getTaskDefinitionKey());
//                        entity.setActivityName(task.getName());
//                        entity.setUserName(user);
//                        entity.setTaskType(StaticUtils.TAST_TYPE_SCHEDULE);
//                        entity.setCreateTime(new Date());
//                        entity.setProcInstId(procInstId);
//                        entity.setApplyId(applyId);
//                        this.systemService.save(entity);
//                    }
//                    count++;
//                    if (ArrayUtils.isNotEmpty(type)
//                        && StaticUtils.ASSIGN_TYPE_TASK_REALLCATE
//                            .equals(type[0])) {
//                        //记录分派历史
//                        this.addAssignTaskHis(task, user,
//                            StaticUtils.ASSIGN_TYPE_TASK_REALLCATE);
//                        this.sendNotice(procInstId, requestNo,
//                            StaticUtils.ASSIGN_TYPE_TASK_REALLCATE, assignee,
//                            user);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 启动流程并领用任务
//     *
//     * @param processKey
//     * @param users
//     * @param businessKeys
//     */
//    @Override
//    public void startPrcessClaimTask(String processKey, String users,
//            List<String> businessKeys) {
//        String[] userArr = users.split(",");
//        int count = 0;
//        if (CollectionUtils.isNotEmpty(businessKeys)) {
//            for (String businessKey : businessKeys) {
//                ProcessInstance processInstance = null;
//                try {
//                    processInstance = this.processConfigService.startProcess(
//                        processKey, businessKey, null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (null != processInstance && ArrayUtils.isNotEmpty(userArr)) {
//                    Task task = this.taskService.createTaskQuery()
//                        .processInstanceId(processInstance.getId()).active()
//                        .singleResult();
//                    if (null != task) {
//                        if (count >= userArr.length) {
//                            count = 0;
//                        }
//                        String user = userArr[count];
//                        this.taskService.claim(task.getId(), user);
//                        UserTaskPoolEntity entity = new UserTaskPoolEntity();
//                        entity.setTaskId(task.getId());
//                        entity.setActivityCode(task.getTaskDefinitionKey());
//                        entity.setActivityName(task.getName());
//                        entity.setUserName(user);
//                        entity.setTaskType(StaticUtils.TAST_TYPE_SCHEDULE);
//                        entity.setCreateTime(new Date());
//                        entity.setProcInstId(task.getProcessInstanceId());
//                        entity.setApplyId(businessKey);
//                        this.userTaskPoolService.save(entity);
//                        String assignType = "";
//                        if (StaticUtils.PRECESS_NAME_SYSTEM_REJECT
//                            .equals(processKey)) {//系统直拒流程
//                            final TLoanLendRequestEntity tLoanLendRequestEntity = this.systemService
//                                .findUniqueByProperty(
//                                    TLoanLendRequestEntity.class, "id",
//                                    businessKey);
//                            tLoanLendRequestEntity
//                                .setState(StaticUtils.ASSIGN_TYPE_SYSTEM_REJECT);
//                            this.systemService
//                                .updateEntitie(tLoanLendRequestEntity);
//                            assignType = StaticUtils.ASSIGN_TYPE_SYSTEM_REJECT;
//                        } else {//质检流程
//                            TLoanQualityCheckEntity check = new TLoanQualityCheckEntity();
//                            check.setApplyId(businessKey);
//                            check.setAssignee(user);
//                            check.setCreateUser(ResourceUtil
//                                .getSessionUserName().getUserName());
//                            check.setCreateTime(new Date());
//                            this.systemService.save(check);
//                            assignType = StaticUtils.ASSIGN_TYPE_QUALITY_CHECK;
//                        }
//                        count++;
//                        //记录分派历史
//                        this.addAssignTaskHis(task, user, assignType);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 更新任务状态
//     *
//     * @param task
//     */
//    @Override
//    public void updateUserTaskStatus(Task task) {
//        final TSUser user = ResourceUtil.getSessionUserName();
//        final UserTaskPoolEntity userTaskPool = this.systemService
//            .findUniqueByProperty(UserTaskPoolEntity.class, "taskId",
//                task.getId());
//        if (null != userTaskPool
//            && !StaticUtils.TAST_TYPE_HANDING
//                .equals(userTaskPool.getTaskType())) {
//            String hql = "from UserTaskPoolEntity u where u.userName=? and u.taskType!='4'";
//            final List<UserTaskPoolEntity> tasks = this.systemService.findHql(
//                hql, user.getUserName());
////                    .findByProperty(UserTaskPoolEntity.class, "userName",user.getUserName());
//            if (CollectionUtils.isNotEmpty(tasks)) {
//                for (final UserTaskPoolEntity userTask : tasks) {
//                    if (userTask.getTaskId().equals(task.getId())) {
//                        userTask.setTaskType(StaticUtils.TAST_TYPE_HANDING);
//                        this.systemService.updateEntitie(userTask);
//                    } else {
//                        if (StaticUtils.TAST_TYPE_HANDING.equals(userTask
//                            .getTaskType())) {
//                            userTask.setTaskType(StaticUtils.TAST_TYPE_SUSPEND);
//                            this.systemService.updateEntitie(userTask);
//                            this.suspendTask(userTask.getTaskId(),
//                                userTask.getApplyId());
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 添加任务记录信息
//     *
//     * @param taskId
//     * @param requestNo
//     * @param task
//     */
//    @Override
//    public void addTaskRecord(String requestNo, Task task) {
//        final TSUser user = ResourceUtil.getSessionUserName();
//        final Date nowDate = new Date();
//        TLoanTaskRecordEntity entity = null;
//        List<TLoanTaskRecordEntity> list = null;
//        CriteriaQuery cq = new CriteriaQuery(TLoanTaskRecordEntity.class);
//        cq.eq("requestNo", requestNo);
//        cq.eq("taskId", task.getId());
//        cq.eq("operateType", StaticUtils.TAST_OPERATETYPE_SUSPEND);
//        cq.isNotNull("beginTime");
//        cq.isNull("endTime");
//        cq.add();
//        list = this.tLoanTaskRecordService.getListByCriteriaQuery(cq, false);
//        if (CollectionUtils.isNotEmpty(list)) {
//            entity = list.get(0);
//            entity.setEndTime(nowDate);
//            //更新任务挂起结束时间
//            this.tLoanTaskRecordService.updateEntitie(entity);
//        } else {
//            cq = new CriteriaQuery(TLoanTaskRecordEntity.class);
//            cq.eq("requestNo", requestNo);
//            cq.eq("taskId", task.getId());
//            cq.eq("operateType", StaticUtils.TAST_OPERATETYPE_JOB);
//            cq.isNotNull("beginTime");
//            cq.isNull("endTime");
//            cq.add();
//            list = this.tLoanTaskRecordService
//                .getListByCriteriaQuery(cq, false);
//            if (CollectionUtils.isEmpty(list)) {
//                entity = new TLoanTaskRecordEntity();
//                entity.setRequestNo(requestNo);
//                entity.setOperateUser(user.getUserName());
//                entity.setOperateType(StaticUtils.TAST_OPERATETYPE_JOB);
//                entity.setTaskId(task.getId());
//                entity.setTaskKey(task.getTaskDefinitionKey());
//                entity.setTaskName(task.getName());
//                entity.setBeginTime(nowDate);
//                this.systemService.save(entity);
//            }
//
//            cq = new CriteriaQuery(TLoanTaskRecordEntity.class);
//            cq.eq("requestNo", requestNo);
//            cq.eq("taskId", task.getId());
//            cq.eq("operateType", StaticUtils.TAST_OPERATETYPE_LINK);
//            cq.isNotNull("beginTime");
//            cq.isNull("endTime");
//            cq.add();
//            list = this.tLoanTaskRecordService
//                .getListByCriteriaQuery(cq, false);
//            if (CollectionUtils.isEmpty(list)) {
//                entity = new TLoanTaskRecordEntity();
//                entity.setRequestNo(requestNo);
//                entity.setOperateUser(user.getUserName());
//                entity.setTaskId(task.getId());
//                entity.setTaskKey(task.getTaskDefinitionKey());
//                entity.setTaskName(task.getName());
//                entity.setOperateType(StaticUtils.TAST_OPERATETYPE_LINK);
//                entity.setBeginTime(task.getCreateTime());
//                this.systemService.save(entity);
//            }
//        }
//    }
//
//    /**
//     * 获取初审节点的key
//     *
//     * @param processDefinitionId
//     * @return
//     */
//    @Override
//    public List<String> getFirstCheckKeys(String processDefinitionId) {
////        final ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService)
////                .getDeployedProcessDefinition(processDefinitionId);
////        List<ActivityImpl> activitiList = null;
////        List<String> result = null;
////        if (null != def) {
////            activitiList = def.getActivities();
////            result = new ArrayList<String>();
////            for (final ActivityImpl act : activitiList) {
////                final String type = String.valueOf(act.getProperty("type"));
////                final String id = act.getId();
////                if ("userTask".equals(type)
////                        && (StaticUtils.TASK_INFORMATION_FILTER.equals(id)
////                        || StaticUtils.TASK_INPUT_SOCIALSECURITY.equals(id)
////                        || StaticUtils.TASK_INPUT_WATERBANK.equals(id)
////                        || StaticUtils.TASK_PEOPLEBANK_REPORT.equals(id)
////                        || StaticUtils.TASK_RELEVANCE_CHECK.equals(id))) {
////                    result.add(id);
////                }
////            }
////        }
//
//        List<String> result = null;
//        if (StringUtils.isNotBlank(processDefinitionId)) {
//            final ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) this.repositoryService
//                .getProcessDefinition(processDefinitionId);
//            final ActivityImpl activity = processDefinition
//                .findActivity("parallelGateway1430885124196");
//            if (null != activity) {
//                final List<PvmTransition> transitions = activity
//                    .getIncomingTransitions();
//                if (CollectionUtils.isNotEmpty(transitions)) {
//                    result = new ArrayList<String>();
//                    for (final PvmTransition trans : transitions) {
//                        final String type = (String) trans.getSource()
//                            .getProperty("type");
//                        if ("userTask".equals(type)) {
//                            final String taskCode = trans.getSource().getId();
//                            result.add(taskCode);
//                        }
//                    }
//                }
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 添加任务分派历史
//     *
//     * @param task
//     * @param userName
//     * @param assignType
//     */
//    @Override
//    public void addAssignTaskHis(Task task, String userName, String assignType) {
//        final Map<String, Object> vars = this.runtimeService.getVariables(task
//            .getExecutionId());
//        final TSUser user = ResourceUtil.getSessionUserName();
//        TLoanAssignTaskHisEntity assignTaskHis = new TLoanAssignTaskHisEntity();
//        assignTaskHis.setApplyId(String.valueOf(vars
//            .get(StaticUtils.PROCESS_BUSINESS_KEY)));
//        assignTaskHis.setRequestNo(String.valueOf(vars
//            .get(StaticUtils.PROCESS_REQUEST_ID)));
//        assignTaskHis.setActivityCode(task.getTaskDefinitionKey());
//        assignTaskHis.setTaskId(task.getId());
//        assignTaskHis.setTaskName(task.getName());
//        assignTaskHis.setOldUser(task.getAssignee());
//        assignTaskHis.setNewUser(userName);
//        assignTaskHis.setCreateUser(user.getUserName());
//        assignTaskHis.setCreateTime(new Date());
//        assignTaskHis.setAssignType(assignType);
//        this.systemService.save(assignTaskHis);
//    }
//
//    /**
//     * 是否在初审环节
//     *
//     * @param applyId
//     * @return
//     */
//    @Override
//    public boolean isFirstCheck(String applyId) {
//        boolean flag = false;
//        final List<Task> tasks = this.taskService.createTaskQuery()
//            .processInstanceBusinessKey(applyId).active().list();
//        if (CollectionUtils.isNotEmpty(tasks)) {
//            for (final Task task : tasks) {
//                final String taskCode = task.getTaskDefinitionKey();
//                if (StaticUtils.TASK_INFORMATION_FILTER.equals(taskCode)
//                    || StaticUtils.TASK_INPUT_SOCIALSECURITY.equals(taskCode)
//                    || StaticUtils.TASK_INPUT_WATERBANK.equals(taskCode)
//                    || StaticUtils.TASK_PEOPLEBANK_REPORT.equals(taskCode)
//                    || StaticUtils.TASK_RELEVANCE_CHECK.equals(taskCode)
//                    || StaticUtils.TASK_CAR_INSURANCE_INFO.equals(taskCode)
//                    || StaticUtils.TASK_LIFE_INSURANCE_INFO.equals(taskCode)
//                    || StaticUtils.TASK_MORTGAGE_INFO.equals(taskCode)) {
//                    flag = true;
//                    break;
//                }
//            }
//        }
//
//        return flag;
//    }
//
//    /**
//     * 发送任务通知
//     *
//     * @param processInstanceId
//     * @param requestNo
//     * @param type
//     * @param oldUser
//     * @param newUser
//     */
//    private void sendNotice(String processInstanceId, String requestNo,
//            String type, String oldUser, String newUser) {
//        final TSUser user = ResourceUtil.getSessionUserName();
//        List<String> users = null;
//        String msg = "";
//        if (StaticUtils.ASSIGN_TYPE_TASK_REALLCATE.equals(type)) {
//            if (StringUtils.isNotBlank(oldUser)) {
//                users = new ArrayList<String>();
//                users.add(oldUser);
//                TSUser u = this.systemService.findUniqueByProperty(
//                    TSUser.class, "userName", newUser);
//                msg = "您任务列表中的进件（" + requestNo + "），已被\"" + user.getRealName()
//                    + "\"将此任务分派给\"" + u.getRealName() + "\"，请知晓。";
//                this.messageDwr.pushMessage(msg, users);
//            }
//        } else {
////            List<UserTaskPoolEntity> list = this.systemService.findByProperty(
////                UserTaskPoolEntity.class, "procInstId", processInstanceId);
//            List<Task> list = this.taskService.createTaskQuery()
//                .processInstanceId(processInstanceId).active().list();
//            if (CollectionUtils.isNotEmpty(list)) {
//                users = new ArrayList<String>();
//                for (Task entity : list) {
//                    String name = entity.getAssignee();
//                    if (null != name && !users.contains(name)
//                        && !name.equals(user.getUserName())) {
//                        users.add(name);
//                    }
//                }
//            }
//
//            if (CollectionUtils.isNotEmpty(users)) {
//                if (StaticUtils.APPROVE_SUGGEST_001.equals(type)) {
//                    msg = "您任务列表中的进件（" + requestNo + "），已被\""
//                        + user.getRealName() + "\"拒绝发布，请知晓。";
//                } else if (StaticUtils.APPROVE_SUGGEST_007.equals(type)) {
//                    msg = "您任务列表中的进件（" + requestNo + "），已被\""
//                        + user.getRealName() + "\"欺诈提交，请知晓。";
//                } else {
//                    msg = "您任务列表中的进件（" + requestNo + "），已被\""
//                        + user.getRealName() + "\"退件至终审或拒绝至终审，请知晓。";
//                }
//
//                this.messageDwr.pushMessage(msg, users);
//            }
//        }
//    }
//
//    /**
//     * 完成任务分派任务
//     *
//     * @param taskId
//     */
//    @Override
//    public void completeTaskAssignment(String[] taskIds, String[] userIds,
//            String type) {
//        if (ArrayUtils.isNotEmpty(taskIds) && ArrayUtils.isNotEmpty(userIds)) {
//            final TSUser user = ResourceUtil.getSessionUserName();
//            int count = taskIds.length;
//            String[] tasks = new String[count];
//            for (int i = 0; i < count; i++) {
//                String taskId = taskIds[i];
//                final Task task = this.taskService.createTaskQuery()
//                    .taskId(taskId).active().singleResult();
//                this.taskService.setAssignee(task.getId(), user.getUserName());
//                this.taskService.complete(task.getId());
//                tasks[i] = this.taskService.createTaskQuery()
//                    .processInstanceId(task.getProcessInstanceId()).active()
//                    .singleResult().getId();
//            }
//            this.taskReallocate(tasks, userIds, type);
//        }
//    }
//
//    /**
//     * 查询流程是否有评分卡节点，若有则返回taskScoreCard（评分卡），否则返回taskLastInstance（终审）
//     *
//     * @param processDefinitionId
//     * @return
//     */
//    @Deprecated
//    @Override
//    public String queryTaskScoreCard(String processDefinitionId) {
//        final ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService)
//            .getDeployedProcessDefinition(processDefinitionId);
//        List<ActivityImpl> activitiList = null;
//        String result = "";
//        if (null != def) {
//            activitiList = def.getActivities();
//            if (CollectionUtils.isNotEmpty(activitiList)) {
//                for (final ActivityImpl act : activitiList) {
//                    final String type = String.valueOf(act.getProperty("type"));
//                    final String id = act.getId();
//                    if ("serviceTask".equals(type)
//                        && StaticUtils.TASK_SCORE_CARD.equals(id)) {
//                        result = id;
//                        break;
//                    }
//                }
//            }
//        }
//        if (StringUtils.isBlank(result)) {
//            result = StaticUtils.TASK_LAST_INSTANCE;
//        }
//
//        return result;
//    }
//
//    @Override
//    public void logoutSuspendTask() {
//        final TSUser user = ResourceUtil.getSessionUserName();
//        final String taskType = "'" + StaticUtils.TAST_TYPE_HANDING + "','"
//            + StaticUtils.TAST_TYPE_SCHEDULE + "','"
//            + StaticUtils.TAST_TYPE_RETURN + "'";
//        final List<UserTaskPoolEntity> userTaskList = this.userTaskPoolService
//            .findByTaskType(user.getUserName(), taskType);
//        if (CollectionUtils.isNotEmpty(userTaskList)) {
//            for (final UserTaskPoolEntity userTaskPool : userTaskList) {
//                String taskId = userTaskPool.getTaskId();
//                String applyId = userTaskPool.getApplyId();
//                this.suspendTask(taskId, applyId);
//            }
//        }
//    }
//
//    /**
//     * 已返回人行报告
//     *
//     * @param applyId
//     */
//    @Override
//    public void hasPeopleBankReport(String applyId) {
//        List<Task> tasks = this.taskService.createTaskQuery()
//            .processInstanceBusinessKey(applyId).active()
//            .taskDefinitionKey(StaticUtils.TASK_PEOPLEBANK_REPORT).list();
//        if (CollectionUtils.isNotEmpty(tasks)) {
//            if ("reportNotReturn".equals(tasks.get(0).getAssignee())) {
//                this.taskService.unclaim(tasks.get(0).getId());
//            }
//        }
//    }
//
//    /**
//     * 是否允许获取任务
//     */
//    @Override
//    public boolean isAllowGetTask(String taskType, String paramName) {
//        boolean flag = false;
//        if (StringUtils.isNotBlank(taskType)
//            && StringUtils.isNotBlank(paramName)) {
//            final TSUser user = ResourceUtil.getSessionUserName();
//            final int count = this.userTaskPoolService.countTasks(
//                user.getUserName(), taskType);
//            final TLoanSystermConfigEntity config = this.systemService
//                .findUniqueByProperty(TLoanSystermConfigEntity.class,
//                    "parameter", paramName);
//            int allowCount = 0;
//            if (null != config) {
//                allowCount = Integer.valueOf(config.getValue().trim());
//            }
//
//            if (allowCount > count) {
//                flag = true;
//            }
//        }
//
//        return flag;
//    }
//
//    /**
//     * 删除非直拒的流程实例
//     *
//     * @param businessKey
//     */
//    @Override
//    public void deleteProInstByNotReject(String businessKey) {
//        List<ProcessInstance> processList = this.runtimeService
//            .createProcessInstanceQuery()
//            .processInstanceBusinessKey(businessKey).active().list();
//        if (CollectionUtils.isNotEmpty(processList)) {
//            for (ProcessInstance process : processList) {
//                if (process.getProcessDefinitionId().startsWith("auditing")) {
//                    this.userTaskPoolService.deleteByProcessInstanceId(process
//                        .getId());
//                    this.runtimeService.deleteProcessInstance(process.getId(),
//                        "");
//                }
//            }
//        }
//    }
//
//    /**
//     * 获取流程定义的ID
//     *
//     * @param applyId
//     * @param taskId
//     * @return
//     */
//    @Override
//    public String queryProcessDefinitionId(String applyId, String taskId) {
//        String processDefinitionId = "";
//        Task task = null;
//        List<HistoricProcessInstance> historicProcessInstanceList = new ArrayList<HistoricProcessInstance>();
//        if (StringUtils.isNotBlank(taskId)) {
//            Task ta = this.taskService.createTaskQuery().taskId(taskId)
//                .singleResult();
//            if (ta.getProcessDefinitionId().startsWith(
//                StaticUtils.PRECESS_NAME_SYSTEM_REJECT)) {//直拒进件有历史流程的查询历史流程，无历史流程的查询当前流程
//                List<HistoricProcessInstance> hisList = this.historyService
//                    .createHistoricProcessInstanceQuery()
//                    .processInstanceBusinessKey(applyId).finished()
//                    .orderByProcessInstanceEndTime().desc().list();
//                if (CollectionUtils.isNotEmpty(hisList)) {
//                    historicProcessInstanceList = hisList;
//                } else {
//                    task = ta;
//                }
//            } else {
//                task = ta;
//            }
//        } else {
//            List<Task> tasks = this.taskService.createTaskQuery()
//                .processInstanceBusinessKey(applyId).active()
//                .orderByTaskCreateTime().desc().listPage(0, 1);
//            if (CollectionUtils.isNotEmpty(tasks)) {
//                Task ta = tasks.get(0);
//                if (ta.getProcessDefinitionId().startsWith(
//                    StaticUtils.PRECESS_NAME_SYSTEM_REJECT)) {//直拒进件有历史流程的查询历史流程，无历史流程的查询当前流程
//                    List<HistoricProcessInstance> hisList = this.historyService
//                        .createHistoricProcessInstanceQuery()
//                        .processInstanceBusinessKey(applyId).finished()
//                        .orderByProcessInstanceEndTime().desc().list();
//                    if (CollectionUtils.isNotEmpty(hisList)) {
//                        historicProcessInstanceList = hisList;
//                    } else {
//                        task = ta;
//                    }
//                } else {
//                    task = ta;
//                }
//            }
//        }
//
//        if (null != task) {
//            if (StaticUtils.TASK_QUALITY_CHECK.equals(task
//                .getTaskDefinitionKey())) {
//                Map<String, Object> vars = this.runtimeService
//                    .getVariables(task.getExecutionId());
//                String businessKey = (String) vars
//                    .get(StaticUtils.PROCESS_BUSINESS_KEY);
//                historicProcessInstanceList = this.historyService
//                    .createHistoricProcessInstanceQuery()
//                    .processInstanceBusinessKey(businessKey).finished()
//                    .orderByProcessInstanceEndTime().desc().list();
//            } else {
//                processDefinitionId = task.getProcessDefinitionId();
//            }
//        } else {
//            if (CollectionUtils.isEmpty(historicProcessInstanceList)) {
//                historicProcessInstanceList = this.historyService
//                    .createHistoricProcessInstanceQuery()
//                    .processInstanceBusinessKey(applyId).finished()
//                    .orderByProcessInstanceEndTime().desc().list();
//            }
//        }
//
//        if (CollectionUtils.isNotEmpty(historicProcessInstanceList)) {
//            for (HistoricProcessInstance hi : historicProcessInstanceList) {
//                if (hi.getProcessDefinitionId().startsWith("auditing")) {
//                    processDefinitionId = hi.getProcessDefinitionId();
//                    break;
//                }
//            }
//        }
//        if (StringUtils.isBlank(processDefinitionId)
//            || CollectionUtils.isNotEmpty(historicProcessInstanceList)) {//直拒进件
//            for (HistoricProcessInstance hi : historicProcessInstanceList) {
//                if (hi.getProcessDefinitionId().startsWith(
//                    StaticUtils.PRECESS_NAME_SYSTEM_REJECT)) {
//                    processDefinitionId = hi.getProcessDefinitionId();
//                    break;
//                }
//            }
//        }
//
//        return processDefinitionId;
//    }
//}
