package com.leeo.poi;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;

import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

public class WordUtil {

    public static final String IMAGE_ = "IMAGE_";
    private static final String EL_LEFT = "<";
    private static final String EL_RIGHT = ">";
    private XWPFDocument document = null;
    private List<String> noSNTable = null;

    /**
     * Read and write word by template
     * @param templatePath The path of the word template
     * @param outputPath The path of the result word file to write out
     * @param map The rule that is how to read and write the word for example:
     * e.g string value replace: The map's key is the text which is ready to
     *     repalce in the word and value of the map is a text insert into word.
     * e.g image insert: The map's key must start with 'IMAGE_' and value of
     *     the map must be put a path of a image.
     * e.g list value insert: The map's key is the name of a table in word.
     *     The value of the map must be a instance of calss List and the members
     *     of list is a instance of calss Map and this map as the string value replace map
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InvalidFormatException
     */
    public boolean generateWordFromTemplate(String templatePath,
                                            String outputPath,
                                            Map<String, Object> map) throws FileNotFoundException,
                                                                            IOException,
                                                                            InvalidFormatException {


        document = new XWPFDocument(POIXMLDocument.openPackage(templatePath));
        return readWordDocument(map, outputPath);
    }

    /**
     * Read and write word by template in inputStream
     * @param inputStream
     * @param outputPath
     * @param map
     * @return
     * @throws InvalidFormatException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean generateWordFromTemplate(java.io.InputStream inputStream,
                                            String outputPath,
                                            Map<String, Object> map) throws InvalidFormatException,
                                                                            FileNotFoundException,
                                                                            IOException {
        document = new XWPFDocument(inputStream);
        return readWordDocument(map, outputPath);
    }

    private boolean readWordDocument(Map<String, Object> map,
                                     String outputPath) throws InvalidFormatException,
                                                               FileNotFoundException,
                                                               IOException {
        // replace the text of header
        if (document.getHeaderList() != null &&
            document.getHeaderList().size() > 0) {
            XWPFHeader header = document.getHeaderArray(0);
            List<XWPFParagraph> listHeader = header.getParagraphs();
            for (XWPFParagraph paragraph : listHeader) {
                replaceWordText(paragraph, map);
            }
        }
        // replace the text of body
        Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
        while (itPara.hasNext()) {
            XWPFParagraph paragraph = itPara.next();
            replaceWordText(paragraph, map);
            //                wordReadRule.addPictureInWord(document);
        }
        // replace the text of table
        Iterator<XWPFTable> itTable = document.getTablesIterator();
        while (itTable.hasNext()) {
            XWPFTable table = itTable.next();
            readWordTableRangeRule(table, map);

        }
        FileOutputStream fos = new FileOutputStream(new File(outputPath));
        document.write(fos);
        fos.flush();
        fos.close();
        return true;
    }

    /**
     * Read Word Table Range Rule
     * @param table
     * @param mapProperties
     */
    private void readWordTableRangeRule(XWPFTable table,
                                        Map<String, Object> mapProperties) {
        List<Map<String, String>> hasCellContant =
            new ArrayList<Map<String, String>>();
        int rcount = table.getNumberOfRows();
        boolean isListValue = false;
        boolean noSN = false;
        for (int i = 0; i < rcount; i++) {
            XWPFTableRow row = table.getRow(i);
            List<XWPFTableCell> cells = row.getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                XWPFTableCell cell = cells.get(j);
                String cellTextString = cell.getText();
                for (Map.Entry<String, Object> entry :
                     mapProperties.entrySet()) {
                    String paramName = EL_LEFT + entry.getKey() + EL_RIGHT;
                    if (cellTextString.contains(paramName)) {
                        String replaceText = "";
                        if (entry.getValue() instanceof List) {
                            if (getNoSNTable().contains(entry.getKey())) {
                                noSN = true;
                            }
                            List<Map<String, String>> list =
                                (List<Map<String, String>>)entry.getValue();
                            hasCellContant.addAll(list);
                            isListValue = true;
                        } else {
                            if (null != entry.getValue()) {
                                replaceText = String.valueOf(entry.getValue());
                            }
                        }
                        repalceText(cell, paramName, replaceText);
                        if (isListValue)
                            break;
                    }
                }
                if (isListValue)
                    break;
            }
            if (isListValue)
                break;
        }
        if (isListValue)
            addListValueInTable(hasCellContant, table, noSN);
    }

    private void addListValueInTable(List<Map<String, String>> hasCellContant,
                                     XWPFTable table, boolean noSN) {
        //add text of list values in table
        Map<Integer, List<String>> insertTextIndex =
            new HashMap<Integer, List<String>>();
        if (hasCellContant.size() > 0) {
            for (int i = 0; i < hasCellContant.size(); i++) {
                XWPFTableRow row = null;
                List<XWPFTableCell> cells = null;
                Map<String, String> map = hasCellContant.get(i);
                if (i == 0) {
                    row = table.getRow(1);
                } else {
                    row = table.createRow();
                }
                cells = row.getTableCells();
                for (int j = 0; j < cells.size(); j++) {
                    XWPFTableCell cell = cells.get(j);
                    String text = cell.getText();
                    if (j == 0 && i > 0) {
                        if (!noSN) {
                            text = String.valueOf(i + 1);
                        }
                    } else if (j != 0) {
                        List<String> listParamNames = new ArrayList<String>();
                        for (Map.Entry<String, String> entry :
                             map.entrySet()) {
                            String paramName =
                                EL_LEFT + entry.getKey() + EL_RIGHT;
                            if (i == 0) {
                                if (text.contains(paramName)) {
                                    text =
text.replace(paramName, entry.getValue());
                                    listParamNames.add(entry.getKey());
                                }
                            } else {
                                listParamNames = insertTextIndex.get(j);
                                for (String name : listParamNames) {
                                    if (name.equals(entry.getKey())) {
                                        text += entry.getValue();
                                    }
                                }
                            }
                        }
                        if (i == 0) {
                            insertTextIndex.put(j, listParamNames);
                        }
                    }
                    if (i == 0 && j != 0) {
                        cell.removeParagraph(0);
                    }
                    if (!(i == 0 && j == 0)) {
                        cell.setText(text);
                    }
                }
            }
        }
    }

    /**
     * replace text and images
     * @param paragraph
     * @param mapProperties
     * @throws InvalidFormatException
     * @throws FileNotFoundException
     */
    private void replaceWordText(XWPFParagraph paragraph,
                                 Map<String, Object> mapProperties) throws InvalidFormatException,
                                                                           FileNotFoundException {
        List<XWPFRun> runs = paragraph.getRuns();
        for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(i);
            String oneparaString = run.getText(run.getTextPosition());
            if (null != oneparaString) {
                for (Map.Entry<String, Object> entry :
                     mapProperties.entrySet()) {
                    boolean isImage = entry.getKey().startsWith(IMAGE_);
                    String paramName = EL_LEFT + entry.getKey() + EL_RIGHT;
                    if (!isImage) {
                        if (oneparaString.contains(paramName)) {
                            String replaceText = "";
                            if (null != entry.getValue()) {
                                replaceText = String.valueOf(entry.getValue());
                            }
                            oneparaString =
                                    oneparaString.replace(paramName, replaceText);
                        }

                    } else if (isImage) {
                        if (oneparaString.contains(paramName.replace(IMAGE_,
                                                                     ""))) {
                            oneparaString = "";
                            CTInline inline =
                                run.getCTR().addNewDrawing().addNewInline();
                            insertPicture(document,
                                          String.valueOf(entry.getValue()),
                                          inline, 100, 100);
                        }
                    }
                }
                run.setText(oneparaString, 0);
            }
        }
    }

    /**
     * insert Picture
     * @param document
     * @param filePath
     * @param inline
     * @param width
     * @param height
     * @throws InvalidFormatException
     * @throws FileNotFoundException
     */
    private void insertPicture(XWPFDocument document, String filePath,
                               CTInline inline, int width,
                               int height) throws InvalidFormatException,
                                                  FileNotFoundException {
        document.addPictureData(new FileInputStream(filePath),
                                XWPFDocument.PICTURE_TYPE_PNG);
        int id = document.getAllPictures().size() - 1;
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        String blipId =
            document.getAllPictures().get(id).getPackageRelationship().getId();
        String picXml = getPicXml(blipId, width, height);
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            xe.printStackTrace();
        }
        inline.set(xmlToken);
        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);
        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);
        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("IMG_" + id);
        docPr.setDescr("IMG_" + id);
    }

    private void repalceText(XWPFTableCell cell, String paramName,
                             String replaceText) {
        if (null != cell.getParagraphs()) {
            int lengthOld = cell.getParagraphs().size();
            for (int i = 0; i < lengthOld; i++) {
                XWPFParagraph xWPFParagraph = cell.getParagraphs().get(i);
                for (int j = 0; j < xWPFParagraph.getCTP().getRArray().length;
                     j++) {
                    CTR run = xWPFParagraph.getCTP().getRArray()[j];
                    for (int k = 0; k < run.getTArray().length; k++) {
                        CTText text = run.getTArray()[k];
                        String stringValue = text.getStringValue();
                        if (stringValue.contains(paramName)) {
                            stringValue =
                                    stringValue.replace(paramName, replaceText);
                            text.setStringValue(stringValue);
                        }
                    }
                }
            }
        }
    }

    /**
     * get the xml of the picture
     * @param blipId
     * @param width
     * @param height
     * @return
     */
    private String getPicXml(String blipId, int width, int height) {
        String picXml =
            "" + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
            "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
            "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
            "         <pic:nvPicPr>" + "            <pic:cNvPr id=\"" + 0 +
            "\" name=\"Generated\"/>" + "            <pic:cNvPicPr/>" +
            "         </pic:nvPicPr>" + "         <pic:blipFill>" +
            "            <a:blip r:embed=\"" + blipId +
            "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
            "            <a:stretch>" + "               <a:fillRect/>" +
            "            </a:stretch>" + "         </pic:blipFill>" +
            "         <pic:spPr>" + "            <a:xfrm>" +
            "               <a:off x=\"0\" y=\"0\"/>" +
            "               <a:ext cx=\"" + width + "\" cy=\"" + height +
            "\"/>" + "            </a:xfrm>" +
            "            <a:prstGeom prst=\"rect\">" +
            "               <a:avLst/>" + "            </a:prstGeom>" +
            "         </pic:spPr>" + "      </pic:pic>" +
            "   </a:graphicData>" + "</a:graphic>";
        return picXml;
    }

    public void setNoSNTable(List<String> noSNTable) {
        this.noSNTable = noSNTable;
    }

    public List<String> getNoSNTable() {
        if (null == noSNTable) {
            noSNTable = new ArrayList<String>();
        }
        return noSNTable;
    }
}
