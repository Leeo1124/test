package com.leeo.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Gson工具类
 * 
 * @author Leeo
 * @create 2013-3-29 下午2:40:14
 */
public class GsonUtil {

    private GsonUtil() {

    }

    /**
     * 将Java对象转换为json字符串
     * 
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式    
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.  
                .setPrettyPrinting() //对json结果格式化.  
                .setVersion(1.0)    //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
                                 //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
                                 //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  
                .create();
        return gson.toJson(obj);
    }
    
    /**
     * 将json字符串转换为Java对象
     * 
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> T toJavaBean(String jsonString, Class<T> cls) {
        T t = cls.cast(null);
        try {
            t = new Gson().fromJson(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将json字符串转换为Java对象List集合
     * 
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> List<T> toJavaBeanByList(String jsonString, Class<T> clazz) {
//        调用报错：java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap cannot be cast to json.entity.Student
//        List<T> list = new ArrayList<>();
//        try {
////            Gson gson = new Gson();
//            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
//            }.getType());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return list;

        //jsonToList解决方案一：
        List<T> lst = new ArrayList<>();
        JsonArray array = new JsonParser().parse(jsonString).getAsJsonArray();
        for (final JsonElement elem : array) {
            lst.add(new Gson().fromJson(elem, clazz));
        }
        return lst;
    }

    /**
     * 将json字符串转换为Java对象List集合(jsonToList解决方案二)
     * 
     * @param json
     * @param type
     * @return
     */
    public static <T> List<T> getListFromJSON(String json, Class<T[]> type) {
        T[] list = new Gson().fromJson(json, type);
        return Arrays.asList(list);
    }

    /**
     * 将json字符串转换为Java对象List<Map<String, T>>集合
     * 
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> List<Map<String, T>> toJavaBeanByListMap(
            String jsonString, Class<T> clazz) {
        List<Map<String, T>> list = new ArrayList<>();
        try {
            list = new Gson().fromJson(jsonString,
                new TypeToken<List<Map<String, T>>>() {
                }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}