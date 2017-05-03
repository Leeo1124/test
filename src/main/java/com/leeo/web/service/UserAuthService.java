package com.leeo.web.service;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.leeo.web.entity.Permission;
import com.leeo.web.entity.Resource;
import com.leeo.web.entity.Role;
import com.leeo.web.entity.RoleResourcePermission;
import com.leeo.web.entity.User;

@Service
@Transactional
public class UserAuthService {
    
    @Autowired
    private GroupService groupService;
    @Autowired
    private AuthService authService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private PermissionService permissionService;
    
    public Set<Role> findRoles(User user) {

        if (user == null) {
            return Sets.newHashSet();
        }
        Long userId = user.getId();

        Set<Long> groupIds = this.groupService.findGroupIds(userId);
        Set<Long> roleIds = this.authService.findRoleIds(userId, groupIds);
        Set<Role> roles = this.roleService.findShowRoles(roleIds);

        return roles;

    }
    
    public Set<String> findStringRoles(User user) {
        Set<Role> roles = findRoles(user);
        return Sets.newHashSet(Collections2.transform(roles, new Function<Role, String>() {
            @Override
            public String apply(Role input) {
                return input.getRole();
            }
        }));
    }
    
    public Set<String> findStringPermissions(User user) {
        Set<String> permissions = Sets.newHashSet();

        Set<Role> roles = findRoles(user);
        for (Role role : roles) {
            for (RoleResourcePermission rrp : role.getResourcePermissions()) {
                Resource resource = this.resourceService.findOne(rrp.getResourceId());

                String actualResourceIdentity = this.resourceService.findResourceIdentity(resource);

                //不可用 即没查到 或者标识字符串不存在
                if (resource == null || StringUtils.isEmpty(actualResourceIdentity) || Boolean.FALSE.equals(resource.getShow())) {
                    continue;
                }

                for (Long permissionId : rrp.getPermissionIds()) {
                    Permission permission = this.permissionService.findOne(permissionId);

                    //不可用
                    if (permission == null || Boolean.FALSE.equals(permission.getShow())) {
                        continue;
                    }
                    permissions.add(actualResourceIdentity + ":" + permission.getPermission());

                }
            }

        }

        return permissions;
    }
}