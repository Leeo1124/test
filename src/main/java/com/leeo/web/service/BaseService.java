package com.leeo.web.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leeo.web.entity.AbstractEntity;
import com.leeo.web.repository.BaseRepository;

@Service
@Transactional
public abstract class BaseService<T extends AbstractEntity<?>, ID extends Serializable> {

    protected BaseRepository<T, ID> baseRepository;

    public abstract void setRepository(BaseRepository<T, ID> baseRepository);
    
    /**
     * 保存单个实体
     *
     * @param m 实体
     * @return 返回保存的实体
     */
    public T save(T t) {
        return this.baseRepository.save(t);
    }

    /**
     * 更新单个实体
     *
     * @param m 实体
     * @return 返回更新的实体
     */
    public T update(T t) {
        return this.baseRepository.save(t);
    }

    /**
     * 根据主键删除相应实体
     *
     * @param id 主键
     */
    public void delete(ID id) {
        this.baseRepository.delete(id);
    }

    /**
     * 删除实体
     *
     * @param t 实体
     */
    public void delete(T t) {
        this.baseRepository.delete(t);
    }



    /**
     * 按照主键查询
     *
     * @param id 主键
     * @return 返回id对应的实体
     */
    public T findOne(ID id) {
        return this.baseRepository.findOne(id);
    }

    /**
     * 实体是否存在
     *
     * @param id 主键
     * @return 存在 返回true，否则false
     */
    public boolean exists(ID id) {
        return this.baseRepository.exists(id);
    }

    /**
     * 统计实体总数
     *
     * @return 实体总数
     */
    public long count() {
        return this.baseRepository.count();
    }

    /**
     * 分页及排序查询实体
     *
     * @param pageable 分页及排序数据
     * @return
     */
    public Page<T> findAll(Pageable pageable) {
        return this.baseRepository.findAll(pageable);
    }
    
    public List<T> findAll(Specification<T> spec) {
        return this.baseRepository.findAll(spec);
    }
    
    @Transactional(readOnly = true)
    public List<T> findByIdIn(Collection<ID> ids){
        return this.baseRepository.findByIdIn(ids);
    }
    
    @Transactional(readOnly = true)
    public Collection<T> findALL(Collection<ID> ids){
        return this.baseRepository.findALL(ids);
    }

}
