package com.leeo.web.repository;

import org.springframework.stereotype.Repository;

import com.leeo.web.entity.Permission;

@Repository
public interface PermissionRepository extends BaseRepository<Permission, Long>{
    
}