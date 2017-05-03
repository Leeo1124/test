package poi;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import com.leeo.poi.OfficeTool;
import com.leeo.poi.Word2HtmlUtil;

public class POITest {

    public static void main(String argv[]) {
        try {
            //word2003
//            Word2HtmlUtil.toHtmlByDoc("D:/workspace/test/src/test/java/poi/aaaa.doc","D:/workspace/test/src/test/java/poi/aaaa.html");
//            Word2HtmlUtil.toHtmlByDoc(
//                "D:/workspace/test/src/test/java/poi/Linux下openoffice安装步骤.doc",
//                "D:/workspace/test/src/test/java/poi/bbbb.html");
//            Word2HtmlUtil.toHtmlByDoc(
//                "D:/workspace/test/src/test/java/poi/信审系统配置文件修改文档.doc",
//                "D:/workspace/test/src/test/java/poi/ccc.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //word2007
//        Word2HtmlUtil.toHtmlByDocx(
//            "D:/workspace/test/src/test/java/poi/信审系统配置文件修改文档.docx",
//                "D:/workspace/test/src/test/java/poi/ddd.html");
        Word2HtmlUtil.toHtmlByDocx(
            "D:/workspace/test/src/test/java/poi/Linux下openoffice安装步骤.docx",
            "D:/workspace/test/src/test/java/poi/eee.html");
        
        
        
//        OfficeTool office = new OfficeTool(new File("D:/workspace/test/src/test/java/poi/信审系统配置文件修改文档.docx"));
//        String str = office.get07Html();
//        System.out.println(str);
//        System.out.println(office.save07AsHtml(office.get07Html()));
        
        //图片显示不了
//        OfficeTool office = new OfficeTool(new File("D:/workspace/test/src/test/java/poi/Linux下openoffice安装步骤.docx"));
//        String str = office.get07Html();
//        System.out.println(str);
//        System.out.println(office.save07AsHtml(office.get07Html()));
        
//        OfficeTool office = new OfficeTool(new File("D:/workspace/test/src/test/java/poi/Linux下openoffice安装步骤.doc"));
//        String str = office.get03Html();
//        System.out.println(str);
//        System.out.println(office.save03AsHtml(office.get03Html()));
        
        //有问题
        OfficeTool office = new OfficeTool(new File("D:/workspace/test/src/test/java/poi/演示文稿1.pptx"));
        String str = office.getPPTHtml();
        System.out.println(str);
        try {
            office.convert("D:/workspace/test/src/test/java/poi/演示文稿1.pptx", "D:/workspace/test/src/test/java/poi/演示文稿1.html");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    
    }
}
