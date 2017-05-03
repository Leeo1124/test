package com.leeo.email;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class SendMailUtil {

    private static final Logger logger = LoggerFactory.getLogger(SendMailUtil.class);

    private static final String from = "123@qq.com";//ResourceUtil.getConfigByName("emailUserName");
    private static final String fromName = "123";
    private static final String charSet = "utf-8";
    private static final String username = "123@qq.com";//ResourceUtil.getConfigByName("emailUserName");
    private static final String password = "123";//ResourceUtil.getConfigByName("emailPassword");
    private static Map<String, String> hostMap = new HashMap<>();
    static {
        // 126
        SendMailUtil.hostMap.put("smtp.126", "smtp.126.com");
        
        // qq
        SendMailUtil.hostMap.put("smtp.qq", "smtp.qq.com");

        // 163
        SendMailUtil.hostMap.put("smtp.163", "smtp.163.com");

        // sina
        SendMailUtil.hostMap.put("smtp.sina", "smtp.sina.com.cn");

        // tom
        SendMailUtil.hostMap.put("smtp.tom", "smtp.tom.com");

        // 263
        SendMailUtil.hostMap.put("smtp.263", "smtp.263.net");

        // yahoo
        SendMailUtil.hostMap.put("smtp.yahoo", "smtp.mail.yahoo.com");

        // hotmail
        SendMailUtil.hostMap.put("smtp.hotmail", "smtp.live.com");

        // gmail
        SendMailUtil.hostMap.put("smtp.gmail", "smtp.gmail.com");
        SendMailUtil.hostMap.put("smtp.port.gmail", "465");
    }

    public static String getHost(String email) throws Exception {
        final Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
        final Matcher matcher = pattern.matcher(email);
        String key = "unSupportEmail";
        if (matcher.find()) {
            key = "smtp." + matcher.group(1);
        }
        if (SendMailUtil.hostMap.containsKey(key)) {
            return SendMailUtil.hostMap.get(key);
        } else {
            throw new Exception("unSupportEmail");
        }
    }

    public static int getSmtpPort(String email) throws Exception {
        final Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
        final Matcher matcher = pattern.matcher(email);
        String key = "unSupportEmail";
        if (matcher.find()) {
            key = "smtp.port." + matcher.group(1);
        }
        if (SendMailUtil.hostMap.containsKey(key)) {
            return Integer.parseInt(SendMailUtil.hostMap.get(key));
        } else {
            return 25;
        }
    }

    /**
     * 发送模板邮件
     *
     * @param toMailAddrs
     *        收信人地址
     * @param subject
     *        email主题
     * @param templatePath
     *        模板地址
     * @param map
     *        模板map
     */
    public static void sendFtlMail(String[] toMailAddrs, String subject,
            String templatePath, Map<String, Object> map) {
        Template template = null;
        Configuration freeMarkerConfig = null;
        final HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(SendMailUtil.getHost(SendMailUtil.from));
            hemail.setSmtpPort(SendMailUtil.getSmtpPort(SendMailUtil.from));
            hemail.setCharset(SendMailUtil.charSet);
            for (String email : toMailAddrs) {
                hemail.addTo(email);
            }
            hemail.setFrom(SendMailUtil.from, SendMailUtil.fromName);
            hemail.setAuthentication(SendMailUtil.username,
                SendMailUtil.password);
            hemail.setSubject(subject);
            // 添加附件
            hemail.attach(new File(SendMailUtil.getFilePath()+"/email.ftl"));
            freeMarkerConfig = new Configuration();
            freeMarkerConfig.setDirectoryForTemplateLoading(new File(
                SendMailUtil.getFilePath()));
            // 获取模板
            template = freeMarkerConfig.getTemplate(
                SendMailUtil.getFileName(templatePath), new Locale("Zh_cn"),
                "UTF-8");
            // 模板内容转换为string
            final String htmlText = FreeMarkerTemplateUtils
                .processTemplateIntoString(template, map);
            logger.info(htmlText);
            hemail.setMsg(htmlText);
            hemail.send();
            logger.info("email send true!");
        } catch (final Exception e) {
            e.printStackTrace();
            logger.info("email send error!");
        }
    }
    
    /**
     * 发送带附件的邮件
     * 
     * @param toMailAddr
     *        收信人地址
     * @param subject
     *        email主题
     * @param message
     *        发送email信息
     * @param attach
     *        附件来源
     */
    public static void sendAttachmentEmail(String[] toMailAddrs,
            String subject, String message, DataSource attach) {
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(SendMailUtil.getHost(SendMailUtil.from));
            hemail.setSmtpPort(SendMailUtil.getSmtpPort(SendMailUtil.from));
            hemail.setCharset(SendMailUtil.charSet);
            for (String email : toMailAddrs) {
                hemail.addTo(email);
            }
            hemail.setFrom(SendMailUtil.from, SendMailUtil.fromName);
            hemail.setAuthentication(SendMailUtil.username,
                SendMailUtil.password);
            hemail.setSubject(subject);
            hemail.setMsg(message);
            hemail.attach(attach, attach.getName(), attach.getName());
            hemail.send();
            logger.info("email send true!");
        } catch (final Exception e) {
            logger.info("email send error!"+e);
        }
    }

    /**
     * 发送普通邮件
     *
     * @param toMailAddr
     *        收信人地址
     * @param subject
     *        email主题
     * @param message
     *        发送email信息
     */
    public static void sendCommonMail(String[] toMailAddrs, String subject,
            String message) {
        final HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(SendMailUtil.getHost(SendMailUtil.from));
            hemail.setSmtpPort(SendMailUtil.getSmtpPort(SendMailUtil.from));
            hemail.setCharset(SendMailUtil.charSet);
            for (String email : toMailAddrs) {
                hemail.addTo(email);
            }
            hemail.setFrom(SendMailUtil.from, SendMailUtil.fromName);
            hemail.setAuthentication(SendMailUtil.username,
                SendMailUtil.password);
            hemail.setSubject(subject);
            hemail.setMsg(message);
            hemail.send();
            logger.info("email send true!");
        } catch (final Exception e) {
            logger.info("email send error!"+e);
        }
    }

    public static String getHtmlText(String templatePath,
            Map<String, Object> map) {
        Template template = null;
        String htmlText = "";
        try {
            Configuration freeMarkerConfig = null;
            freeMarkerConfig = new Configuration();
            freeMarkerConfig.setDirectoryForTemplateLoading(new File(
                SendMailUtil.getFilePath()));
            // 获取模板
            template = freeMarkerConfig.getTemplate(
                SendMailUtil.getFileName(templatePath), new Locale("Zh_cn"),
                "UTF-8");
            // 模板内容转换为string
            htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(
                template, map);
            logger.info(htmlText);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return htmlText;
    }

    private static String getFilePath() {
        String path = SendMailUtil.getAppPath(SendMailUtil.class);
        path = path + File.separator + "ftl" + File.separator;
        path = path.replace("\\", "/");
        logger.info(path);
        return path;
    }

    private static String getFileName(String path) {
        path = path.replace("\\", "/");
        logger.info(path);
        return path.substring(path.lastIndexOf("/") + 1);
    }

    @SuppressWarnings("unchecked")
    public static String getAppPath(Class<?> cls) {
        // 检查用户传入的参数是否为空
        if (cls == null) {
            throw new java.lang.IllegalArgumentException("参数不能为空！");
        }
        final ClassLoader loader = cls.getClassLoader();
        // 获得类的全名，包括包名
        String clsName = cls.getName() + ".class";
        // 获得传入参数所在的包
        final Package pack = cls.getPackage();
        String path = "";
        // 如果不是匿名包，将包名转化为路径
        if (pack != null) {
            final String packName = pack.getName();
            // 此处简单判定是否是Java基础类库，防止用户传入JDK内置的类库
            if (packName.startsWith("java.") || packName.startsWith("javax.")) {
                throw new java.lang.IllegalArgumentException("不要传送系统类！");
            }
            // 在类的名称中，去掉包名的部分，获得类的文件名
            clsName = clsName.substring(packName.length() + 1);
            // 判定包名是否是简单包名，如果是，则直接将包名转换为路径，
            if (packName.indexOf(".") < 0) {
                path = packName + "/";
            } else {// 否则按照包名的组成部分，将包名转换为路径
                int start = 0, end = 0;
                end = packName.indexOf(".");
                while (end != -1) {
                    path = path + packName.substring(start, end) + "/";
                    start = end + 1;
                    end = packName.indexOf(".", start);
                }
                path = path + packName.substring(start) + "/";
            }
        }
        // 调用ClassLoader的getResource方法，传入包含路径信息的类文件名
        final java.net.URL url = loader.getResource(path + clsName);
        // 从URL对象中获取路径信息
        String realPath = url.getPath();
        // 去掉路径信息中的协议名"file:"
        int pos = realPath.indexOf("file:");
        if (pos > -1) {
            realPath = realPath.substring(pos + 5);
        }
        // 去掉路径信息最后包含类文件信息的部分，得到类所在的路径
        pos = realPath.indexOf(path + clsName);
        realPath = realPath.substring(0, pos - 1);
        // 如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
        if (realPath.endsWith("!")) {
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
        }
        /*------------------------------------------------------------
         ClassLoader的getResource方法使用了utf-8对路径信息进行了编码，当路径
                          中存在中文和空格时，他会对这些字符进行转换，这样，得到的往往不是我们想要
                          的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的
                          中文及空格路径
        -------------------------------------------------------------*/
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("realPath----->" + realPath);
        return realPath;
    }

    public static void main(String[] args) {
        String[] emailAddrs = {"test@qq.com"};
        String subject = "sendemail test!";
        String content = "<html><body><font size='15' color='red'>This is a test mail.</font></body></html>";

//        final Map<String, Object> map = new HashMap<>();
//        map.put("subject", "测试标题");
//        map.put("content", "测试 内容");
//        final String templatePath = "ftl/email.ftl";
//        SendMailUtil.sendFtlMail(emailAddrs, subject, templatePath, map);
        
//        SendMailUtil.sendCommonMail(emailAddrs, subject, content);
    
        DataSource DataSource = new FileDataSource(SendMailUtil.getFilePath()+"/email.ftl");
        SendMailUtil.sendAttachmentEmail(emailAddrs, subject, content, DataSource);
    }
}