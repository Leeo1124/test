package com.leeo.transactional.trans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransServicesC {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void savec() throws Exception {
        System.out.println("-----saveC");
   
        throw new Exception("出错");
    }
}
