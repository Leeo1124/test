package com.leeo.json;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
  
public class JsonDateValueProcessor implements JsonValueProcessor {   
  
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
       
    @Override
    public Object processArrayValue(Object value, JsonConfig config) {   
        return process(value);   
    }   
  
    @Override
    public Object processObjectValue(String key, Object value, JsonConfig config) {   
        return process(value);   
    }   
       
    private static Object process(final Object value){   
           
        if(value instanceof Date){
            SimpleDateFormat sdf = new SimpleDateFormat(JsonDateValueProcessor.DATE_TIME_FORMAT,Locale.UK);  //(format,Locale.UK);   
            return sdf.format(value);   
        }   
        return value == null ? "" : value.toString();   
    }   
}
