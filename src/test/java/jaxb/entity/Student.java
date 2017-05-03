package jaxb.entity;
 import javax.xml.bind.annotation.XmlAccessType;  
import javax.xml.bind.annotation.XmlAccessorType;  
import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  
import javax.xml.bind.annotation.XmlType;  
  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "student")  
@XmlType(propOrder = {})  
public class Student {  
  
    @XmlAttribute  
    private Integer id;  
  
    @XmlElement  
    private String name;  
  
    @XmlElement(name = "role")  
    private Role role;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Student [" + (this.id != null ? "id=" + this.id + ", " : "")
            + (this.name != null ? "name=" + this.name + ", " : "")
            + (this.role != null ? "role=" + this.role : "") + "]";
    }  
  
}  