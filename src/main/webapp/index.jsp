<%@include file="/office/include.jsp" %>
<!DOCTYPE html>
<html>


<body>
<meta charset="utf-8">
<title>SMS API</title>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+path+"/";
%>
<body>
welcome to SMS API
<form action="<c:url value='/office/login.do'/>" method="post"  id="mainform" name="mainform">
    <br>
    <br>
    loginname: <input type="text" name="smsUserId" required/>
    <br>
    <br>
    password: <input type="password" name="smsPassword" required/>
    <br>
    <br>
    <input name="" type="submit" value="login"/>
</form>


</body>
<c:import url="/office/script.jsp"/>

</html>
