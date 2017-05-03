package com.leeo.shrio;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.leeo.web.entity.User;
import com.leeo.web.service.UserAuthService;
import com.leeo.web.service.UserService;

public class UserRealm extends AuthorizingRealm {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);
    
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthService userAuthService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername().trim();
        String password = "";
        if (null != token.getPassword()) {
            password = new String(token.getPassword());
        }
        User user = this.userService.findByUsername(username);
        if (user != null) {
            if ("disabled".equals(user.getStatus())) {
                throw new DisabledAccountException();
            }

            return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
        } else {
            return null;
        }
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        User user = this.userService.findByUsername(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(this.userAuthService.findStringRoles(user));
        authorizationInfo.setStringPermissions(this.userAuthService.findStringPermissions(user));

        return authorizationInfo;
    }
    
}
