package json;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import json.entity.Employee;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.leeo.json.JsonDateValueProcessor;

/**
 * 使用json-lib构造和解析Json数据
 */
public class JsonlibTest {

    /**
     * 构造Json数据
     * 
     * @return
     */
    public static String BuildJson() {

        // JSON格式数据解析对象
        JSONObject jo = new JSONObject();

        // 下面构造两个map、一个list和一个Employee对象
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "Alexia");
        map1.put("sex", "female");
        map1.put("age", "23");
//        map1.put("birthday", new Date());
//        map1.put("cost", new BigDecimal("121.345678"));
//        map1.put("availableBalance", 121.345678);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "Edward");
        map2.put("sex", "male");
        map2.put("age", "24");
//        map2.put("birthday", new Date());
//        map2.put("cost", new BigDecimal("121.345678"));
//        map2.put("availableBalance", 121.345678);

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map1);
        list.add(map2);

        Employee employee = new Employee();
        employee.setName("wjl");
        employee.setSex("female");
        employee.setAge(24);
//        employee.setBirthday(new Date());
//        employee.setCost(new BigDecimal("121.345678"));
//        employee.setAvailableBalance(121.345678);

        // 将Map转换为JSONArray数据
        JSONArray ja1 = JSONArray.fromObject(map1);
        // 将List转换为JSONArray数据
        JSONArray ja2 = JSONArray.fromObject(list);
        // 将Bean转换为JSONArray数据
        JSONArray ja3 = JSONArray.fromObject(employee);

        System.out.println("JSONArray对象数据格式：");
        System.out.println(ja1.toString());
        System.out.println(ja2.toString());
        System.out.println(ja3.toString());

        // 构造Json数据，包括一个map和一个Employee对象
        jo.put("map", ja1);
        jo.put("employee", ja2);

        System.out.println("\n最终构造的JSON数据格式：");
        System.out.println(jo.toString());

        return jo.toString();

    }

    /**
     * 解析Json数据
     * 
     * @param jsonString
     *        Json数据字符串
     */
    public static void ParseJson(String jsonString) {

        // 以employee为例解析，map类似
        JSONObject jb = JSONObject.fromObject(jsonString);
        JSONArray ja = jb.getJSONArray("employee");

        List<Employee> empList = new ArrayList<>();

        // 循环添加Employee对象（可能有多个）
        for (int i = 0; i < ja.size(); i++) {
            Employee employee = new Employee();

            employee.setName(ja.getJSONObject(i).getString("name"));
            employee.setSex(ja.getJSONObject(i).getString("sex"));
            employee.setAge(ja.getJSONObject(i).getInt("age"));
//            employee.setBirthday((Date)ja.getJSONObject(i).get("birthday"));
//            employee.setCost((BigDecimal)ja.getJSONObject(i).get("cost"));
//            employee.setAvailableBalance(ja.getJSONObject(i).getDouble("availableBalance"));

            empList.add(employee);
        }

        System.out.println("\n将Json数据转换为Employee对象：");
        for (int i = 0; i < empList.size(); i++) {
            Employee emp = empList.get(i);
            System.out.println("name: " + emp.getName() + " sex: "
                + emp.getSex() + " age: " + emp.getAge());
        }

    }

    public static String toJson(Object object) {
        JSONObject jsonObject = new JSONObject();
        //实例化JSON日期类：（否则Date类型日期不正确）
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class,
            new JsonDateValueProcessor());
        JSONObject jsonOperation = JSONObject.fromObject(object, jsonConfig);
        jsonObject.accumulate("JSON", jsonOperation);

        System.out.println("转换后的JSON数据：" + jsonObject.toString());

        return jsonObject.toString();
    }

    public static void toBean(String jsonString) {
        JSONObject json = JSONObject.fromObject(jsonString);
        JSONObject jsonObject = json.getJSONObject("JSON");
        if (null != jsonObject) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date birthday = null;
            try {
                birthday = format.parse(jsonObject.getString("birthday"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            Employee employee = new Employee();
            employee.setName(jsonObject.getString("name"));
            employee.setSex(jsonObject.getString("sex"));
            employee.setAge(jsonObject.getInt("age"));
            employee.setBirthday(birthday);
            employee.setCost(BigDecimal.valueOf(jsonObject.getDouble("cost")));
            employee.setAvailableBalance(jsonObject.getDouble("availableBalance"));

            System.out.println("转换后的Bean数据：" + employee.toString());
        }
    }

    public static Employee getEmployee() {
        Employee employee = new Employee();
        employee.setName("wjl");
        employee.setSex("female");
        employee.setAge(24);
        employee.setBirthday(new Date());
        employee.setCost(new BigDecimal("121.345678"));
        employee.setAvailableBalance(121.345678);

        return employee;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {

//        ParseJson(BuildJson());

//        toJson(getEmployee());
//
        String jsonString = "{\"JSON\":{\"age\":24,\"availableBalance\":121.345678,\"birthday\":\"2015-12-28 18:47:01\",\"cost\":121.345678,\"name\":\"wjl\",\"sex\":\"female\"}}";
        toBean(jsonString);
        
    }
}