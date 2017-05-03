package json.entity;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student {
    
    @Expose
    private int id;
    
    @Expose
    private String name;
    
    @Expose  
    @SerializedName("bir")  
    private Date birthDay;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDay() {
        return this.birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "Student [birthDay=" + this.birthDay + ", id=" + this.id
            + ", name=" + this.name + "]";
    }

}