<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="/office/include.jsp" %>
<%@include file="/office/checkLogin.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+path+"/";
    %>
    <title>SMSLogs List</title>
    <link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.5.1.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui-1.8.13.custom.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value='/My97DatePicker/WdatePicker.js' />"></script>
    <script type="text/javascript">
        function gopage(val) {
            document.mainform.pageIndex.value = val;
            document.mainform.submit();
        }
    </script>
</head>
<body>
	<a href="<%=path%>/office/menu.jsp">Menu</a>
<br/>
<br/>
<br/>
	<form action="<c:url value='/office/queryNexmoMessage.do'/>" id="mainform" name="mainform" method="post">
    <s:hidden name="pageIndex" value="1"></s:hidden>
    phoneNumber
    <s:textfield name="phoneNum"></s:textfield>
    status
    <s:textfield name="status1"></s:textfield>
    开始时间:
    <s:textfield name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="true" value="%{start}" />
    结束时间:
    <s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="true" value="%{end}" />
    每页记录:
    <s:select list="#{'20': '20','50':'50','100':'100','500':'500','1000':'1000'}" name="size1"  id="size1" class="ComboType" ></s:select>
   <!--  <select name="size">
        <option value="20">20</option>
        <option value="50">50</option>
        <option value="100">100</option>
        <option value="500">500</option>
        <option value="1000">1000</option>
    </select> -->
    <input type="submit" value="<s:text name="submit"></s:text>"/>

    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    <a href="<%=path%>/office/logout.do">logout</a>
</form>
<br/>
<br/>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
	<tr bgcolor="#0084ff">
		 <td align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;">
            	Phone Number
        </td>
        <td align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;">
          	  status
        </td>
        
        <td align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;">
           	 err_code
        </td>
        <td align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;">
         	   message_timestamp
        </td>
	</tr>
	<s:iterator value="#request.messages" var="message" status="cnt">
		<tr bgcolor="#e4f2ff">
            <td align="center" style="font-size:13px;cursor: pointer;">
            	<s:property value="#message.msisdn"/>
            </td>
             <td align="center" style="font-size:13px;cursor: pointer;">
            	<s:property value="#message.status"/>
            </td>
             <td align="center" style="font-size:13px;cursor: pointer;">
            	<s:property value="#message.err_code"/>
            </td>
             <td align="center" style="font-size:13px;cursor: pointer;">
            	<s:property value="#message.message_timestamp"/>
            </td>
          </tr>
	</s:iterator>
	 <tr>
        <td colspan="7" align="right" bgcolor="66b5ff" align="center"
            style="font-size: 13px;">
            ${page}
        </td>
    </tr>
</table>
<c:import url="/office/script.jsp"/>
</body>
</html>