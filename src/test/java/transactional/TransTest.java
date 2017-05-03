package transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leeo.transactional.TransServices;


/**
 * 事务测试
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml",
    "classpath:spring-task.xml",
    "classpath:spring-hibernate.xml",
    "classpath:spring-amq.xml",
    "classpath:spring-quartz.xml" })
public class TransTest {

	@Autowired
	private TransServices transServices;

	@Test
	public void rollbackFor() throws Exception {
		this.transServices.save();
	}

	@Test
	public void noRollbackFor() throws Exception {
		this.transServices.save2();
	}

	@Test
	public void requires_new() throws Exception {
		this.transServices.save3();
	}

	@Test
	public void required() throws Exception {
		this.transServices.save4();
	}

	@Test
	public void requires_new2() throws Exception {
		this.transServices.save5();
	}

	@Test
	public void required2() throws Exception {
		this.transServices.save6();
	}

	@Test
	public void nested() throws Exception {
		this.transServices.save7();
	}

	@Test
	public void nested2() throws Exception {
		this.transServices.save8();
	}
	
}
