package jaxb.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jaxb.NullCustomizer;

import org.eclipse.persistence.oxm.annotations.XmlCustomizer;

import com.leeo.utils.JaxbDateSerializer;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(name = "book", propOrder = { "author", "calendar", "price", "id" })
@XmlCustomizer(NullCustomizer.class)
//??为什么不起作用
public class Book {

    @XmlAttribute
    private Integer id;

    @XmlElement(required = true, nillable = true)
    //??为什么不起作用
//    @XmlNullPolicy(emptyNodeRepresentsNull = true, nullRepresentationForXml = XmlMarshalNullRepresentation.EMPTY_NODE)
    private String author;

    @XmlElement(name = "price_1", required = true)
    private float price;

    @XmlElement(nillable = true)
    @XmlJavaTypeAdapter(JaxbDateSerializer.class)
    //日期格式化
    private Date calendar;

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getCalendar() {
        return this.calendar;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book [" + (this.author != null ? "author=" + this.author + ", " : "")
            + "price=" + this.price + ", "
            + (this.calendar != null ? "calendar=" + this.calendar + ", " : "")
            + (this.id != null ? "id=" + this.id : "") + "]";
    }
}