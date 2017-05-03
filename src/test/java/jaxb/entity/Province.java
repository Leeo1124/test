package jaxb.entity;
 import javax.xml.bind.annotation.XmlAccessType;  
import javax.xml.bind.annotation.XmlAccessorType;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlType;  
  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(propOrder = { "name", "provCity" })  
public class Province {  
  
    @XmlElement(name = "province_name")  
    private String name;  
  
    @XmlElement(name = "prov_city")  
    private String provCity;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvCity() {
        return this.provCity;
    }

    public void setProvCity(String provCity) {
        this.provCity = provCity;
    }

    @Override
    public String toString() {
        return "Province [" + (this.name != null ? "name=" + this.name + ", " : "")
            + (this.provCity != null ? "provCity=" + this.provCity : "") + "]";
    }  
  
}  