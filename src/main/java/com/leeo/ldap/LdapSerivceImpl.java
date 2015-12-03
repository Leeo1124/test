package com.leeo.ldap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.WhitespaceWildcardsFilter;
import org.springframework.ldap.query.ConditionCriteria;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

@Service
public class LdapSerivceImpl implements LdapSerivce {
    public static final String BASE_DN = "dc=micmiu,dc=com";

    @Autowired
    private LdapTemplate ldapTemplate;

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public List<String> getAllNames() {//测试通过
        final LdapQuery query = LdapQueryBuilder.query().where("objectclass").is("inetOrgPerson");

        return this.ldapTemplate.search(query,
            new AttributesMapper<String>() {
            @Override
            public String mapFromAttributes(Attributes attrs)
                    throws NamingException {
                return attrs.get("cn").get().toString();
            }
        });
    }

    @Override
    public List<Person> getAllPersons() {//测试通过
        //方式一：
//        final LdapQuery query = LdapQueryBuilder.query().where("objectclass").is("inetOrgPerson");
//
//        return this.ldapTemplate.search(query, new PersonAttributesMapper());

        //方式二：
        /*
         * 可以理解为查询条件的过滤器，比如sql中的where子句，
         * 但下面的关于objectclass的属性是必须写的，理解为1=1吧
         */
        final AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "inetOrgPerson"));
//        andFilter.and(new LikeFilter("cn", "*Michael*"));
        andFilter.and(new WhitespaceWildcardsFilter("cn", "Michael"));

        return this.ldapTemplate.search("",
            andFilter.encode(), SearchControls.SUBTREE_SCOPE,
            new PersonAttributesMapper());

        //方式三：
        /*
         * search方法中设计到三个参数，第一个是dn(类似于路径url)，
         * 这里为空是因为 我们之前XML中已经配置了根地址，
         * 第二个就是对象类型，第三个是要查询之后返回的信息
         */
//        return this.ldapTemplate.search("", "(objectclass=domain)",
//            new AttributesMapper<Person>() {
//            public Person mapFromAttributes(Attributes attrs)
//                    throws NamingException {
//                final Person Person = new Person();
//                final String name = (String) attrs.get("dc").get();
//                Person.setFullName(name);
//                return Person;
//            }
//        });
    }

    @Override
    public void create(Person person) {//测试通过
//        final DistinguishedName dn = new DistinguishedName();
//        dn.add("ou", "Tester");

        final Name dn = LdapNameBuilder.newInstance().add("ou", "Tester")
                .add("uid", "hello2").build();

        final Attributes attrs = new BasicAttributes();
        attrs.put("objectclass", "inetOrgPerson");
        attrs.put("uid", person.getOu());
        attrs.put("cn", person.getDescription());
        attrs.put("sn", person.getDescription());

        //方式一：
        this.ldapTemplate.bind(dn, null, attrs);

        //方式二：
//        final DirContextAdapter context = new DirContextAdapter(dn);
//        context.setAttributeValues("objectclass",
//            new String[] { "organizationalUnit" });
//        context.setAttributeValue("description", "asd");
//        this.ldapTemplate.bind(context);
    }

    @Override
    public void delete(Person person) {//测试通过
        //dn(类似于路径url)，这里为空是因为 我们之前XML中已经配置了根地址
        final Name dn = LdapNameBuilder.newInstance()
                .add("ou", "Tester")
                .add("uid", person.getOu())
                .build();
        this.ldapTemplate.unbind(dn);
    }

    @Override
    public void update(Person g) {//测试通过
        final Name dn = LdapNameBuilder.newInstance()
                .add("ou", "Tester")
                .add("uid", "Miumiu")
                .build();

        final Attributes attrs = new BasicAttributes();
        final BasicAttribute basicAttr = new BasicAttribute("objectclass");
        basicAttr.add("inetOrgPerson");
        attrs.put(basicAttr);
        attrs.put("uid", "sss");
        attrs.put("cn", "aaa");
        attrs.put("sn", "bbb");

        //暴力型,删除后重新添加一个记录
        this.ldapTemplate.rebind(dn, null, attrs);
    }

    @Override
    public void modify(Person person) {//测试通过
        final Name dn = LdapNameBuilder.newInstance().add("ou", "Tester")
                .build();

        /* 同样也封装了个修改的方法，但是他把这个方法放在了里面 */
        /* 设置修改的属性，但这里只能修改一个属性就新建一个不能用Attributes(至少我没用过，因为后面有更好的方法) */
        final Attribute attr = new BasicAttribute("description",
                "Description 测试");
        /* 修改的类 将需要修改的属性放在里面 */
        final ModificationItem item = new ModificationItem(
            DirContext.REPLACE_ATTRIBUTE, attr);
        /*
         * 进行修改 创建一个修改条件，他是ModificationItem[]数组格式 所以要同时更新两个属性 就要
         * 创建两个ModificationItem 然后放进数组
         */
        this.ldapTemplate.modifyAttributes(dn, new ModificationItem[] { item });
    }

    private static Attributes buildAttributes(Person person) {
        final Attributes attrs = new BasicAttributes();
        final BasicAttribute basicAttr = new BasicAttribute("objectclass");
//        basicAttr.add("top");
        basicAttr.add("organizationalUnit");
        attrs.put(basicAttr);
        attrs.put("dc", person.getOu());
        attrs.put("description", person.getDescription());
//        attrs.put("uid", "sss");
//        attrs.put("cn", "aaa");
//        attrs.put("sn", "bbb");

        return attrs;
    }

    protected Name buildDn(Person person) {
        return LdapNameBuilder.newInstance()
                .add("ou", "Developer")
                .add("uid", "Michael")
                .build();
    }

    private class PersonAttributesMapper implements AttributesMapper<Person> {
        @Override
        public Person mapFromAttributes(Attributes attrs)
                throws NamingException {
            final Person person = new Person();
            person.setFullName((String) attrs.get("cn").get());
            person.setLastName((String) attrs.get("sn").get());
//            Person.setDescription((String) attrs.get("description").get());
            return person;
        }
    }

    @Override
    public List<User> getAllUsers(Date lastTaskTime) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("utc"));
        final ConditionCriteria criteria = LdapQueryBuilder.query().base("ou=users")
                .where("modifyTimeStamp");
        LdapQuery query = null;
        if (lastTaskTime == null) {
            query = criteria.lte(dateFormat.format(new Date()));
        } else {
            query = criteria.gte(dateFormat.format(lastTaskTime));
        }

        return this.ldapTemplate.find(query, User.class);
    }

    @Override
    public List<Group> getAllGroups(Date lastTaskTime) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("utc"));
        final ConditionCriteria criteria = LdapQueryBuilder.query()
                .base("ou=groups").where("modifyTimeStamp");
        LdapQuery query = null;
        if (lastTaskTime == null) {
            query = criteria.lte(dateFormat.format(new Date()));
        } else {
            query = criteria.gte(dateFormat.format(lastTaskTime));
        }

        return this.ldapTemplate.find(query, Group.class);
    }
}