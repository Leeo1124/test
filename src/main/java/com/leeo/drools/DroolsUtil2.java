package com.leeo.drools;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

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

public class DroolsUtil2 {

	public static synchronized KnowledgeBase getKnowLedgeBase(String drlPath) {
	    System.out.println(Thread.currentThread().getName()+"-----------------------");
		KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
		System.setProperty("drools.dateformat", "yyyy-MM-dd");
		kb.add(ResourceFactory.newFileResource(drlPath), ResourceType.DRL);

		if (kb.hasErrors()) {
			KnowledgeBuilderErrors kbErrors = kb.getErrors();
			for (Iterator<KnowledgeBuilderError> iter = kbErrors.iterator(); iter.hasNext();) {
				System.out.println(iter.next());
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		Collection<KnowledgePackage> kpackage = kb.getKnowledgePackages();
		KnowledgeBaseConfiguration kbconf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		kbconf.setProperty("org.drools.sequential", "true");
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbconf);
		kbase.addKnowledgePackages(kpackage);
		System.out.println(Thread.currentThread().getName());
		return kbase;
	}
	public static KnowledgeBase getKnowLedgeBase(File drlFile) {
		KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
		System.setProperty("drools.dateformat", "yyyy-MM-dd");
		kb.add(ResourceFactory.newFileResource(drlFile), ResourceType.DRL);
		if (kb.hasErrors()) {
			KnowledgeBuilderErrors kbErrors = kb.getErrors();
			for (Iterator<KnowledgeBuilderError> iter = kbErrors.iterator(); iter.hasNext();) {
				System.out.println(iter.next());
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		Collection<KnowledgePackage> kpackage = kb.getKnowledgePackages();
		KnowledgeBaseConfiguration kbconf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		kbconf.setProperty("org.drools.sequential", "true");
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbconf);
		kbase.addKnowledgePackages(kpackage);
		return kbase;
	}
	
	public static void main(String[] args) {
//	    final CountDownLatc latch = new CountDownLatch(1);
        for(int i=0;i<50;i++){
            
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    try {
//                        latch.await();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    DroolsUtil2.getKnowLedgeBase("D:/workspace/test/src/main/resources/rules/loan.drl");
                }
            }).start();
        }
//        latch.countDown();
    }

}
