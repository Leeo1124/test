package com.leeo.web.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IdEntity<ID extends Serializable> extends AbstractEntity<ID> {
 
    private static final long serialVersionUID = -845868697463334925L;
    
    @Id
    //GenerationType.IDENTITY 自增长，仅限于数字id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;
 
    @Override
    public ID getId() {
        return this.id;
    }
 
    @Override
    public void setId(ID id) {
        this.id = id;
    }
}