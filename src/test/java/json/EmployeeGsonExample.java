package json;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import json.entity.Address;
import json.entity.Employee2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
 
public class EmployeeGsonExample {
 
    public static void main(String[] args) throws IOException {
        Employee2 emp = createEmployee();
 
        // Get Gson object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
 
        // read JSON file data as String
        String fileData = new String(Files.readAllBytes(Paths
                .get("D:/workspace/test/src/test/java/json/employee.txt")));
 
        // parse json string to object
        Employee2 emp1 = gson.fromJson(fileData, Employee2.class);
 
        // print object data
        System.out.println("nnEmployee Objectnn" + emp1);
 
        // create JSON String from Object
        String jsonEmp = gson.toJson(emp);
        System.out.print(jsonEmp);
 
    }
 
    public static Employee2 createEmployee() {
 
        Employee2 emp = new Employee2();
        emp.setId(100);
        emp.setName("David");
        emp.setPermanent(false);
        emp.setPhoneNumbers(new long[] { 123456, 987654 });
        emp.setRole("Manager");
 
        Address add = new Address();
        add.setCity("Bangalore");
        add.setStreet("BTM 1st Stage");
        add.setZipcode(560100);
        emp.setAddress(add);
 
        List<String> cities = new ArrayList<String>();
        cities.add("Los Angeles");
        cities.add("New York");
        emp.setCities(cities);
 
        Map<String, String> props = new HashMap<String, String>();
        props.put("salary", "1000 Rs");
        props.put("age", "28 years");
        emp.setProperties(props);
 
        return emp;
    }
}