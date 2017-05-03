package com.leeo.websocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能说明：TelSocketSessionUtils
 */
public class TelSocketSessionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelSocketSessionUtils.class);

    private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    /**
     * 保存一个连接
     * @param username
     * @param empNo
     * @param session
     */
    public static void add(String username, int empNo, WebSocketSession session){
        clients.put(getKey(username, empNo), session);
    }

    /**
     * 获取一个连接
     * @param username
     * @param empNo
     * @return
     */
    public static WebSocketSession get(String username, int empNo){
        return clients.get(getKey(username, empNo));
    }

    /**
     * 移除一个连接
     * @param username
     * @param empNo
     */
    public static void remove(String username, int empNo) {
        clients.remove(getKey(username, empNo));
    }

    /**
     * 组装sessionId
     * @param username
     * @param empNo
     * @return
     */
    public static String getKey(String username, int empNo) {
        return username + "_" + empNo;
    }

    /**
     * 判断是否有效连接
     * 判断是否存在
     * 判断连接是否开启
     * 无效的进行清除
     * @param username
     * @param empNo
     * @return
     */
    public static boolean hasConnection(String username, int empNo) {
        String key = getKey(username, empNo);
        if (clients.containsKey(key)) {
            return true;
        }

        return false;
    }

    /**
     * 获取连接数的数量
     * @return
     */
    public static int getSize() {
        return clients.size();
    }

    /**
     * 发送消息到客户端
     * @param username
     * @param empNo
     * @param message
     * @throws Exception
     */
    public static void sendMessage(String username, int empNo, String message) throws Exception {
        if (!hasConnection(username, empNo)) {
            throw new NullPointerException(getKey(username, empNo) + " connection does not exist");
        }

        WebSocketSession session = get(username, empNo);
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            LOGGER.error("websocket sendMessage exception: " + getKey(username, empNo));
            LOGGER.error(e.getMessage(), e);
            clients.remove(getKey(username, empNo));
        }
    }

}