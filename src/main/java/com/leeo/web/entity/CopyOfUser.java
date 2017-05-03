package com.leeo.web.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_sys_user") 
public class CopyOfUser extends IdEntity<Long>{

    private static final long serialVersionUID = -6416431995690017358L;

	private String username;

	private String password;
	
	private String realName;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

}
