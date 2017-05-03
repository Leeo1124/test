package springdatajpa;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.leeo.web.entity.Role;
import com.leeo.web.entity.User;
import com.leeo.web.service.RoleService;
import com.leeo.web.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-amq.xml",
    "classpath:spring-data-jpa.xml" })
public class SpringDataJpaTest {

    @Autowired
    private UserService userService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RoleService roleService;

    @Test
    public void save() {
        User user = new User();
        user.setUsername("aa");
        user.setPassword("aa");
//        user.setRealName("管理员a");
        this.userService.saveUser(user);
    }

    @Test
    public void update() {
        User user = this.userService.getUser(2l);
        user.setUsername("admin");
        this.userService.updateUser(user);
    }
    
    @Test
    public void delete() {
        this.userService.deleteUser(2l);
    }
    
    @Test
    public void deleteByUserName() {
        this.userService.deleteByUserName("root");
    }

    @Test
    public void getUser() {
        User user = this.userService.getUser(1l);
        System.out.println(user);
    }

    @Test
    public void findAllUsers() {
        List<User> users = this.userService.findAllUsers();
        System.out.println(users);
    }

    @Test
    public void findAllUserByPage() {
        Sort sort = new Sort(Direction.DESC, "id");
        PageRequest page = new PageRequest(0, 2, sort);
        Page<User> users = this.userService.findAllUserByPage(page);
        for (User user : users) {
            System.out.println(user);
        }
    }
    
    @Test
    public void findByUsernameQueryLike() {
        List<User> users = this.userService.findByUsernameQueryLike("ad");
        System.out.println(users);
    }
    
    @Test
    public void findByNativeSql() {
        List<User> users = this.userService.findByNativeSql("ad");
        System.out.println(users);
    }
    
    @Test
    public void findByUsernameLike() {
        List<User> users = this.userService.findByUsernameLike("root");
        System.out.println(users);
    }
    
    @Test
    public void findByUsername(){
        User user = this.userService.findByUsername("admin");
        System.out.println(user);
    }
    
    @Test
    public void jdbc() {
        this.jdbcTemplate
            .execute("INSERT INTO `jg_person` VALUES ('3', 10, NOW(), NOW(), '小黑', 1000.00)");
    }
    
    @Test
    public void saveRole() {
        Role role = new Role();
//        role.setCode("admin");
        role.setName("管理员");
        this.roleService.save(role);
    }
    
    @Test
    public void findByRole() {
        Role role = this.roleService.findOne(1l);
        System.out.println(role);
    }
    
    /**
     * @QueryHints ???
     */
    @Test
    public void findRoleByPage() {
        Sort sort = new Sort(Direction.DESC, "id");
        PageRequest page = new PageRequest(0, 2, sort);
        Page<Role> roles = this.roleService.findByPage("admin", page);
        for (Role role : roles) {
            System.out.println(role);
        }
    }
}
