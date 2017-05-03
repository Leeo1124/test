package com.leeo.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.google.gson.GsonBuilder;
import com.leeo.web.entity.User;
import com.leeo.websocket.MyWebSocketHandler;
import com.leeo.websocket.entity.Message;

@Controller
@RequestMapping("/msg")
public class MessageController {
    
	@Resource
	private MyWebSocketHandler handler;
	private Map<Long, User> users = new HashMap<>();
	public static final Map<String, String> map = new HashMap<>();
	static{
	    map.put("a", "test");
	}
	
	// 模拟一些数据
	@ModelAttribute
	public void setReqAndRes() {
		User u1 = new User();
		u1.setId(1L);
		u1.setUsername("admin");
//		u1.setRealName("管理员");
		this.users.put(u1.getId(), u1);
		User u2 = new User();
		u2.setId(2L);
		u2.setUsername("root");
//		u2.setRealName("系统管理员");
		this.users.put(u2.getId(), u2);
	}
	// 用户登录
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String doLogin(User user, HttpServletRequest request) {
		request.getSession().setAttribute("uid", user.getId());
		request.getSession().setAttribute("name",this.users.get(user.getId()).getUsername());
		return "redirect:talk";
	}

	// 跳转到交谈聊天页面
	@RequestMapping(value = "talk", method = RequestMethod.GET)
	public String talk() {
		return "default/talk";
	}

	// 跳转到发布广播页面
	@RequestMapping(value = "broadcast", method = RequestMethod.GET)
	public String broadcast() {
		return "default/broadcast";
	}

	// 发布系统广播（群发）
	@ResponseBody
	@RequestMapping(value = "broadcast", method = RequestMethod.POST)
	public void broadcast(String text) {
		Message msg = new Message();
		msg.setDate(new Date());
		msg.setFrom("system");
		msg.setFromName("系统广播");
		msg.setTo("admin");//(0L);
		msg.setText(text);
		this.handler.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
	}
}