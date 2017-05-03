package drools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.leeo.drools.DroolsUtil;

/**
 * 1、首先需要通过使用KnowledgeBuilder 将相关的规则文件进行编译，产生对应的KnowledgePackage集合。
 * 2、接下来再通过KnowledgeBase 把产生的KnowledgePackage 集合收集起来。
 * 3、最后再产生StatefulKnowledgeSession 将规则当中需要使用的fact 对象插入进去、将规则当中需要用
 * 到的global 设置进去，然后调用fireAllRules()方法触发所有的规则执行。
 * 4、最后调用dispose()方法将内存资源释放。
 * -------------
 * StatelessKnowledgeSession 对StatefulKnowledgeSession 做
 * 了包装，使得在使用StatelessKnowledgeSession 对象时不需要再调用dispose()方法释放内存
 * 资源了。在使用StatelessKnowledgeSession 时不能进行重复插入fact 的操作、也不能重复的调用
 * fireAllRules()方法来执行所有的规则，对应这些要完成的工作在StatelessKnowledgeSession
 * 当中只有execute(…)方法，通过这个方法可以实现插入所有的fact 并且可以同时执行所有的
 * 规则或规则流，事实上也就是在执行execute(…)方法的时候就在StatelessKnowledgeSession
 * 内部执行了insert()方法、fireAllRules()方法和dispose()方法。
 */
public class Test {
    
    private static final KnowledgeBuilder kbuilder;
    
    static {//方案一：只加载一次（缺点不能实时生效）(5.3.0.Final有问题 5.6.0.Final无问题)
        kbuilder = KnowledgeBuilderFactory
                .newKnowledgeBuilder();
            kbuilder.add(
                ResourceFactory.newClassPathResource("rules/test.drl", Test.class),
                ResourceType.DRL);
    }
    
    public static void main(String[] args) {
//        //方案一：synchronized同步，否则多线程调用编译规则文件时会报错(5.3.0.Final有问题 5.6.0.Final无问题)
//        for(int i=0;i<10;i++){
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    callRules2();
////                    callRules();
//                }
//            });
//            thread.start();
//        }
        
//        callRules();
        
//        callRules();
        
        callMapRules();
    }
    
    public static void callRules(){
//        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
//                .newKnowledgeBuilder();
//            kbuilder.add(
//                ResourceFactory.newClassPathResource("rules/test.drl", Test.class),
//                ResourceType.DRL);
            if (kbuilder.hasErrors()) {
                System.out.println("规则中存在错误，错误消息如下：");
                KnowledgeBuilderErrors kbuidlerErrors = kbuilder.getErrors();
                for (Iterator<KnowledgeBuilderError> iter = kbuidlerErrors
                    .iterator(); iter.hasNext();) {
                    System.out.println(iter.next());
                }
            }
            //产生规则包的集合
            Collection<KnowledgePackage> kpackage = kbuilder.getKnowledgePackages();
            //设置环境参数
            KnowledgeBaseConfiguration kbConf = KnowledgeBaseFactory
                .newKnowledgeBaseConfiguration();
            kbConf.setProperty("org.drools.sequential", "true");
            KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbConf);
            kbase.addKnowledgePackages(kpackage);//将KnowledgePackage集合添加到KnowledgeBase当中

            StatefulKnowledgeSession statefulKSession = kbase
                .newStatefulKnowledgeSession();
//            statefulKSession.setGlobal("globalTest", new Object());//设置一个global对象
            List<String> list = new ArrayList<>();
            list.add("a");
            list.add("b");
            list.add("c");
            Map<String,String> map = new HashMap<>();
            map.put("a", "hello");
            map.put("b", "world");
            DataTest dataTest = new DataTest();
            dataTest.setList(list);
            dataTest.setMap(map);//map 5.3报错 java.util.HashMap cannot be cast to java.util.Collection
            dataTest.setName("person");
            
            statefulKSession.insert(dataTest);//插入一个fact对象
            
            Map<String, Object> results = new HashMap<>();
            statefulKSession.setGlobal("results", results);
            List<String> resultList = new ArrayList<>();
            statefulKSession.setGlobal("resultList", resultList);
            
            int count = statefulKSession.fireAllRules();
            System.out.println("总执行了"+count+"条规则"); 
            statefulKSession.dispose();
            
            String processKey = (String) results.get("processKey");
            System.out.println("规则返回值："+processKey);
            System.out.println("规则返回值："+resultList.get(0));
            
//            StatelessKnowledgeSession
//            statelessKSession=kbase.newStatelessKnowledgeSession();
//            ArrayList<Object> list=new ArrayList<Object>();
//            list.add(new Object());
//            list.add(new Object());
//            statelessKSession.execute(list);
    }
    
    public static void callRules2(){
        List<DataTest> list = new ArrayList<>();
        DataTest data1 = new DataTest();
        data1.setName("person");
        DataTest data2 = new DataTest();
        data2.setName("person");
        DataTest data3 = new DataTest();
        data3.setName("person");
        list.add(data1);
        list.add(data2);
        list.add(data3);
        final Map<String, Object> result = DroolsUtil.doDrools(
            "rules/test.drl", list, null);
        final String processKey = (String) result.get("processKey");
        System.out.println("------------:" + processKey);
    }
    
    public static void callMapRules(){
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("name", "person");
        map1.put("code", "a");
        Map<String, String> map2 = new HashMap<>();
        map2.put("name", "person");
        map2.put("code", "b");
//        Map<String, String> map3 = new HashMap<>();
//        map3.put("name", "world");
//        map3.put("code", "c");
        list.add(map1);
        list.add(map2);
//        list.add(map3);
        final Map<String, Object> result = DroolsUtil.doDrools(
            "rules/mapTest.drl", list, null);
        final String processKey = (String) result.get("processKey");
        System.out.println("------------:" + processKey);
    }
}