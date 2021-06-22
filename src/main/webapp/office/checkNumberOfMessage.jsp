<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<%@include file="/office/checkLogin.jsp" %>
<%@page import="ph.sinonet.vg.live.model.enums.SmsConfigCode"%>
<%@page import="java.util.List"%>
<%@ page import="ph.sinonet.vg.live.dao.SmsDao" %>
<%@ page import="ph.sinonet.vg.live.dao.PlatformDao" %>
<%@ page import="ph.sinonet.vg.live.service.interfaces.PlatformService" %>
<%@ page import="ph.sinonet.vg.live.model.sms.SmsConfig" %>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript"  src="<c:url value='/js/jquery-1.5.1.min.js'/>"></script>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+path+"/";
    %>

    <title>check Number Of Message</title>
    <link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value="/js/jquery-1.5.1.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui-1.8.13.custom.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value='/My97DatePicker/WdatePicker.js' />"></script>

    <script type="text/javascript">
        
        function colorizeMenu(idx){
            document.getElementById(idx).style.backgroundColor = '#e4f2ff';
        }

        function submitForExcuted(btn,action,user){
            btn.disabled=true;
            window.location.href=action+"?name="+user;
        }
        <%
        List list=SmsConfigCode.getActiveSmsApi();
        if(list!=null && list.size()>0)
        {
        request.setAttribute("smsconfiglist",list);
        }

        PlatformService platformService = (PlatformService) ctx.getBean("platformService");
        request.setAttribute("platFormList", platformService.getPlatformList(null));
		 %>
    </script>
</head>
<body onLoad="javascript:colorizeMenu('newaccessid');">
<s:form method="post" id="deleteSmsConfig">
    <s:hidden name="name"></s:hidden>
</s:form>
<a href="<%=path%>/office/menu.jsp">Menu</a>
<br/>
<br/>
<br/>
<form action="<c:url value='/office/queryNumberOfMessage.do'/>" method="post">
    Account ID
    <s:textfield name="accountId" id="accountId"></s:textfield>
   <!-- <input type="text" name="name"/> --> 
    type
    <s:select list="%{#request.smsconfiglist}" listValue="text" listKey="code" emptyOption="true" name="type" id="type"></s:select>
        Platform ID
        <s:select list="%{#request.platFormList}" listKey="platformId" listValue="platformId" emptyOption="true" name="projectname" id="PlatformID"></s:select>
	开始时间:
    <s:textfield name="start" size="17" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="true" value="%{start}" />
    结束时间:
    <s:textfield name="end" size="17" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="true" value="%{end}" />
    <input type="submit" value="submit" />

    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   

</form>
查询发送短信条数为：<input name="checkNumber" id="checkNumber" value="${numberOfMessage }"/>
<br></br>
<br></br>
<br></br>
<form action="<c:url value='/office/querySumNumberOfMessage.do'/>" method="post">
        Platform ID
        <s:select list="%{#request.platFormList}" listKey="platformId" listValue="platformId" emptyOption="true" name="projectname" id="PlatformID"></s:select>
	 channel
	<s:select list="#{'1':' Channel 1','2':' Channel 2','3':' Channel 3','4':' Channel 4','5':' Channel 5','6':' Channel 6' }" name="channel" emptyOption="true" id="channel"></s:select>
	开始时间:
    <s:textfield name="start" size="17" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="true" value="%{start}" />
    结束时间:
    <s:textfield name="end" size="17" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="true" value="%{end}" />
    <input type="submit" value="submit" />

    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    

</form>

通道查询发送短信条数为：<input name="checkSumNumberOfMessage" id="checkSumNumberOfMessage" value="${sumNumberOfMessage }"/>
<br />
<br />
 <a href="<%=path%>/office/logout.do">logout</a>
<c:import url="/office/script.jsp" />
</body>
</html>
