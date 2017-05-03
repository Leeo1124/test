package com.leeo.web.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.leeo.websocket.TelSocketSessionUtils;

@Controller
@RequestMapping("/websocket")
public class WebsocketController {

    private static final Logger logger = LoggerFactory
        .getLogger(WebsocketController.class);
    
    @RequestMapping(value = "toWebsocket", method = RequestMethod.GET)
    public String toWebsocket() {
        return "default/websocket_demo";
    }

    @RequestMapping(value="/sendMessage/{username}/{empNo}", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String sendMessage(@PathVariable String username, @PathVariable int empNo) throws IOException {
        String message = "hello I am Websocket Server";
        //无关代码都省略了
        if (TelSocketSessionUtils.hasConnection(username, empNo)) {
//            TelSocketSessionUtils.get(username, empNo).getAsyncRemote().sendText(message);
            TelSocketSessionUtils.get(username, empNo).sendMessage(
                new TextMessage(message));
            logger.info("使用Websocket给客户端推送消息." + message);
        } else {
            throw new NullPointerException(TelSocketSessionUtils.getKey(
                username, empNo) + " Connection does not exist");
        }

        return "success";
    }
}