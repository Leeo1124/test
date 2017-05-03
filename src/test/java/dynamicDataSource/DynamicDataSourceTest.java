package dynamicDataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.leeo.web.entity.User;
import com.leeo.web.service.DynamicDataSourceService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-amq.xml",
    "classpath:spring-data-jpa.xml" })
public class DynamicDataSourceTest {

    @Autowired
    private DynamicDataSourceService dynamicDataSourceServicee;

    @Test
    public void getUserByDataSourceSlave() {
        User user = this.dynamicDataSourceServicee.getUser(1l);
        System.out.println("-----: "+user);
    }
    
    @Test
    public void getUser() {
        User user = this.dynamicDataSourceServicee.getUser2(1l);
        System.out.println("-----: "+user);
    }
}
