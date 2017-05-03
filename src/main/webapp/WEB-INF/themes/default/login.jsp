<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setAttribute("ctx", request.getContextPath());
%>
<!DOCTYPE html >
<html>
<head>
<title>用户登录</title>
</head>
<body>
	<div style="margin-top: 200px;">
		<form action="${ctx}/login" method="post" style="text-align: center;">
			<table align="center">
				<tr>
					<td>username:</td>
					<td><input type="text" id="username" name="username" size="20"value="" /></td>
				</tr>
				<tr>
					<td>password:</td>
					<td><input type="password" id="password" name="password" size="20" value="" /></td>
				</tr>
				<tr><td colspan="2"></td></tr>
				<tr>
					<td colspan="2" align="center"><input type="checkbox" name="rememberMe" value="true" />下次自动登录</td>
				</tr>
				<tr><td colspan="2"></td></tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="login" />
						<input type="reset" value="reset" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>