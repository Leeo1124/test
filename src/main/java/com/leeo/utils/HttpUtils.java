//package com.common;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.collections.MapUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpHost;
//import org.apache.http.HttpStatus;
//import org.apache.http.NameValuePair;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.AuthCache;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.protocol.HttpClientContext;
//import org.apache.http.impl.auth.BasicScheme;
//import org.apache.http.impl.client.BasicAuthCache;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.apache.log4j.Logger;
//import org.dom4j.Document;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//import org.fusesource.hawtbuf.ByteArrayInputStream;
//
//import com.app.biz.vincioResponse.entity.VincioResponseEntity;
//import com.app.biz.vincioResponseRisk.entity.VincioResponseRiskEntity;
//import com.framework.core.util.DateUtils;
//import com.framework.core.util.ResourceUtil;
//import com.framework.web.system.service.SystemService;
//
///**
// * Http请求工具类
// *
// * @author 黄呈沅
// */
//public class HttpUtils {
//    private static final Logger logger = Logger.getLogger(HttpUtils.class);
//    public static String RULE_ENGINE_URI = "http://"
//        + ResourceUtil.getConfigByName("ruleEngineIP") + ":"
//        + ResourceUtil.getConfigByName("ruleEnginePort")
//        + "/vincio/ProcessFlowServlet";
//    public static final Map<String, String> RULE_PARAMS = new HashMap<String, String>();
//    static {
////        HttpUtils.RULE_PARAMS
////        .put("_processflowname_", "OasisDemo.WorkFlowDemo");
//        HttpUtils.RULE_PARAMS.put("_executionmode_", "sync");
//        HttpUtils.RULE_PARAMS.put("_responsecontenttype_", "text/plain");
//        HttpUtils.RULE_PARAMS.put("_username_",
//            ResourceUtil.getConfigByName("ruleEngineUserName"));
//        HttpUtils.RULE_PARAMS.put("_password_",
//            ResourceUtil.getConfigByName("ruleEnginePassword"));
////        HttpUtils.RULE_PARAMS.put("_outputs_", "WorkFlowID");
//    }
//
//    /**
//     * 发送GET请求
//     *
//     * @param uri
//     *        请求URI地址
//     * @return
//     * @throws IllegalArgumentException
//     */
//    public static String doGet(String uri) throws IllegalArgumentException {
//        if (null == uri || "".equals(uri)) {
//            throw new IllegalArgumentException("uri 不能为空.");
//        }
//        final CloseableHttpClient httpclient = HttpClients.createDefault();
//        String result = "";
//        try {
//            // 创建httpget.
//            final HttpGet httpget = new HttpGet(uri);
//            // 执行get请求.
//            final CloseableHttpResponse response = httpclient.execute(httpget);
//            if (null != response) {
//                // 获取响应实体
//                final HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    result = EntityUtils.toString(entity);
//                }
//                response.close();
//            }
//        } catch (final Exception e) {
//            HttpUtils.logger.error(e);
//        } finally {
//            try {
//                httpclient.close();
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 发送POST请求
//     *
//     * @param uri
//     *        请求URI地址
//     * @param params
//     *        请求参数
//     * @return
//     * @throws IllegalArgumentException
//     */
//    public static String doPost(String uri, Map<String, String> params)
//            throws IllegalArgumentException {
//        if (null == uri || "".equals(uri)) {
//            throw new IllegalArgumentException("uri 不能为空.");
//        }
//        // 创建默认的httpClient实例.
//        final CloseableHttpClient httpclient = HttpClients.createDefault();
//        // 创建httppost
//        final HttpPost httppost = new HttpPost(uri);
//        // 创建参数队列
//        List<NameValuePair> paramList = null;
//        if (MapUtils.isNotEmpty(params)) {
//            paramList = new ArrayList<NameValuePair>();
//            for (final Map.Entry<String, String> entry : params.entrySet()) {
//                final String key = entry.getKey();
//                final String value = entry.getValue();
//                paramList.add(new BasicNameValuePair(key, value));
//            }
//        }
//        String result = "";
//        UrlEncodedFormEntity uefEntity = null;
//        try {
//            if (null != paramList) {
//                uefEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
//                httppost.setEntity(uefEntity);
//            }
//            final CloseableHttpResponse response = httpclient.execute(httppost);
//            if (null != response) {
//                final HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    result = EntityUtils.toString(entity, "UTF-8");
//                    HttpUtils.logger.info(result);
//                }
//                response.close();
//            }
//        } catch (final Exception e) {
//            HttpUtils.logger.error(e);
//        } finally {
//            try {
//                httpclient.close();
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 发送Get请求，采用Basic认证模式
//     *
//     * @param uri
//     * @param hostname
//     * @param port
//     * @param username
//     * @param password
//     * @return
//     * @throws Exception
//     */
//    @SuppressWarnings("resource")
//    public static String doGetByBasic(String uri, String hostname, int port,
//            String username, String password) throws Exception {
//        if (null == uri || "".equals(uri)) {
//            throw new IllegalArgumentException("uri 不能为空.");
//        }
//
//        final CloseableHttpClient httpclient = HttpClients.createDefault();
//        final AuthCache authCache = new BasicAuthCache();
//        final BasicScheme basicScheme = new BasicScheme();
//        final HttpClientContext context = HttpClientContext.create();
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        final HttpHost httpHost = new HttpHost(hostname, port, "http");
//        final AuthScope authScope = new AuthScope(httpHost);
//
//        authCache.put(httpHost, basicScheme);
//        context.setAuthCache(authCache);
//        credentialsProvider.setCredentials(authScope,
//            new UsernamePasswordCredentials(username, password));
//        context.setCredentialsProvider(credentialsProvider);
//
//        final HttpGet httpget = new HttpGet(uri);
//        String result = "";
//        try {
//            final CloseableHttpResponse response = httpclient.execute(httpHost,
//                httpget, context);
//            if (null != response) {
//                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                    throw new Exception("请求响应异常，响应非200状态：");
//                }
//                final HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    result = EntityUtils.toString(entity, "UTF-8");
//                    HttpUtils.logger.info(result);
//                }
//                response.close();
//            }
//        } catch (final ClientProtocolException e) {
//            HttpUtils.logger.error(e);
//        } catch (final IOException e) {
//            HttpUtils.logger.error(e);
//        } finally {
//            try {
//                httpclient.close();
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 解析规则引擎的返回值
//     *
//     * @param str
//     * @return
//     */
//    public static String resposeAnalyzeRule(String str) {
//        String result = "";
//        if (StringUtils.isNotBlank(str) && str.indexOf("ERROR") == -1
//            && str.indexOf('=') != -1) {
//            final String resp = str.split("=")[1];
//            if (StringUtils.isNotBlank(resp)) {
//                result = resp;
//            }
//        }
//
//        return StringUtils.trimToEmpty(result);
//    }
//    
//    /**
//     * 预置备环节解析规则引擎的返回值
//     *
//     * @param strApplyId 进件号
//     * @param strXML 返回的xml值
//     * @param intReponseType 类型，1为规则引擎直拒  2为评分卡
//     * @return
//     * @author Eshell Wong
//     * @throws Exception
//     */
//    public static VincioResponseEntity preResposeAnalyzeRule(String strApplyId, String strXML,Integer intResponseType) {
//    	logger.error(strApplyId+"规则引擎响应结果："+strXML);
//    	
//        //规则引擎标准给出的结果是Result=   这几个字符是7个字符。用以进行截取判断
//        if (strXML == null || strXML.length() < 7
//            || strXML.indexOf("preCAresponse") < 7) {
//            return null;
//        }
//        final VincioResponseEntity preCA = new VincioResponseEntity();
//        InputStream inputStream = null;
//        try {
//
//        	inputStream = new ByteArrayInputStream(strXML
//                .substring(7).getBytes("UTF-8"));
//            final SAXReader reader = new SAXReader();
//            reader.setEncoding("UTF-8");
//            final Document document = reader.read(inputStream);
//            final Element root = document.getRootElement();
////            if(root.element("approveSuggest")==null){
////            	return null;
////            }
//        	preCA.setApplyId(strApplyId);
//            preCA.setCreateTime(new Date());
//            preCA.setResponseType(intResponseType);
//            preCA.setApproveSuggest(root.element("approveSuggest")==null?null:root.element("approveSuggest").getText());
//            preCA.setCreditContent(root.element("creditContent")==null?null:root.element("creditContent").getText());
//            preCA.setCreditSysUser(root.element("creditSysUser")==null?null:root.element("creditSysUser").getText());
//            preCA.setCreditTime(DateUtils.parseDate(root.element("creditTime").getText(),StaticUtils.DATE_TIME_FORMAT));
//            preCA.setCreditType(root.element("creditType")==null?null:root.element("creditType").getText());
//            preCA.setMainReasonCode(root.element("mainReasonCode")==null?null:root.element("mainReasonCode").getText());
//            preCA.setMainReason(root.element("mainReason")==null?null:root.element("mainReason").getText());
//            preCA.setRemarks(root.element("remarks")==null?null:root.element("remarks").getText());
//            preCA.setSubReasonCode(root.element("subReasonCode")==null?null:root.element("subReasonCode").getText());
//            preCA.setSubReason(root.element("subReason")==null?null:root.element("subReason").getText());
//            
//            if(root.element("suggestMoneyAmount")!=null && !root.element("suggestMoneyAmount").getText().trim().equals("") ){
//            	try{
//            		preCA.setSuggestMoneyAmount(new BigDecimal(root.element("suggestMoneyAmount")
//                        .getText()));
//            	}catch(Exception ex)
//            	{
//            		logger.error(strApplyId+":VincioResponseEntity:preCA.setSuggestMoneyAmount失败！");
//            	}
//            }else
//            {
//            	preCA.setSuggestMoneyAmount(null);
//            }
//            
//            if(root.element("approvedIncome")!=null && !root.element("approvedIncome").getText().trim().equals("") ){
//            	try{
//	            preCA.setApprovedIncome(new BigDecimal(root.element("approvedIncome")
//	                    .getText()));
//            	}catch(Exception ex)
//            	{
//            		logger.error(strApplyId+":VincioResponseEntity:preCA.setApprovedIncome失败！");
//            	}
//            }else
//            {
//            	preCA.setApprovedIncome(null);
//            }
//            if(root.element("approvedLiabilities")!=null && !root.element("approvedLiabilities").getText().trim().equals("") ){
//            	try{
//	            preCA.setApprovedLiabilities(new BigDecimal(root.element("approvedLiabilities")
//	                    .getText()));
//            	}catch(Exception ex)
//            	{
//            		logger.error(strApplyId+":VincioResponseEntity:preCA.setApprovedLiabilities失败！");
//            	}
//            }else
//            {
//            	preCA.setApprovedLiabilities(null);
//            }
//            if(root.element("liabilityRatio")!=null && !root.element("liabilityRatio").getText().trim().equals("")){
//            	try{
//	            preCA.setLiabilityRatio(new BigDecimal(root.element("liabilityRatio")
//	                    .getText()));
//            	}catch(Exception ex)
//            	{
//            		logger.error(strApplyId+":VincioResponseEntity:preCA.setLiabilityRatio失败！");
//            	}
//            }else
//            {
//            	preCA.setLiabilityRatio(null);
//            }
//            if(root.element("scoreCard")!=null && !root.element("scoreCard").getText().trim().equals("")){
//            	try{
//	            preCA.setScoreCard(new BigDecimal(root.element("scoreCard")
//	                .getText()));
//            	}catch(Exception ex)
//            	{
//            		logger.error(strApplyId+":VincioResponseEntity:preCA.setScoreCard失败！");
//            	}
//            }else
//            {
//            	preCA.setScoreCard(null);
//            }
//            preCA.setScoreCardVersion(root.element("scoreCardVersion")==null?null:root.element("scoreCardVersion").getText());
//            preCA.setScoreCardType(root.element("scoreCardType")==null?null:root.element("scoreCardType").getText());
//            preCA.setScoreCardTypeDesc(root.element("scoreCardTypeDesc")==null?null:root.element("scoreCardTypeDesc").getText());
//            //2015-10-08 新增评分卡级别字段
//            preCA.setScoreCardLevel(root.element("scoreCardLevel")==null?null:root.element("scoreCardLevel").getText());
//            
//            //2015-09-02 新增额度策略相关四个字段
//            preCA.setCreditAmountStrategyType(root.element("creditAmountStrategyType")==null?null:root.element("creditAmountStrategyType").getText());
//            preCA.setCreditAmountStrategyDesc(root.element("creditAmountStrategyDesc")==null?null:root.element("creditAmountStrategyDesc").getText());
//            preCA.setVincioType(root.element("vincioType")==null?null:root.element("vincioType").getText());
//            if(root.element("creditAmountStrategyValue")!=null && !root.element("creditAmountStrategyValue").getText().trim().equals("")){
//            	try{
//	            preCA.setCreditAmountStrategyValue(new BigDecimal(root.element("creditAmountStrategyValue")
//	                .getText()));
//            	}catch(Exception ex)
//            	{
//            		logger.error(strApplyId+":VincioResponseEntity:preCA.setCreditAmountStrategyValue失败！");
//            	}
//            }else
//            {
//            	preCA.setCreditAmountStrategyValue(null);
//            }
//            //2015-09-16 新增浮动费率两个字段
//            if(root.element("isFloatingFeeRate")!=null && !root.element("isFloatingFeeRate").getText().trim().equals("")){
//            	try{// update by 黄呈沅 2015-11-07
//	            preCA.setIsFloatingFeeRate(Integer.valueOf(root.element("isFloatingFeeRate")
//	                .getText()));
//            	}catch(Exception ex)
//            	{
//            		logger.error(strApplyId+":VincioResponseEntity:preCA.isFloatingFeeRate失败！");
//            	}
//            }else
//            {
//            	preCA.setIsFloatingFeeRate(null);
//            }
//            if(root.element("floatingFeeRate")!=null && !root.element("floatingFeeRate").getText().trim().equals("")){
//            	try{
//	            preCA.setFloatingFeeRate(new BigDecimal(root.element("floatingFeeRate")
//	                .getText()));
//            	}catch(Exception ex)
//            	{
//            		logger.error(strApplyId+":VincioResponseEntity:preCA.floatingFeeRate失败！");
//            	}
//            }else
//            {
//            	preCA.setFloatingFeeRate(null);
//            }
//            
//            SystemService systemService = (SystemService) ResourceUtil.getBean("systemService");
////            systemService.getSession().getTransaction().begin();
//            String strResponseId = systemService.save(preCA).toString();
////            systemService.getSession().getTransaction().commit();
//            
//            // update by 黄呈沅 2015-11-07
//            final ArrayList<VincioResponseRiskEntity> applicationRisk = new ArrayList<VincioResponseRiskEntity>();
//            List<Element> riskList =null;
//            if(root.element("applicationRisks")!=null){
//	            riskList = root.element("applicationRisks")
//	                .elements("applicationRisk");
//            }
//            if (riskList == null || riskList.isEmpty()) {
//                preCA.setApplicationRisk(null);
//            } else {
//                //风险项list1  riskType 风险类型 content 风险描述  Map<String,String> 封装
//                for (int i = 0; i < riskList.size(); i++) {
////                    final Map<String, String> riskMap = new HashMap<String, String>();
////                    riskMap.put("riskType", riskList.get(i).element("riskType")
////                        .getText());
////                    riskMap.put("content", riskList.get(i).element("content")
////                        .getText());
////                    riskMap.put("riskRuleNumber", riskList.get(i).element("riskRuleNumber")
////                            .getText());
//                	VincioResponseRiskEntity vrRisk = new VincioResponseRiskEntity();
//                	vrRisk.setResponseId(strResponseId);
//                	vrRisk.setApplyId(strApplyId);
//                	vrRisk.setCreateTime(new Date());
//                	vrRisk.setContent( riskList.get(i).element("content")==null?null:riskList.get(i).element("content").getText());
//                	vrRisk.setRiskRuleNumber(riskList.get(i).element("riskRuleNumber")==null?null:riskList.get(i).element("riskRuleNumber").getText());
//                	vrRisk.setRiskType(riskList.get(i).element("riskType")==null?null:riskList.get(i).element("riskType").getText());
//                	systemService.save(vrRisk);
//                    applicationRisk.add(vrRisk);
//                }
//                preCA.setApplicationRisk(applicationRisk);
//            }
//        } catch (final Exception ex) {
//        	ex.printStackTrace();
//            HttpUtils.logger.error("预置备环节调用规则引擎，并接收响应操作异常：异常原因："
//                + ex.getMessage());
//            return null;
//        } finally{// update by 黄呈沅 2015-11-07
//            if(null != inputStream){
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        
////        systemService.
//        return preCA;
//    }
//
////    /**
////     * 预置备环节解析规则引擎的返回值
////     *
////     * @param str
////     * @return
////     * @author Eshell Wong
////     * @throws Exception
////     */
////    public static PreCAResponseEntity preResposeAnalyzeRule(String strXML) {
////
////        //规则引擎标准给出的结果是Result=   这几个字符是7个字符。用以进行截取判断
////        if (strXML == null || strXML.length() < 7
////            || strXML.indexOf("preCAresponse") < 7) {
////            return null;
////        }
////        final PreCAResponseEntity preCA = new PreCAResponseEntity();
////        try {
////
////        	final InputStream inputStream = new ByteArrayInputStream(strXML
////                .substring(7).getBytes());
////            final SAXReader reader = new SAXReader();
////            final Document document = reader.read(inputStream);
////            final Element root = document.getRootElement();
////            preCA.setApplyId(root.element("applyId").getText());
////            preCA.setApproveSuggest(root.element("approveSuggest").getText());
////            preCA.setCreditContent(root.element("creditContent").getText());
////            preCA.setCreditSysUser(root.element("creditSysUser").getText());
////            preCA.setCreditTime(root.element("creditTime").getText());
////            preCA.setCreditType(root.element("creditType").getText());
////            preCA.setMainReasonCode(root.element("mainReasonCode").getText());
////            preCA.setMainReason(root.element("mainReason").getText());
////            preCA.setRemarks(root.element("remarks").getText());
////            preCA.setSubReasonCode(root.element("subReasonCode").getText());
////            preCA.setSubReason(root.element("subReason").getText());
////            preCA.setSuggestMoneyAmount(new BigDecimal(root.element("suggestMoneyAmount")
////                .getText()));
////            preCA.setApprovedIncome(new BigDecimal(root.element("approvedIncome")
////                    .getText()));
////            preCA.setApprovedLiabilities(new BigDecimal(root.element("approvedLiabilities")
////                    .getText()));
////            preCA.setLiabilityRatio(new BigDecimal(root.element("liabilityRatio")
////                    .getText()));
////            preCA.setScoreCardVersion(root.element("scoreCardVersion").getText());
////            preCA.setScoreCardType(root.element("scoreCardType").getText());
////            preCA.setScoreCardTypeDesc(root.element("scoreCardTypeDesc").getText());
////            
////            preCA.setScoreCard(new BigDecimal(root.element("scoreCard")
////                .getText()));
////            final ArrayList applicationRisk = new ArrayList();
////            final List<Element> riskList = root.element("applicationRisks")
////                .elements("applicationRisk");
////
////            if (riskList == null || riskList.size() <= 0) {
////                preCA.setApplicationRisk(null);
////            } else {
////                //风险项list1  riskType 风险类型 content 风险描述  Map<String,String> 封装
////                for (int i = 0; i < riskList.size(); i++) {
////                    final Map<String, String> riskMap = new HashMap<String, String>();
////                    riskMap.put("riskType", riskList.get(i).element("riskType")
////                        .getText());
////                    riskMap.put("content", riskList.get(i).element("content")
////                        .getText());
////                    riskMap.put("riskRuleNumber", riskList.get(i).element("riskRuleNumber")
////                            .getText());
////                    applicationRisk.add(riskMap);
////                }
////                preCA.setApplicationRisk(applicationRisk);
////            }
////        } catch (final Exception ex) {
////        	ex.printStackTrace();
////            HttpUtils.logger.error("预置备环节调用规则引擎，并接收响应操作异常：异常原因："
////                + ex.getMessage());
////            return null;
////        }
////        SystemService systemService = (com.framework.web.system.service.SystemService) ResourceUtil.getBean("systemService");
//////        systemService.
////        return preCA;
////    }
//
//    public static void main(String[] args) throws IllegalArgumentException {
//        final Map<String, String> params = HttpUtils.RULE_PARAMS;
//        params.put("_processflowname_", "OasisDemo.WorkFlowDemo");
//        params.put("_outputs_", "WorkFlowID");
//        params.put(StaticUtils.PROCESS_REQUEST_USER, "admin");
//        params.put(StaticUtils.PROCESS_CHANNEL_ID, "");
//        params.put(StaticUtils.PROCESS_PRODUCT_ID, "");
//        params.put(StaticUtils.PROCESS_CITY_RISK_LEVEL, "");
//        params.put(StaticUtils.PROCESS_BUSINESS_KEY, "");
//        params.put(StaticUtils.PROCESS_TASK_PROPERTY, "");
//        params.put(StaticUtils.PROCESS_CHANNEL_NAME, "人人贷");
//        params.put(StaticUtils.PROCESS_PRODUCT_NAME, "寿险贷");
//
//        final String result = HttpUtils.resposeAnalyzeRule(HttpUtils.doPost(
//            HttpUtils.RULE_ENGINE_URI, params));
////		 String result = resposeAnalyzeRule(doGet(RULE_ENGINE_URI));
//        System.out.println("Response content: " + result);
//
//    }
//}
