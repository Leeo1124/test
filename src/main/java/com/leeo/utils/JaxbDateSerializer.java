package com.leeo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbDateSerializer extends XmlAdapter<String, Date> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String marshal(Date date) {
        if (null == date) {
            return "";
        }
        return this.dateFormat.format(date);
    }

    @Override
    public Date unmarshal(String date) throws Exception {
        if ("".equals(date)) {
            return null;
        }
        return this.dateFormat.parse(date);
    }
}
