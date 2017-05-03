package com.leeo.aop.service;

public class DataAop {

    private String id;
    private String name;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DataAop [" + (this.id != null ? "id=" + this.id + ", " : "")
            + (this.name != null ? "name=" + this.name : "") + "]";
    }

}
