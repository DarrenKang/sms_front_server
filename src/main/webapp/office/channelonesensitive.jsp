<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp" %>
<%@include file="/office/checkLogin.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript"  src="<c:url value='/js/jquery-1.5.1.min.js'/>"></script>

    <%
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+path+"/";
    %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通道一广告词汇总结</title>
 <link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css"/>
 <script type="text/javascript" src="<c:url value="/js/jquery-1.5.1.min.js"/>"></script>
 <script type="text/javascript" src="<c:url value="/js/jquery-ui-1.8.13.custom.min.js"/>"></script>
</head>
<body>
<a href="<%=path%>/office/menu.jsp">Menu</a>
<br/>
<br/>
	通道一广告词汇总结:<font style="color:red">所加词语请用英文逗号分隔</font><br><br>
	<form action="<c:url value='/office/updateAds.do'/>" method="post">
		<textarea rows="10" cols="150" id="filerads" name="advertise">${ads }</textarea>
		<input type="submit" value="提交"/>
	</form>
	<br><br>
	&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    <a href="<%=path%>/office/logout.do">logout</a>
</body>
</html>