package com.leeo.quartz;

import org.quartz.StatefulJob;

/**
 * 实现StatefulJob接口，用于支持concurrent属性
 *
 */
public class MyDetailQuartzStatefulJobBean extends MyDetailQuartzJobBean implements StatefulJob {


}