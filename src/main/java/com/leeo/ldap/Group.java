package com.leeo.ldap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

import com.sun.istack.internal.NotNull;

@Entry(objectClasses = { "posixGroup", "top", "extensibleObject" })
public class Group implements Iterable<Group> {

    @Id
    private Name dn;
    @Attribute(name = "cn")
    private String cn;
    @Attribute(name = "gidNumber")
    private String gid;
    @Attribute(name = "businessCategory")
    private String businessCategory;
    @Attribute(name = "co")
    private String co;
    @Attribute(name = "displayName")
    private String displayName;
    @Attribute(name = "st")
    private String st;
    @Attribute(name = "street")
    private String city;
    @Attribute(name = "departmentNumber")
    private String departmentNumber;
    @Attribute(name = "telephoneNumber")
    private String phone;
    @Attribute(name = "shadowFlag")
    private String type;
    @Attribute(name = "memberUid")
    private List<String> memberUids;
    @NotNull
    @Transient
    private String parentDN;

    @Transient
    private String parentGid;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Transient
    private Group parentNode;
    @Transient
    private Set<Group> childNodes = new HashSet<Group>();

    public Group getParentNode() {
        return this.parentNode;
    }

    public void setParentNode(Group parentNode) {
        this.parentNode = parentNode;
    }

    public Set<Group> getChildNodes() {
        return this.childNodes;
    }

    public void setChildNodes(Set<Group> childNodes) {
        this.childNodes = childNodes;
    }

    public Name getDn() {
        return this.dn;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getCn() {
        return this.cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getGid() {
        return this.gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getBusinessCategory() {
        return this.businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public String getCo() {
        return this.co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSt() {
        return this.st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getParentDN() {
        return this.parentDN;
    }

    public void setParentDN(String parentDN) {
        this.parentDN = parentDN;
    }

    public List<String> getMemberUids() {
        return this.memberUids;
    }

    public void setMemberUids(List<String> memberUids) {
        this.memberUids = memberUids;
    }

    public String getDepartmentNumber() {
        return this.departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(" gidNumber : " + this.getGid()).append(" ;");
        sb.append(" co : " + this.getCo()).append(" ;");
        sb.append(" cn : " + this.getCn()).append(" ;");
        sb.append(" st : " + this.getSt()).append(" ;");
        sb.append(" type : " + this.getType()).append(" ;");
        sb.append(" telephone : " + this.getPhone()).append("]");
        return sb.toString();
    }

    public Iterator<Group> iterator() {
        return new LevelOrderTreeIterator(this);
    }

    private static class LevelOrderTreeIterator implements Iterator<Group> {
        private List<Group> list = new LinkedList<Group>();

        public LevelOrderTreeIterator(Group root) {
            final Group current = root;
            this.list.add(current);
        }

        public boolean hasNext() {
            return !this.list.isEmpty();
        }

        public Group next() {
            final Group current = this.list.remove(0);
            this.list.addAll(current.getChildNodes());

            return current;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
