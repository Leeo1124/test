package com.leeo.amq;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SendMessageObject implements Serializable {
    
    private static final long serialVersionUID = 7999247005202429238L;

    private int id;
    private String content;
    private Date creatTime;
    private BigDecimal cost;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatTime() {
        return this.creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "SendMessageObject [id=" + this.id + ", "
            + (this.content != null ? "content=" + this.content + ", " : "")
            + (this.creatTime != null ? "creatTime=" + this.creatTime + ", " : "")
            + (this.cost != null ? "cost=" + this.cost : "") + "]";
    }
}
