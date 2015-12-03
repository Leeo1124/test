package com.leeo.ldap;

import java.util.Date;
import java.util.List;

public interface LdapSerivce {

    public List<String> getAllNames();

    public List<Person> getAllPersons();

    /**
     * 新增
     *
     * @param person
     */
    public void create(Person person);

    /**
     * 删除
     *
     * @param person
     */
    public void delete(Person person);

    /**
     * 修改
     *
     * @param person
     */
    public void update(Person person);

    /**
     * 修改
     *
     * @param person
     */
    public void modify(Person person);

    public List<User> getAllUsers(Date lastTaskTime);

    public List<Group> getAllGroups(Date lastTaskTime);
}
