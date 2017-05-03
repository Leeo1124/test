package log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log4j建议只使用四个级别，优先级从高到低分别是 ERROR、WARN、INFO、DEBUG
 */
public class LogTest {
    private static Logger logger = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {
        logger.error("error 任务'{}'被{}提交成功，批核建议为'{}'.", "aa", "bb", "cc");
        logger.warn("warn 任务'{}'被{}提交成功，批核建议为'{}'.", "aa", "bb", "cc");
        logger.info("info 任务'{}'被{}提交成功，批核建议为'{}'.", "aa", "bb", "cc");
        logger.debug("debug 任务'{}'被{}提交成功，批核建议为'{}'.", "aa", "bb", "cc");
        logger.trace("trace 任务'{}'被{}提交成功，批核建议为'{}'.", "aa", "bb", "cc"); 
    }
}
