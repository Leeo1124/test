<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
	String basePath2 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setAttribute("ctx", path);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>websocket测试通过</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
<script src="${ctx}/static/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
	var path = '<%=basePath%>';
	var username = '${sessionScope.user.username eq null?-1:sessionScope.user.username}';
	if(username == -1){
		location.href="<%=basePath2%>";
	}
	
	var tryTime = 0;
	var websocket;
	$(function () {
	    initSocket();
	    window.onbeforeunload = function () {
	        //离开页面时的其他操作
	    };
	});
	
	function initSocket() {
		if ('WebSocket' in window) {
			websocket = new WebSocket("ws://" + path + "/ws?username="+username);
		} else if ('MozWebSocket' in window) {
			websocket = new MozWebSocket("ws://" + path + "/ws"+username);
		} else {
			websocket = new SockJS("http://" + path + "/ws/sockjs"+username);
		}
		
		websocket.onopen = function(event) {
			console.log("WebSocket:已连接");
			console.log(event);
		};
		
		websocket.onmessage = function(event) {
			var data=JSON.parse(event.data);
			console.log("WebSocket:收到一条消息",data);
			alert(data.text);
		};
		
		websocket.onerror = function(event) {
			console.log("WebSocket:发生错误 ");
			console.log(event);
		};
		
		websocket.onclose = function(event) {
			console.log("WebSocket:已关闭");
			console.log(event);
			// 断线重连，重试10次，每次之间间隔10秒
		    if (tryTime < 10) {
		        setTimeout(function () {
		            webSocket = null;
		            tryTime++;
		            initSocket();
		        }, 500);
		    } else {
		        tryTime = 0;
		    }
		};
	}
</script>
</head>
<body>
	<p>
		<a href="${ctx}/logout">Logout</a><br /><br />
		<a href="${ctx}/msg/talk" target="_blank">talk</a><br />
		<a href="${ctx}/msg/broadcast" target="_blank">broadcast</a>
	</p>
	welcome!${sessionScope.user.username }

	<br />
	<br />
</body>
</html>