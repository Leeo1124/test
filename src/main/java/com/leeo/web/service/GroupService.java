package com.leeo.web.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.leeo.web.entity.Group;
import com.leeo.web.repository.BaseRepository;
import com.leeo.web.repository.GroupRepository;

@Service
@Transactional
public class GroupService extends BaseService<Group, Long> {

    private GroupRepository groupRepository;

    @Autowired
    @Override
    public void setRepository(BaseRepository<Group, Long> groupRepository) {
        this.baseRepository = groupRepository;
        this.groupRepository = (GroupRepository) groupRepository;
    }

    public Set<Long> findGroupIds(Long userId) {
        return Sets.newHashSet(this.groupRepository.findGroupIds(userId));
    }
}