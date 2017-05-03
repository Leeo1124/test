package ldap;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leeo.ldap.Group;
import com.leeo.ldap.LdapSerivce;
import com.leeo.ldap.Person;
import com.leeo.ldap.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml",
    "classpath:spring-task.xml",
    "classpath:spring-hibernate.xml",
    "classpath:spring-quartz.xml" })
public class LdapTest {

    @Autowired
    LdapSerivce ldapSerivce;

    @Test
    public void getAllNames() {
        final List<String> list = this.ldapSerivce.getAllNames();
        System.out.println(list);
    }

    @Test
    public void getAllPersons() {
        final List<Person> list = this.ldapSerivce.getAllPersons();
        for (final Person person : list) {
            System.out.println(person);
        }
    }

    @Test
    public void create() {
        final Person person = new Person();
        person.setOu("Developer");
        person.setDescription("Container for developer entries");
        this.ldapSerivce.create(person);
    }

    @Test
    public void delete() {
        final Person person = new Person();
        person.setOu("hello2");
        this.ldapSerivce.delete(person);
    }

    @Test
    public void update() {
        final Person person = new Person();
        this.ldapSerivce.update(person);
    }

    @Test
    public void modify() {
        final Person person = new Person();
        this.ldapSerivce.modify(person);
    }

    @Test
    public void getAllUsers() {
        final List<User> users = this.ldapSerivce.getAllUsers(null);
        for (final User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void getAllGroups() {
        final List<Group> groups = this.ldapSerivce.getAllGroups(null);
        for (final Group group : groups) {
            System.out.println(group);
        }
    }

}
