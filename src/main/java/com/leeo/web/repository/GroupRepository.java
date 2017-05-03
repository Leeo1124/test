package com.leeo.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.leeo.web.entity.Group;

@Repository
public interface GroupRepository extends BaseRepository<Group, Long>{
    
    @Query("select groupId from UserGroup where userId=?1")
    List<Long> findGroupIds(Long userId);
}