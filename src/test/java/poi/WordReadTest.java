package poi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.leeo.poi.WordUtil;

/**
 * 读取word模板(只支持docx格式)，填充数据生成文档，支持表格、图片。
 *
 */
public class WordReadTest {

    public static void main(String[] args) throws InvalidFormatException {
        //mapAll的key为需要替换的字符，value为替换成什么
        Map<String, Object> mapAll = new HashMap<String, Object>();
        mapAll.put("replace1", "0000000000");
        mapAll.put("replace2", "Do the test!!");
        mapAll.put("replace3", "Test");
        mapAll.put("replace4", "2222222222222");
        
        //以下定义一个table（模板文件中需要有2行，第一行为header，第二行为数据替换），就是多条记录，但是table的name必须放在第一个栏位（即table的第一行第一列）
        //table的第二行如Test中的列子，必须是第一列为行号（不必管），第二列为<replace5>，第三列为<replace6>
        /**********table********************start****************************************/
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        //第一行数据
        Map<String, String> row1 = new HashMap<String, String>();
        row1.put("replace5", "-");
        row1.put("replace6", "WWWWWWWWWW");
        list.add(row1);
        
        //第二行数据
        Map<String, String> row2 = new HashMap<String, String>();
        row2.put("replace5", "");
        row2.put("replace6", "SSSSSSSSSS");
        list.add(row2);
        
        //第三行数据
        Map<String, String> row3 = new HashMap<String, String>();
        row3.put("replace5", "11111");
        row3.put("replace6", "ZZZZZZZZZZZ");
        list.add(row3);

        mapAll.put("Table No.1", list);//table name 为 Table No.1
        /*******table***********************end****************************************/
        //图片替换
        mapAll.put(WordUtil.IMAGE_ + "image",
                   "D:/workspace/test/src/test/java/poi/image1.png");
        try {
            WordUtil a = new WordUtil();
            a.generateWordFromTemplate("D:/workspace/test/src/test/java/poi/Template.doc",
                                     "D:/workspace/test/src/test/java/poi/test.doc",
                                     mapAll);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        

}
