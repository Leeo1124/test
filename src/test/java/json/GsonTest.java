package json;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import json.entity.Student;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonTest {

    @Test
    public void toJson() {
        Gson gson = new Gson();

        Student student1 = new Student();
        student1.setId(1);
        student1.setName("李四");
//        student1.setBirthDay(new Date());

        // 简单的bean转为json
        String s1 = gson.toJson(student1);
        System.out.println("简单Bean转化为Json===" + s1);
    }

    @Test
    public void toBean() {
        Gson gson = new Gson();
        String str = "{\"id\":1,\"name\":\"李四\",\"birthDay\":\"Jan 6, 2016 11:18:25 AM\"}";

        // json转为简单Bean
        Student student = gson.fromJson(str, Student.class);
        System.out.println("Json转为简单Bean===" + student);
    }

    @Test
    public void toJsonByList() {
        Gson gson = new Gson();

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("曹贵生");
        student2.setBirthDay(new Date());

        Student student3 = new Student();
        student3.setId(3);
        student3.setName("柳波");
        student3.setBirthDay(new Date());

        List<Student> list = new ArrayList<>();
        list.add(student2);
        list.add(student3);

        // 带泛型的list转化为json
        String s2 = gson.toJson(list);
        System.out.println("带泛型的list转化为json==" + s2);
    }

    @Test
    public void toBeanByList() {
        Gson gson = new Gson();
        String str = "[{\"id\":2,\"name\":\"曹贵生\",\"birthDay\":\"Jan 6, 2016 11:23:27 AM\"},{\"id\":3,\"name\":\"柳波\",\"birthDay\":\"Jan 6, 2016 11:23:27 AM\"}]";

        // json转为带泛型的list
        List<Student> retList = gson.fromJson(str,
            new TypeToken<List<Student>>() {
            }.getType());
        for (Student stu : retList) {
            System.out.println(stu);
        }
    }

    @Test
    public void toJsonByMap() {
        Gson gson = new Gson();

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("曹贵生");
        student2.setBirthDay(new Date());

        Student student3 = new Student();
        student3.setId(3);
        student3.setName("柳波");
        student3.setBirthDay(new Date());

        Map<String, Student> map = new HashMap<>();
        map.put("student2", student2);
        map.put("student3", student3);

        Map<String, Student> map2 = new HashMap<>();
        map2.put("student4", student2);
        map2.put("student5", student3);

        List<Map<String, Student>> list = new ArrayList<>();
        list.add(map);
        list.add(map2);

        // 带泛型的Map转化为json
        String s2 = gson.toJson(list);
        System.out.println("带泛型的list转化为json==" + s2);
    }
}
