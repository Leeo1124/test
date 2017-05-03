package com.leeo.web.entity;

public enum GroupType {

    user("用户组"), admin("管理员");

    private final String info;

    private GroupType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return this.info;
    }
}
