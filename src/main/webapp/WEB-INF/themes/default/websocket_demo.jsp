<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getServerName() + ":" + request.getServerPort() + path + "/";
	String basePath2 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setAttribute("ctx", path);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
#connect-container {
	float: left;
	width: 400px
}

#connect-container div {
	padding: 5px;
}

#console-container {
	float: left;
	margin-left: 15px;
	width: 400px;
}

#console {
	border: 1px solid #CCCCCC;
	border-right-color: #999999;
	border-bottom-color: #999999;
	height: 170px;
	overflow-y: scroll;
	padding: 5px;
	width: 100%;
}

#console p {
	padding: 0;
	margin: 0;
}
</style>

<script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>
<script src="${ctx}/static/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
	var path = '<%=basePath%>';
	var websocket = null;
	var url = null;
	var transports = [];
	
	function setConnected(connected) {
		document.getElementById('connect').disabled = connected;
		document.getElementById('disconnect').disabled = !connected;
		document.getElementById('echo').disabled = !connected;
	}
	
	function connect() {
		alert(url);
		if (!url) {
			alert('Select whether to use W3C WebSocket or SockJS');
			return;
		}
		websocket = (url.indexOf('sockjs') != -1) ? new SockJS(url, undefined, { protocols_whitelist : transports }) : new WebSocket(url);
		
		websocket.onopen = function(event) {
			setConnected(true);
			log('Info: connection opened.');
			
			console.log("WebSocket:已连接");
			console.log(event);
		};
		
		websocket.onmessage = function(event) {
			log('Received: ' + event.data);
			
			console.log("WebSocket:收到一条消息",event.data);
			alert(event.data);
		};
		
		websocket.onerror = function(event) {
			console.log("WebSocket:发生错误 ");
			console.log(event);
		};
		
		websocket.onclose = function(event) {
			setConnected(false);
			log('Info: connection closed.');
			log(event);
			
			console.log("WebSocket:已关闭");
			console.log(event);
		};
	}
	
	function disconnect() {
		if (websocket != null) {
			websocket.close();
			websocket = null;
		}
		setConnected(false);
	}
	
	function echo() {
		if (websocket != null) {
			var message = document.getElementById('message').value;
			log('Sent: ' + message);
			websocket.send(message);
		} else {
			alert('connection not established, please connect.');
		}
	}
	function updateUrl(urlPath) {
		var username = '${sessionScope.user.username}';
		var empNo = 123;
		if (urlPath.indexOf('sockjs') != -1) {
			url = urlPath;
			document.getElementById('sockJsTransportSelect').style.visibility = 'visible';
		} else {
			if (window.location.protocol == 'http:') {
				url = 'ws://' + window.location.host + urlPath + "?username="+username+"&empNo="+empNo;
			} else {
				url = 'wss://' + window.location.host + urlPath + "?username="+username+"&empNo="+empNo;
			}
			document.getElementById('sockJsTransportSelect').style.visibility = 'hidden';
		}
	}
	function updateTransport(transport) {
		alert(transport);
		transports = (transport == 'all') ? [] : [
			transport
		];
	}
	function log(message) {
		var console = document.getElementById('console');
		var p = document.createElement('p');
		p.style.wordWrap = 'break-word';
		p.appendChild(document.createTextNode(message));
		console.appendChild(p);
		while (console.childNodes.length > 25) {
			console.removeChild(console.firstChild);
		}
		console.scrollTop = console.scrollHeight;
	}
</script>
</head>
<body>
	<p>
		<a href="${ctx}/websocket/sendMessage/${sessionScope.user.username}/123" target="_blank">给客户端推送消息</a><br /><br />
	</p>

	<br />
	<br />
	<noscript>
		<h2 style="color: #ff0000">Seems your browser doesn't support
			Javascript! Websockets rely on Javascript being enabled. Please
			enable Javascript and reload this page!</h2>
	</noscript>
	<div>
		<div id="connect-container">
			<input id="radio1" type="radio" name="group1" onclick="updateUrl('/test/websocket');">
			<label for="radio1">W3C WebSocket</label> <br> 
			<input id="radio2" type="radio" name="group1" onclick="updateUrl('/test/websocket');">
			<label for="radio2">SockJS</label>
			<div id="sockJsTransportSelect" style="visibility: hidden;">
				<span>SockJS transport:</span> <select
					onchange="updateTransport(this.value)">
					<option value="all">all</option>
					<option value="websocket">websocket</option>
					<option value="xhr-polling">xhr-polling</option>
					<option value="jsonp-polling">jsonp-polling</option>
					<option value="xhr-streaming">xhr-streaming</option>
					<option value="iframe-eventsource">iframe-eventsource</option>
					<option value="iframe-htmlfile">iframe-htmlfile</option>
				</select>
			</div>
			<div>
				<button id="connect" onclick="connect();">Connect</button>
				<button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
			</div>
			<div>
				<textarea id="message" style="width: 350px">Here is a message!</textarea>
			</div>
			<div>
				<button id="echo" onclick="echo();" disabled="disabled">Echo
					message</button>
			</div>
		</div>
		<div id="console-container">
			<div id="console"></div>
		</div>
	</div>
</body>
</html>