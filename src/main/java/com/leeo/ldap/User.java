package com.leeo.ldap;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Attribute.Type;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(objectClasses = { "inetOrgPerson", "organizationalPerson", "person",
    "shadowAccount", "top" })
public class User {
    @Id
    private Name dn;
    @Attribute(name = "cn")
    private String cn;
    @Attribute(name = "uid")
    private String uid;
    @Attribute(name = "sn")
    private String sn;

    @Attribute(name = "businessCategory")
    private String businessCategory;
    @Attribute(name = "employeeType")
    private String employeeType;
    @Attribute(name = "mail")
    private String mail;
    @Attribute(name = "phone")
    private String phone;
    @Attribute(name = "title")
    private String title;
    @Attribute(name = "userPassword", type = Type.BINARY)
    private byte[] password;

    private String street;
    private String st;
    @Attribute(name = "modifyTimeStamp")
    private String modifyDateTime;
    @Attribute(name = "modifiersName")
    private String modifyUser;

    @Attribute(name = "createTimestamp")
    private String createDateTime;

    @Attribute(name = "creatorsName")
    private String createUser;

    @Attribute(name = "shadowInactive")
    private String isDisable;

    public String getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getIsDisable() {
        return this.isDisable;
    }

    public void setIsDisable(String isDisable) {
        this.isDisable = isDisable;
    }

    public String getModifyUser() {
        return this.modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getModifyDateTime() {
        return this.modifyDateTime;
    }

    public void setModifyDateTime(String modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSt() {
        return this.st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getCn() {
        return this.cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getBusinessCategory() {
        return this.businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public String getEmployeeType() {
        return this.employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Name getDn() {
        return this.dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(" cn: " + this.getCn()).append(",");
        sb.append(" uid: " + this.getUid()).append(",");
        sb.append(" password: " + this.getPassword()).append(",");
        sb.append(" phone: " + this.getPhone()).append(",");
        sb.append(" title: " + this.getTitle()).append("]");
        return sb.toString();
    }

}
