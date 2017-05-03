package com.leeo.drools;

import java.util.ArrayList;
import java.util.List;

public class RuleVariable {
    private int contractFrom;//来源
    private String fundSource;//资金渠道
    private String productType;//产品ID
    private String wholeSale;//产品属性 0期缴 1趸缴
    private String lendChannel;//进件渠道  线上 线下 信托
    private String lendType;//客户属性  展期 新客户之类的
    private String rulePath;//规则路径
    private int isCA;//是否CA  0是 1不是
    private List<Integer> templateList = new ArrayList<>();//模板列表

    public int getContractFrom() {
        return this.contractFrom;
    }

    public void setContractFrom(int contractFrom) {
        this.contractFrom = contractFrom;
    }

    public String getFundSource() {
        return this.fundSource;
    }

    public void setFundSource(String fundSource) {
        this.fundSource = fundSource;
    }

    public String getWholeSale() {
        return this.wholeSale;
    }

    public void setWholeSale(String wholeSale) {
        this.wholeSale = wholeSale;
    }

    public String getLendChannel() {
        return this.lendChannel;
    }

    public void setLendChannel(String lendChannel) {
        this.lendChannel = lendChannel;
    }

    public String getLendType() {
        return this.lendType;
    }

    public void setLendType(String lendType) {
        this.lendType = lendType;
    }

    public int getIsCA() {
        return this.isCA;
    }

    public void setIsCA(int isCA) {
        this.isCA = isCA;
    }

    public List<Integer> getTemplateList() {
        return this.templateList;
    }

    public void setTemplateList(List<Integer> templateList) {
        this.templateList = templateList;
    }

    public String getRulePath() {
        return this.rulePath;
    }

    public void setRulePath(String rulePath) {
        this.rulePath = rulePath;
    }

    public String getProductType() {
        return this.productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return "RuleVariable [contractFrom=" + this.contractFrom
            + ", fundSource=" + this.fundSource + ", productType="
            + this.productType + ", wholeSale=" + this.wholeSale
            + ", lendChannel=" + this.lendChannel + ", lendType="
            + this.lendType + ", rulePath=" + this.rulePath + ", isCA="
            + this.isCA + ", templateList=" + this.templateList + "]";
    }
}
