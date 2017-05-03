package com.leeo.web.entity;

public enum AuthType {

    user("用户"), user_group("用户组");

    private final String info;

    private AuthType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return this.info;
    }
}
