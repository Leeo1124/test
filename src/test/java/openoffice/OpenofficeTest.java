package openoffice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Date;
import java.util.regex.Pattern;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class OpenofficeTest {

    private static Logger logger = LoggerFactory
        .getLogger(OpenofficeTest.class);
    
    public static void test(){
        String openofficeHome = "/opt/openoffice4/program/"
                + "soffice -headless -accept=\"socket,host=127.0.0.1,port=8100:urp;\" -nofirststartwizard &";
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(
                "192.168.34.129", 8100);
            // 创建Openoffice连接
            try {
                // 连接
                connection.connect();
            } catch (Exception e) {
                e.printStackTrace();

                try {
                    //启动监听命令
//                    Runtime.getRuntime().exec(openofficeHome);
                    connection.connect();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            // 创建转换器
            DocumentConverter converter = new OpenOfficeDocumentConverter(
                connection);
            // 转换文档问html
            converter
                .convert(
                    new File(
                        "D:/workspace/test/src/main/resources/rules/ICBCAccreditConvertService.docx"),
                    new File("D:/workspace/test/src/test/java/openoffice/aa.html"));
            // 关闭openoffice连接
            connection.disconnect();
    }

    public static void main(String[] args) {
        Date startDate = new Date();  
        String sourceFile = "D:/workspace/test/src/test/java/openoffice/aaaa.doc";  
        String destFile = "D:/workspace/test/src/test/java/openoffice/change2.pdf";  
        System.out.println(office2PDF(sourceFile, destFile));  
        Date endDate = new Date();
        System.out.println("the cost time is "+(endDate.getTime()-startDate.getTime()));  
    }

    /**  
     * 将Office文档转换为PDF. 运行该函数需要用到OpenOffice, OpenOffice下载地址为  
     * http://www.openoffice.org/  
     * @param sourceFile  
     *            源文件, 绝对路径. 可以是Office2003-2007全部格式的文档, Office2010的没测试. 包括.doc,  
     *            .docx, .xls, .xlsx, .ppt, .pptx等. 示例: F:\\office\\source.doc  
     * @param destFile  
     *            目标文件. 绝对路径. 示例: F:\\pdf\\dest.pdf  
     * @return 操作成功与否的提示信息. 如果返回 -1, 表示找不到源文件, 或url.properties配置错误; 如果返回 0,  
     *         则表示操作成功; 返回1, 则表示转换失败  
     */    
    public static int office2PDF(String sourceFile, String destFile) {  
        Process pro = null;  
        OpenOfficeConnection connection = null;  
        try {  
            File inputFile = new File(sourceFile);  
            if (!inputFile.exists()) {  
                return -1;//文件不存在  
            }  
  
            //文件夹不存在创建目录  
            File outputFile = new File(destFile);  
            if (!outputFile.getParentFile().exists()) {  
                outputFile.getParentFile().mkdirs();  
            }  
  
            String OpenOffice_HOME = "/opt/openoffice4/program/";//"F:\\tool\\OpenOffice4";  
            if (OpenOffice_HOME.charAt(OpenOffice_HOME.length() - 1) != '\\') {  
                OpenOffice_HOME += "\\";  
            }  
            // 启动OpenOffice的服务    
            String command = OpenOffice_HOME
                    + "soffice -headless -accept=\"socket,host=127.0.0.1,port=8100:urp;\" -nofirststartwizard &";
                    //+ "program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";  
            pro = Runtime.getRuntime().exec(command);
            // connect to an OpenOffice.org instance running on port 8100  
            connection = new SocketOpenOfficeConnection("192.168.34.129", 8100);  
            connection.connect();
  
            // convert  
            DocumentConverter converter = new OpenOfficeDocumentConverter(  
                    connection);  
            converter.convert(inputFile, outputFile);  
  
            return 0;  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            return -1;  
        } catch (ConnectException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            if(connection!=null){  
                // close the connection  
                connection.disconnect();  
            }  
             // 关闭OpenOffice服务的进程    
            if(pro!=null){  
                pro.destroy();  
            }  
        }  
  
        return 1;  
    } 

    /**
     * 将Office文档转换为PDF. 运行该函数需要用到OpenOffice, OpenOffice
     * 
     * @param sourceFile
     *        源文件,绝对路径. 可以是Office2003-2007全部格式的文档, Office2010的没测试. 包括.doc,
     *        .docx, .xls, .xlsx, .ppt, .pptx等.
     * @param destFile
     *        目标文件.绝对路径.
     */
    public static void word2pdf(String inputFilePath) {
        DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();

        String officeHome = getOfficeHome();
        config.setOfficeHome(officeHome);

        OfficeManager officeManager = config.buildOfficeManager();
        officeManager.start();

        OfficeDocumentConverter converter = new OfficeDocumentConverter(
            officeManager);
        String outputFilePath = getOutputFilePath(inputFilePath);
        File inputFile = new File(inputFilePath);
        if (inputFile.exists()) {// 找不到源文件, 则返回
            File outputFile = new File(outputFilePath);
            if (!outputFile.getParentFile().exists()) { // 假如目标路径不存在, 则新建该路径
                outputFile.getParentFile().mkdirs();
            }
            converter.convert(inputFile, outputFile);
        }

        officeManager.stop();
    }

    public static String getOutputFilePath(String inputFilePath) {
        String outputFilePath = inputFilePath.replaceAll(".doc", ".pdf");
        return outputFilePath;
    }

    public static String getOfficeHome() {
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return "/opt/openoffice.org3";
        } else if (Pattern.matches("Windows.*", osName)) {
            return "D:/Program Files/OpenOffice.org 3";
        } else if (Pattern.matches("Mac.*", osName)) {
            return "/Application/OpenOffice.org.app/Contents";
        }
        return null;
    }
}
