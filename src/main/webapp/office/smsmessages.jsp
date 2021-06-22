<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="ph.sinonet.vg.live.model.enums.SmsConfigCode"%>
<%@ page import="ph.sinonet.vg.live.service.interfaces.PlatformService"%>
<%@include file="/office/include.jsp"%>
<%@include file="/office/checkLogin.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + path + "/";
%>

<title>SMS Sent List</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="<c:url value="/js/jquery-1.5.1.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/js/jquery-ui-1.8.13.custom.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value='/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript">
	function gopage(val) {
		document.mainform.pageIndex.value = val;
		document.mainform.submit();
	}
</script>

<%
	List list = SmsConfigCode.getActiveSmsApi();
	if (list != null && list.size() > 0) {
		request.setAttribute("smsconfiglist", list);
	}

	PlatformService platformService = (PlatformService) ctx.getBean("platformService");
	request.setAttribute("platFormList", platformService.getPlatformList(null));
%>

</head>
<body>

	<a href="<%=path%>/office/menu.jsp">Menu</a>
	<br />
	<br />
	<br />
	<form action="<c:url value='/office/querySentSMS.do'/>" id="mainform"
		name="mainform" method="post">
		<s:hidden name="pageIndex" value="1"></s:hidden>
		code
		<s:select list="%{#request.smsProvider}" listValue="text"
			listKey="code" emptyOption="true" name="smsprovider"></s:select>
		<%--project--%>
		<%--<s:select list="%{#request.products}" listKey="code" listValue="text" emptyOption="true"--%>
		<%--name="projectname"></s:select>--%>

		<s:if test="#session.customer.platformIds=='999'">
        Platform ID
        <s:select list="%{#request.platFormList}" listKey="platformId"
				listValue="platformId" emptyOption="true" name="projectname"></s:select>
		</s:if>
		<s:else>
			<s:hidden name="projectname" value="%{#session.customer.platformIds}" />
		</s:else>
		开始时间:
		<s:textfield id="startDate" name="start" size="17"
			onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
			My97Mark="true" value="%{start}" />
		结束时间:
		<s:textfield name="end" size="17"
			onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
			My97Mark="true" value="%{end}" />
		Account ID:
		<s:textfield name="accountId" size="11"></s:textfield>
		Phone Number:
		<s:textfield name="phonenumber" size="13"></s:textfield>
		每页记录:
		<s:select
			list="#{'20': '20','50':'50','100':'100','500':'500','1000':'1000'}"
			name="size1" id="size1" class="ComboType"></s:select>

		<!-- <select name="size">
        <option value="20">20</option>
        <option value="50">50</option>
        <option value="100">100</option>
        <option value="500">500</option>
        <option value="1000">1000</option>
    </select> -->
		<input type="submit" value="<s:text name="submit"></s:text>" />

		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		<a href="<%=path%>/office/logout.do">logout</a>
	</form>

	<br />
	<br />
	<table width="100%" border="0" align="center" cellpadding="0"
		cellspacing="1" bgcolor="#99c8d7">
		<tr bgcolor="#0084ff">
			<!--  new output  -->
			<td align="center"
				style="font-size: 13px;; color: #FFFFFF; font-weight: bold; cursor: pointer;">
				Product</td>
			<td align="center"
				style="font-size: 13px;; color: #FFFFFF; font-weight: bold; cursor: pointer;">
				SMS Provider</td>
			<td align="center"
				style="font-size: 13px;; color: #FFFFFF; font-weight: bold; cursor: pointer;">
				Account ID</td>
			<td align="center"
				style="font-size: 13px;; color: #FFFFFF; font-weight: bold; cursor: pointer;">
				Phone Number</td>
			<td align="center"
				style="font-size: 13px;; color: #FFFFFF; font-weight: bold; cursor: pointer;">
				Message</td>
			<td align="center"
				style="font-size: 13px;; color: #FFFFFF; font-weight: bold; cursor: pointer;">
				Response</td>
			<td align="center"
				style="font-size: 13px;; color: #FFFFFF; font-weight: bold; cursor: pointer;">
				smsLength</td>
			<td align="center"
				style="font-size: 13px;; color: #FFFFFF; font-weight: bold; cursor: pointer;">
				Create Time</td>
		</tr>
		<s:iterator value="%{#request.messages}" var="sms" status="cnt">
			<tr bgcolor="#e4f2ff">
				<td align="center" style="font-size: 13px; cursor: pointer;"><s:iterator
						value="%{#request.platFormList}" var="optx">
						<s:if test="%{#optx.platformId==#sms.product}">
							<s:property value="#optx.name" />
						</s:if>
					</s:iterator></td>
				<td align="center" style="font-size: 13px; cursor: pointer;"><s:property
						value="#sms.smsprovider" /></td>
				<td align="center" style="font-size: 13px; cursor: pointer;"><s:property
						value="#sms.user_account" /></td>
				<td align="center" style="font-size: 13px; cursor: pointer;"><s:property
						value="#sms.phonenumber" /></td>
				<td align="center" style="font-size: 13px; cursor: pointer;"><s:property
						value="#sms.content" /></td>
				<td align="center" style="font-size: 13px; cursor: pointer;"><s:property
						value="#sms.result" /></td>
				<td align="center" style="font-size: 13px; cursor: pointer;"><s:property
						value="#sms.smsLength" /></td>
				<td align="center" style="font-size: 13px; cursor: pointer;"><s:property
						value="#sms.createtime" /></td>
			</tr>
		</s:iterator>
		<tr>

			<td colspan="7" align="right" bgcolor="66b5ff" align="center"
				style="font-size: 13px;">${page}</td>

		</tr>
	</table>

	<c:import url="/office/script.jsp" />
	<script type="text/javascript">
		if (localStorage.getItem('defaultDate') == null) {
			setdate()
		};

		//日期框添加默认值
		function setdate() {
			var today = new Date();
			var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-'
					+ today.getDate();
			var time = "00:00:00";
			var dateTime = date + ' ' + time;
			document.getElementById('startDate').value = dateTime;
			localStorage.setItem('defaultDate', dateTime)
		}
	</script>
</body>
</html>
