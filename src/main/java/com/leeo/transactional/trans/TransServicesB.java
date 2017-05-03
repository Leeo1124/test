package com.leeo.transactional.trans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransServicesB {

    @Autowired
    public TransServicesC transServicesC;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation=Propagation.NESTED)
    public void saveB() throws Exception {
        System.out.println("-----saveB");
        this.jdbcTemplate.execute("insert into test123(id, NAME, remart) values('222','bbb','bbb')");
    
        this.transServicesC.savec();
    }
}
