package com.leeo.transactional.trans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransServicesA {

    @Autowired
    public TransServicesB transServicesB;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveA() {
        System.out.println("-----saveA");
        this.jdbcTemplate.execute("insert into test123(id, NAME, remart) values('111','aaa','aaa')");
        /**
         * B事物提交失败回滚，此处捕获异常spring事物报错：Transaction rolled back because it has
         * been marked as rollback-only at
         * 初步解决方案：
         * transactionManager下添加属性<property
         * name="globalRollbackOnParticipationFailure" value="false"
         * />
         */
        try {
            this.transServicesB.saveB();
        } catch (final Exception e) {
//            e.printStackTrace();
        }

    }
}
