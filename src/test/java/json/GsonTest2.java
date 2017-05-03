package json;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import json.entity.Employee2;
import json.entity.Student;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.leeo.json.GsonUtil;

public class GsonTest2 {
    
    @Test
    public void toJson() {
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("李坤");
        student1.setBirthDay(new Date());

        String json = GsonUtil.toJson(student1);
        System.out.println(json);
    }

    @Test
    public void toBean() {
        System.out.println("----------------------");
        String jsonString = "{\"id\":1,\"name\":\"李四\",\"birthDay\":\"Jan 6, 2016 11:18:25 AM\"}";
        Student s = GsonUtil.toJavaBean(jsonString, Student.class);
        System.out.println(s);
    }

    @Test
    public void toJsonByList() {
        System.out.println("----------------------");
        String jsonString3 = "[{\"id\":2,\"name\":\"曹贵生\",\"birthDay\":\"Jan 6, 2016 11:23:27 AM\"},{\"id\":3,\"name\":\"柳波\",\"birthDay\":\"Jan 6, 2016 11:23:27 AM\"}]";
//        List<Student> list2 = GsonUtil.toJavaBeanByList(jsonString3,Student.class);
        List<Student> list2 = GsonUtil.getListFromJSON(jsonString3, Student[].class);
        for(Student st : list2){
            System.out.println(st);
        }
    }

    @Test
    public void toBeanByListMap() {
        System.out.println("----------------------");
        String jsonString2 = "[{\"student2\":{\"id\":2,\"name\":\"曹贵生\",\"bir\":\"Jan 7, 2016 2:05:11 PM\"},\"student3\":{\"id\":3,\"name\":\"柳波\",\"bir\":\"Jan 7, 2016 2:05:11 PM\"}},{\"student5\":{\"id\":3,\"name\":\"柳波\",\"bir\":\"Jan 7, 2016 2:05:11 PM\"},\"student4\":{\"id\":2,\"name\":\"曹贵生\",\"bir\":\"Jan 7, 2016 2:05:11 PM\"}}]";
        List<Map<String, Student>> list = GsonUtil.toJavaBeanByListMap(jsonString2,Student.class);
        for(Map<String, Student> st : list){
            System.out.println(st);
        }
    }
    
    @Test
    public void toJsonByFile() throws IOException {
        //得到的是当前类class文件的URI目录
        System.out.println(this.getClass().getResource(""));
        //得到的是当前的classpath的绝对URI路径
        System.out.println(this.getClass().getResource("/"));
        System.out.println(this.getClass().getClassLoader().getResource(""));
        System.out.println(ClassLoader.getSystemResource(""));
        //ServletActionContext.getServletContext().getRealPath("/")
        
//        File f = new File(this.getClass().getResource("/").getFile(),"json/student.json");
//        String jsonString = FileUtils.readFileToString(f, "UTF-8");
//        System.out.println(jsonString);
//        Student student = GsonUtil.toJavaBean(jsonString, Student.class);
//        System.out.println(student.toString());
        
        File f = new File(this.getClass().getResource("/").getFile(),"json/employee.txt");
        String jsonString = FileUtils.readFileToString(f, "UTF-8");
        System.out.println(jsonString);
        Employee2 employee2 = GsonUtil.toJavaBean(jsonString, Employee2.class);
        System.out.println(employee2);
        System.out.println(employee2.toString());
        
    }
    
    /**
     *  通过JsonReader解析Json对象
     * 
     */
    @Test
    public void jsonReaderTest() {
        // 这里的Json放到string中，所以加上了转译
        String jsonData = "[{\"username\":\"name01\",\"userId\":001},{\"username\":\"name02\",\"userId\":002}]";

        JsonReader reader = new JsonReader(new StringReader(jsonData));
        reader.setLenient(true); // 在宽松模式下解析
        try {
            reader.beginArray(); // 开始解析数组（包含一个或多个Json对象）
            while (reader.hasNext()) { // 如果有下一个数据就继续解析
                reader.beginObject(); // 开始解析一个新的对象
                while (reader.hasNext()) {
                    String tagName = reader.nextName(); // 得到下一个属性名
                    if (tagName.equals("username")) {
                        System.out.println(reader.nextString());
                    } else if (tagName.equals("userId")) {
                        System.out.println(reader.nextString());
                    }
                }
                reader.endObject(); // 结束对象的解析
            }
            reader.endArray(); // 结束解析当前数组
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void jsonWriterTest() throws IOException {
        Employee2 emp = EmployeeGsonExample.createEmployee();
        
        //writing on console, we can initialize with FileOutputStream to write to file
        OutputStreamWriter out = new OutputStreamWriter(System.out);
        JsonWriter writer = new JsonWriter(out);
        //set indentation for pretty print
        writer.setIndent("\t");
        //start writing
        writer.beginObject(); //{
        writer.name("id").value(emp.getId()); // "id": 123
        writer.name("name").value(emp.getName()); // "name": "David"
        writer.name("permanent").value(emp.isPermanent()); // "permanent": false
        writer.name("address").beginObject(); // "address": {
            writer.name("street").value(emp.getAddress().getStreet()); // "street": "BTM 1st Stage"
            writer.name("city").value(emp.getAddress().getCity()); // "city": "Bangalore"
            writer.name("zipcode").value(emp.getAddress().getZipcode()); // "zipcode": 560100
            writer.endObject(); // }
        writer.name("phoneNumbers").beginArray(); // "phoneNumbers": [
            for(long num : emp.getPhoneNumbers()) writer.value(num); //123456,987654
            writer.endArray(); // ]
        writer.name("role").value(emp.getRole()); // "role": "Manager"
        writer.name("cities").beginArray(); // "cities": [
            for(String c : emp.getCities()) writer.value(c); //"Los Angeles","New York"
            writer.endArray(); // ]
        writer.name("properties").beginObject(); //"properties": {
            Set<String> keySet = emp.getProperties().keySet();
            for(String key : keySet) writer.name("key").value(emp.getProperties().get(key));//"age": "28 years","salary": "1000 Rs"
            writer.endObject(); // }
        writer.endObject(); // }
 
        writer.flush();
 
        //close writer
        writer.close();
    }
}
