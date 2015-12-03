package com.leeo.ldap;

public class Person {

    private String ou;
    private String uid;
    private String fullName;
    private String lastName;
    private String description;

    public String getOu() {
        return this.ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Person [ou=" + this.ou + ", fullName=" + this.fullName
                + ", lastName=" + this.lastName + ", description="
                + this.description + "]";
    }

}
