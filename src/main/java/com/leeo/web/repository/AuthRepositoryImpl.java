package com.leeo.web.repository;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.google.common.collect.Sets;

public class AuthRepositoryImpl {
    
    @PersistenceContext  
    private EntityManager em; 
    
    public Set<Long> findRoleIds(Long userId, Set<Long> groupIds) {

        boolean hasGroupIds = groupIds.size() > 0;

        StringBuilder hql = new StringBuilder("select roleIds from Auth where ");
        hql.append(" (userId=:userId) ");

        if (hasGroupIds) {
            hql.append(" or ");
            hql.append(" (groupId in (:groupIds)) ");
        }

        Query query = this.em.createQuery(hql.toString());

        query.setParameter("userId", userId);

        if (hasGroupIds) {
            query.setParameter("groupIds", groupIds);
        }

        List<Set<Long>> roleIdSets = query.getResultList();

        Set<Long> roleIds = Sets.newHashSet();
        for (Set<Long> set : roleIdSets) {
            roleIds.addAll(set);
        }

        return roleIds;
    }
}