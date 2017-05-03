package jaxb;

import javax.xml.bind.JAXBException;

import jaxb.entity.Book;

import org.junit.Test;

import com.leeo.utils.JaxbUtil;

/**
 * 简单对象转换
 *
 * @XmlType，将Java类或枚举类型映射到XML模式类型
 * @XmlAccessorType(XmlAccessType.FIELD) ，控制字段或属性的序列化。
 *                                       FIELD表示JAXB将自动绑定Java类中的每个非静态的（static）、
 *                                       非瞬态的（由@XmlTransient标注）字段到XML。
 *                                       其他值还有XmlAccessType.
 *                                       PROPERTY和XmlAccessType.NONE。
 * @XmlAccessorOrder，控制JAXB 绑定类中属性和字段的排序。
 * @XmlJavaTypeAdapter，使用定制的适配器（即扩展抽象类XmlAdapter并覆盖marshal()和unmarshal()方法），以序列化Java类为XML。
 * @XmlElementWrapper ，对于数组或集合（即包含多个元素的成员变量），生成一个包装该数组或集合的XML元素（称为包装器）。
 * @XmlRootElement，将Java类或枚举类型映射到XML元素。
 * @XmlElement，将Java类的一个属性映射到与属性同名的一个XML元素。
 * @XmlAttribute，将Java类的一个属性映射到与属性同名的一个XML属性。
 * @XmlTransient用于标示在由java对象映射xml时，忽略此属性.
 * @XmlJavaTypeAdapter常用在转换比较复杂的对象时，如map类型或者格式化日期等.
 */
public class JaxbTest1 {

    /**
     * @throws JAXBException
     */
    @Test
    public void showMarshaller() {
        Book book = new Book();
        book.setId(100);
        book.setAuthor("");
//        book.setCalendar(new Date());
        book.setPrice(23.45f);   //默认是0.0

        String str = JaxbUtil.convertToXml(book);
        System.out.println(str);
    }

    /**
     * @throws JAXBException
     */
    @Test
    public void showUnMarshaller() {
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<book id=\"100\">"
            + "    <author>James</author>"
            + "   <calendar>2013-03-29T09:25:56.004+08:00</calendar>"
            + "  <price_1>23.45</price_1>" + "</book>";

        Book book = JaxbUtil.converyToJavaBean(str, Book.class);
        System.out.println(book);
    }

}