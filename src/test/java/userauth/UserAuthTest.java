package userauth;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.collect.Sets;
import com.leeo.web.entity.User;
import com.leeo.web.service.AuthService;
import com.leeo.web.service.GroupService;
import com.leeo.web.service.ResourceService;
import com.leeo.web.service.RoleService;
import com.leeo.web.service.UserAuthService;
import com.leeo.web.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-amq.xml",
    "classpath:spring-data-jpa.xml" })
public class UserAuthTest {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserAuthService userAuthService;
    
    @Test
    public void getGroupList(){
        User user = this.userService.findByUsername("admin");
        System.out.println(user);
        System.out.println(user.getGroupList());
    }
    
    @Test
    public void findGroupIds(){
        System.out.println(this.groupService.findGroupIds(1l));
    }
    
    @Test
    public void findRoleIds(){
        System.out.println(this.authService.findRoleIds(1l));
        System.out.println(this.authService.findRoleIds(Sets.newHashSet(1l,2l)));
        System.out.println(this.authService.findRoleIds(1L, Sets.newHashSet(1l,2l)));
    }
    
    @Test
    public void findRoles(){
        Long[] ids = {1l,2l,3l};
        System.out.println(this.roleService.findByIdIn(Arrays.asList(ids)));
        System.out.println(this.roleService.findALL(Arrays.asList(ids)));
    }
    
    @Test
    public void findResourceIdentity(){
        String str = this.resourceService.findResourceIdentity(this.resourceService.findOne(2l));
        System.out.println(str);
    }
    
    @Test
    public void findStringRoles(){
        Set<String> roles = this.userAuthService.findStringRoles(this.userService.findByUsername("admin"));
        System.out.println(roles);
    }
    
    @Test
    public void findStringPermissions(){
        Set<String> permissions = this.userAuthService.findStringPermissions(this.userService.findByUsername("admin"));
        System.out.println(permissions);
    }
}
