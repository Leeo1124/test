package email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.leeo.email.MimeMailService;
import com.leeo.email.SimpleMailService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-amq.xml",
    "classpath:spring-data-jpa.xml", "classpath:spring-email.xml" })
public class MailServiceTest {

	@Autowired
	private MimeMailService mimeMailService;

	@Autowired
	private SimpleMailService simpleMailService;

	@Test
	public void sendSimpleMail()  {
		this.simpleMailService.sendNotificationMail("test");
	}

	@Test
	public void sendMimeMail() {
		this.mimeMailService.sendNotificationMail("test");
	}

}
