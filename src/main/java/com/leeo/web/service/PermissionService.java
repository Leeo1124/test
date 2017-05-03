package com.leeo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leeo.web.entity.Permission;
import com.leeo.web.repository.BaseRepository;
import com.leeo.web.repository.PermissionRepository;

@Service
@Transactional
public class PermissionService extends BaseService<Permission, Long> {

    private PermissionRepository permissionRepository;

    @Autowired
    @Override
    public void setRepository(BaseRepository<Permission, Long> permissionRepository) {
        this.baseRepository = permissionRepository;
        this.permissionRepository = (PermissionRepository) permissionRepository;
    }

}