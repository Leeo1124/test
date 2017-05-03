package com.leeo.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leeo.web.entity.User;
import com.leeo.web.repository.UserRepository;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    //更新缓存
    @CachePut(value = "userCache", key = "targetClass + '.' + #user.username")
    public void updateUser(User user) {
        this.userRepository.saveAndFlush(user);
    }

    //清除缓存
    @CacheEvict(value = "userCache", key = "targetClass + '.' + #id")
    public void deleteUser(Long id) {
        this.userRepository.delete(id);
    }

    //清除缓存
    @CacheEvict(value = "userCache", key = "targetClass + '.' + #username")
    public void deleteByUserName(String username) {
        this.userRepository.deleteByUserName(username);
    }

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return this.userRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<User> findAllUserByPage(PageRequest page) {
        return this.userRepository.findAll(page);
    }

    @Transactional(readOnly = true)
    public List<User> findByUsernameLike(String username) {
        return this.userRepository.findByUsernameLike(username);
    }

    @Transactional(readOnly = true)
    public List<User> findByUsernameQueryLike(String username) {
        return this.userRepository.findByUsernameQueryLike(username);
    }

    @Cacheable(value = "userCache")
    @Transactional(readOnly = true)
    public List<User> findByNativeSql(String username) {
        return this.userRepository.findByNativeSql(username);
    }

    //查询缓存，先从缓存中读取，如果没有再调用方法获取数据，然后把数据添加到缓存中
    // key = "targetClass + '.' + methodName + '.' + #username"
    @Cacheable(value = "userCache", key = "targetClass + '.' + #username")
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    //内部调用（this），所以没有走 proxy，导致 spring cache 失效(非 public方法类似)
    //,如果想实现基于注释的缓存，必须采用基于 AspectJ 的 AOP 机制
    public User findByUsername2(String username) {
        return this.findByUsername(username);
    }

}