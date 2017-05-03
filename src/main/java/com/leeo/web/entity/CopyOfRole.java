package com.leeo.web.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_sys_role") 
public class CopyOfRole extends UUIDEntity<String>{

    private static final long serialVersionUID = 2958371358359162542L;

    private String name;

	private String code;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
	
}
