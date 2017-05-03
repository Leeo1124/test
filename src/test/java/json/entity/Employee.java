package json.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Employee {

    private String name;
    private String sex;
    private int age;
    private Date birthday;
    private BigDecimal cost;
    private Double availableBalance;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Double getAvailableBalance() {
        return this.availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    @Override
    public String toString() {
        return "Employee ["
            + (this.name != null ? "name=" + this.name + ", " : "")
            + (this.sex != null ? "sex=" + this.sex + ", " : "")
            + "age="
            + this.age
            + ", "
            + (this.birthday != null ? "birthday=" + this.birthday + ", " : "")
            + (this.cost != null ? "cost=" + this.cost + ", " : "")
            + (this.availableBalance != null ? "availableBalance="
                + this.availableBalance : "") + "]";
    }

}
