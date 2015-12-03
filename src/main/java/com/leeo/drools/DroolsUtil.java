package com.leeo.drools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;


public class DroolsUtil {
	
	/**
	 * 评分卡
	 * @param drlPath
	 * @param insertObjects
	 * @param globasServices
	 * @return
	 */
	public static <T> Map<String, Object> doDroolsForScore(String drlPath,List<T> insertObjects,Map<String, Object> globasServices){
		 Map<String, Object> results = new HashMap<String, Object>();
		 results = doDrools( drlPath, insertObjects, globasServices);
		 Iterator<String> scoreKeys = results.keySet().iterator();
		 double sum = 0;
			while (scoreKeys.hasNext()) {
				String key = scoreKeys.next();
				sum += Double.parseDouble(String.valueOf(results.get(key)));
		 }
	     results.put("totalScore", sum);
	     
		 return results;
		
	}
	
	/**
	 * 执行规则方法
	 * @param drlPath drl 文件位置  , 如：com/drools/rules/Sample.drl
	 * @param insertObjects 传入的对象
	 * @param globasServices 传入的service,以key value的方式与drl文件中对应
	 * @return 返回结果对象Map
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> Map<String, Object> doDrools(String drlPath,List<T> insertObjects,Map<String, Object> globasServices){
		KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
		System.setProperty("drools.dateformat","yyyy-MM-dd");
		kb.add(ResourceFactory.newClassPathResource(drlPath), ResourceType.DRL);
		 
		if(kb.hasErrors()){
			KnowledgeBuilderErrors kbErrors = kb.getErrors();
			for (Iterator<KnowledgeBuilderError> iter = kbErrors.iterator();iter.hasNext();) {
				System.out.println(iter.next());
			}
		}
		
		Collection<KnowledgePackage> kpackage = kb.getKnowledgePackages();
		
		KnowledgeBaseConfiguration kbconf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		kbconf.setProperty("org.drools.sequential", "true");
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbconf);
		kbase.addKnowledgePackages(kpackage);
		
		StatefulKnowledgeSession kSession =  kbase.newStatefulKnowledgeSession();
		if(CollectionUtils.isNotEmpty(insertObjects)){
			for (int i = 0; i < insertObjects.size(); i++) {
				kSession.insert(insertObjects.get(i));
			}
		}
		 
		if(MapUtils.isNotEmpty(globasServices)){
			Iterator<String> serviceNames = globasServices.keySet().iterator();
			while (serviceNames.hasNext()) {
				String serviceName = serviceNames.next();
				kSession.setGlobal(serviceName, globasServices.get(serviceName));
			}
		}
        
        Map<String, Object> results = new HashMap<String, Object>();
        kSession.setGlobal("results", results);
        
        kSession.fireAllRules();
        kSession.dispose();
        
        return results;
	}
}
