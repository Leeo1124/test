package com.leeo.web.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leeo.web.entity.Auth;
import com.leeo.web.repository.AuthRepository;
import com.leeo.web.repository.BaseRepository;

@Service
@Transactional
public class AuthService extends BaseService<Auth, Long> {

    private AuthRepository authRepository;

    @Autowired
    @Override
    public void setRepository(BaseRepository<Auth, Long> authRepository) {
        this.baseRepository = authRepository;
        this.authRepository = (AuthRepository) authRepository;
    }

    @Transactional(readOnly = true)
    public Set<Long> findRoleIds(Long userId) {
        return this.authRepository.findRoleIds(userId);
    }

    @Transactional(readOnly = true)
    public Set<Long> findRoleIds(Set<Long> groupIds) {
        return this.authRepository.findRoleIds(groupIds);
    }

    @Transactional(readOnly = true)
    public Set<Long> findRoleIds(Long userId, Set<Long> groupIds) {
        return this.authRepository.findRoleIds(userId, groupIds);
    }
}