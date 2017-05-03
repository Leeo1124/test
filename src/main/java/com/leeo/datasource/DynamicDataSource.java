package com.leeo.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *类名：DynamicDataSource.java
 *功能：动态数据源类
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	/* 
	 * 该方法必须要重写  方法是为了根据数据库标示符取得当前的数据库
	 */
	@Override
    protected Object determineCurrentLookupKey() {
		DataSourceType dataSourceType= DataSourceContextHolder.getDataSourceType();
		return dataSourceType;
	}
	
//	@Override
//    public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
//		super.setDataSourceLookup(dataSourceLookup);
//	}
//
//	@Override
//    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
//		super.setDefaultTargetDataSource(defaultTargetDataSource);
//	}
//	
//	@Override
//    public void setTargetDataSources(Map targetDataSources) {
//		super.setTargetDataSources(targetDataSources);
//	}
//
//    @Override
//    public Logger getParentLogger() {
//        // TODO Auto-generated method stub
//        return null;
//    }

}
