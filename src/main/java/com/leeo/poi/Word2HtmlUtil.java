package com.leeo.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * poi完美word转html(表格、图片、样式),支持表格、图片
 * 
 * @author Leeo
 */
public class Word2HtmlUtil {

    private static Logger logger = LoggerFactory.getLogger(Word2HtmlUtil.class);

    private static final String IMAGE_PATH = Word2HtmlUtil.class.getResource(
        "/").getPath()
        + "openoffice/images/";

    /**
     * 将word文件转换为html
     * 
     * @param sourceFile
     *        源文件
     * @param destFile
     *        目标文件
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    @SuppressWarnings("resource")
    public static void toHtmlByDoc(String sourceFile, String destFile)
            throws TransformerException, IOException,
            ParserConfigurationException {
        long beginTime = System.currentTimeMillis();
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(
            sourceFile));//WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
            DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] content, PictureType pictureType,
                    String suggestedName, float widthInches, float heightInches) {
                File dir = new File(IMAGE_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                return IMAGE_PATH + suggestedName;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);
        //save pictures
        List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = pics.get(i);
                try {
                    pic.writeImageContent(new FileOutputStream(IMAGE_PATH
                        + pic.suggestFullFileName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        FileUtils.writeByteArrayToFile(new File(destFile), out.toByteArray());
        long endTime = System.currentTimeMillis();
        logger.info(sourceFile + "文档转换结束,共耗时：" + (endTime - beginTime) / 100
            + "s");
    }

    /**
     * 将word文件转换为html(表格样式和序号有问题)
     * 
     * @param sourceFile
     *        源文件
     * @param destFile
     *        目标文件
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    @SuppressWarnings("resource")
    public static void toHtmlByDocx(String sourceFile, String destFile) {
        long beginTime = System.currentTimeMillis();
        try {
            File dir = new File(IMAGE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFile));
            XHTMLOptions options = XHTMLOptions.create();

            //img的src属性 后面会自动添加/word/media
            //这里就是images/word/media/ + 图片名字
            options.URIResolver(new BasicURIResolver(IMAGE_PATH));
            //>> 文件的保存路径 之后自动会添加 word\media子路径
            FileImageExtractor extractor = new FileImageExtractor(new File(
                IMAGE_PATH));
            options.setExtractor(extractor);
            XHTMLConverter.getInstance().convert(document,
                new FileOutputStream(destFile), options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        logger.info(sourceFile + "文档转换结束,共耗时：" + (endTime - beginTime) / 100
            + "s");
    }
}
