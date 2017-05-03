package poi;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

/**
 * 获取docx文件中的内容，包含文字、图片
 * @author Chengyuan Huang
 *
 */
public class GetPics {
    public static void main(String[] args) {
        String path ="D:/workspace/test/src/test/java/poi/Linux下openoffice安装步骤.docx";
        File file = new File(path);
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(file));
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(document);
            String text = xwpfWordExtractor.getText();
            System.out.println(text);
            List<XWPFPictureData> picList = document.getAllPictures();
            for (XWPFPictureData pic : picList) {
                System.out.println(pic.getPictureType() + File.separator + pic.suggestFileExtension()
                        +File.separator+pic.getFileName());
                byte[] bytev = pic.getData();
                FileOutputStream fos = new FileOutputStream("d:\\"+pic.getFileName()); 
                fos.write(bytev);
            }
            new FileInputStream(file).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}