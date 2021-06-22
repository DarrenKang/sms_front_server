<%@include file="/office/include.jsp"%>
<%@include file="/office/checkLogin.jsp"%>
<!DOCTYPE html>
<html>
<body>
	<meta charset="utf-8">
	<title>SMS API</title>
	<script type="text/javascript">
		window.onload = function() {
			localStorage.clear();
		}
	</script>
	<%
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + path + "/";
	%>

<body>
	welcome to SMS API
	<br>
	<br>
	<a href="<%=path%>/office/setSmsConfig.jsp">SMS Config</a>
	<br>
	<br>
	<a href="<%=path%>/office/viewSMS.do">SMS Message</a>
	<br>
	<br>
	<a href="<%=path%>/office/account.jsp">Modify Password</a>
	<br>
	<br>
	<a href="<%=path%>/office/viewSMSLog.do">SMSLog Message</a>
	<br>
	<br>
	<a href="<%=path%>/office/channelonesensitive.do">Channel one
		sensitive words config</a>
	<br>
	<br>
	<a href="<%=path%>/office/checkNexmoMessage.do">check nexmo Message</a>
	<br>
	<br>
	<a href="<%=path%>/office/configuringInformation.do">Configuring
		user information</a>
	<br>
	<br>
	<a href="<%=path%>/office/logout.do">logout</a>
	<c:import url="/office/script.jsp" />
</body>
</html>
