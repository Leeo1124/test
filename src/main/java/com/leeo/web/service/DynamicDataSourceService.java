package com.leeo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leeo.datasource.DataSourceType;
import com.leeo.dynamicDataSource.DataSource;
import com.leeo.web.entity.User;

@Service
@Transactional
public class DynamicDataSourceService {

    @Autowired
    private UserService userService;

    @DataSource(DataSourceType.dataSource_slave)
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return this.userService.getUser(id);
    }
    
    @Transactional(readOnly = true)
    public User getUser2(Long id) {
        return this.userService.getUser(id);
    }
}