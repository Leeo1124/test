package com.leeo.web.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.leeo.web.entity.AbstractEntity;

@NoRepositoryBean
public interface BaseRepository<T extends AbstractEntity<?>, ID extends Serializable> extends PagingAndSortingRepository<T, ID>,JpaSpecificationExecutor<T> {

    List<T> findByIdIn(Collection<ID> ids);
    
    @Query("select t from #{#entityName} t where t.id in(?1)")
    Collection<T> findALL(Collection<ID> ids);
    
}