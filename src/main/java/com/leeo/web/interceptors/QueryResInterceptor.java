package com.leeo.web.interceptors;

import java.util.Date;

import org.hibernate.EmptyInterceptor;

/**
 * 
 * hibernate拦截器，用于支持分表（尚未验证）
 *
 */
public class QueryResInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 5300740650204171242L;

    public QueryResInterceptor(Date startTime, Date endTime) {
        
    }

    @Override
    public String onPrepareStatement(String sql) {
        //根据用户的时间替换sql中的表名  
        return sql;
    }
}