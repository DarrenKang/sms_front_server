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

    <title>Sms Config</title>
    <link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	

    <script type="text/javascript">
        function deleteAC(idx){
            if(window.confirm("是否确定删除?")){
                document.deleteSmsServerConfig.action="<c:url value='/office/deleteSmsServerConfig.do'/>";
                document.deleteSmsServerConfig.id.value=idx;
                document.deleteSmsServerConfig.submit();
            }
        }

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
<s:form method="post" id="deleteSmsServerConfig">
    <s:hidden name="id"></s:hidden>
</s:form>
<a href="<%=path%>/office/menu.jsp">Menu</a>
<br/>
<br/>
<br/>
<form action="<c:url value='/office/querySmsServerConfigByProject.do'/>" method="post">
    name
    <s:textfield name="name" id="name"></s:textfield>
   <!-- <input type="text" name="name"/> --> 

    <s:if test="#session.customer.platformIds=='999'">
        Platform ID
        <s:select list="%{#request.platFormList}" listKey="platformId" listValue="platformId" emptyOption="true" name="projectname" id="PlatformID"></s:select>
    </s:if>
    <s:else>
        <s:hidden name="projectname" value="%{#session.customer.platformIds}" id="hiddenProject"/>
    </s:else>
	 channel
	<s:select list="#{'1':' Channel 1','2':' Channel 2','3':' Channel 3','4':' Channel 4','5':' Channel 5','6':' Channel 6' }" name="channel" emptyOption="true" id="channel"></s:select>

    <input type="submit" value="submit" />

    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    <a href="<%=path%>/office/logout.do">logout</a>

</form>
<div id="excel_menu_left">

</div>
<div id="middle">
    <div id="right">
        <div id="right_01">
            <div id="right_001">
                <div id="right_02">
                    <div id="right_03"></div>

                </div>

                <div id="right_04">
                    <div style="margin-left:35%; width: 100%;">
                    </div><br/>
                    <!--<s:textfield name="sor123" id="sor123"></s:textfield> -->
                    <table width="98%"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7" id="table1">
                        <tr bgcolor="#0084ff">
                        	<td	align="center">id</td>
                        	
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >
                                name
                            </td>
                           <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;">
                                sms serverIP
                            </td>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >
                                sms username
                            </td>
                            <s:if test="#session.customer.platformIds=='999'">
                                <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >
                                    sms password
                                </td>
                            </s:if>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >
                                REMARKS
                            </td>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >
                                Channel
                            </td>
                            <s:if test="#session.customer.platformIds=='999'">
                                <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >
                                    Belong Project
                                </td>
                            </s:if>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >
                                Operation
                            </td>
                        </tr>
                        <s:iterator value="%{#request.smsServerConfigList}" var="scx" status="cnt">
                            <form name="updateForm<s:property value="#cnt.count"/>" action="<%=request.getContextPath() %>/office/updateSmsServerConfig.do" method="post" >
                              	<tr>
                                	<td	align="center"  style="display:none;">
                                	<input type="text" name="id" maxlength="20" size="5"  value="<s:property value="#scx.id" />" />
                                    </td>
                                    <td  align="center"  style="font-size:13px;">
                                        <s:property value="#scx.id"/>
                                    </td>
                                    <td  align="center"  style="font-size:13px;">
                                     <input type="text" name="name" maxlength="20" size="5" value="<s:property value="#scx.name" />" />
                                    </td>
                                    <td  align="center"  style="font-size:13px;">
                                    <input type="text" name="serverIp" maxlength="20" size="5" value="<s:property value="#scx.serverIp" />" />
                                    </td>
                                     <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="username" maxlength="20" size="5" value="<s:property value="#scx.username" />" />
                                    </td>
                                    <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="password" maxlength="20" size="5" value="<s:property value="#scx.password" />" />
                                    </td>
                                   
                                    <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="remark" maxlength="20" size="5"  value="<s:property value="#scx.remark" />" />
                                    </td>
                                    <!-- 
                                     <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="channel" maxlength="20" size="5"  value="<s:property value="#scx.channel" />" />
                                    </td> -->
                                     <td  align="center"  style="font-size:13px;">
                                    	  <s:if test="#scx.channel == 1">
                                            Channel 1 <input type="radio" name="channel" value="1" checked="checked"/>
                                            Channel 2 <input type="radio" name="channel" value="2"  />
                                            Channel 3 <input type="radio" name="channel" value="3"  />
                                            Channel 4 <input type="radio" name="channel" value="4"  />
                                            Channel 5 <input type="radio" name="channel" value="5"  />
                                            Channel 6 <input type="radio" name="channel" value="6"  />
                                        </s:if>
                                        <s:elseif test="#scx.channel == 2">
                                        	 Channel 1 <input type="radio" name="channel" value="1" />
                                            Channel 2 <input type="radio" name="channel" value="2" checked="checked" />
                                            Channel 3 <input type="radio" name="channel" value="3"  />
                                             Channel 4 <input type="radio" name="channel" value="4"  />
                                            Channel 5 <input type="radio" name="channel" value="5"  />
                                            Channel 6 <input type="radio" name="channel" value="6"  />
                                        </s:elseif>
                                         <s:elseif test="#scx.channel == 3">
                                        	 Channel 1 <input type="radio" name="channel" value="1" />
                                            Channel 2 <input type="radio" name="channel" value="2"  />
                                            Channel 3 <input type="radio" name="channel" value="3" checked="checked" />
                                             Channel 4 <input type="radio" name="channel" value="4"  />
                                            Channel 5 <input type="radio" name="channel" value="5"  />
                                            Channel 6 <input type="radio" name="channel" value="6"  />
                                        </s:elseif>
                                         <s:elseif test="#scx.channel == 4">
                                        	 Channel 1 <input type="radio" name="channel" value="1" />
                                            Channel 2 <input type="radio" name="channel" value="2"  />
                                            Channel 3 <input type="radio" name="channel" value="3"  />
                                             Channel 4 <input type="radio" name="channel" value="4" checked="checked" />
                                            Channel 5 <input type="radio" name="channel" value="5"  />
                                            Channel 6 <input type="radio" name="channel" value="6"  />
                                        </s:elseif>
                                         <s:elseif test="#scx.channel == 5">
                                        	 Channel 1 <input type="radio" name="channel" value="1" />
                                            Channel 2 <input type="radio" name="channel" value="2"  />
                                            Channel 3 <input type="radio" name="channel" value="3"  />
                                             Channel 4 <input type="radio" name="channel" value="4"  />
                                            Channel 5 <input type="radio" name="channel" value="5" checked="checked" />
                                            Channel 6 <input type="radio" name="channel" value="6"  />
                                        </s:elseif>
                                        <s:else >
                                            Channel 1 <input type="radio" name="channel" value="1"   />
                                            Channel 2 <input type="radio" name="channel" value="2" />
                                            Channel 3 <input type="radio" name="channel" value="3"  />
                                             Channel 4 <input type="radio" name="channel" value="4"  />
                                            Channel 5 <input type="radio" name="channel" value="5"  />
                                            Channel 6 <input type="radio" name="channel" value="6"  checked="checked"/>
                                        </s:else> 
                                    </td>
                                     <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="platformIds" maxlength="20" size="5"  value="<s:property value="#scx.platformId" />" />
                                    </td>
                                    <td align="center"  style="font-size:13px;">
                                        <input type="button" name="update" value="Update" onclick="document.forms['updateForm<s:property value="#cnt.count"/>'].submit();" /><br/>
                                        <input type="button" name="delete" value="Delete" onclick="javascript:deleteAC('<s:property value="#scx.id" />');" /><br/>
                                    </td>
                                </tr>
                            </form>
                        </s:iterator>
                    </table>

                    <div style="margin-left:15px;width: 100%;">
                        <br />
                        <h1>New Asiaroutes Config</h1>
                        <table>
                            <s:form namespace="/office" action="addSmsAsiarouteConfig" theme="css_xhtml">
                                <tr><td><span style="color:red;">* </span>sms name</td><td><s:textfield name="name" maxlength="20"/></td></tr>
                                <tr><td><span style="color:red;">* </span>sms serverIP</td><td><s:textfield name="serverIp" maxlength="200"/></td></tr>
                                <tr><td><span style="color:red;">* </span>sms user id</td><td><s:textfield name="username" maxlength="50"/></td></tr>
                                <tr><td><span style="color:red;">* </span>sms password</td><td><s:textfield name="password" maxlength="50"/></td></tr>
                                <tr><td><span style="color:red;">* </span>channel</td><td> <s:select name="channel" list="{'1','2','3','4','5','6'}" /> </td></tr>
                                <tr>
                                    <td colspan="2">
                                        <s:if test="#session.customer.platformIds=='999'">
                                            Platform ID<br>
                                            <s:iterator value="%{#request.platFormList}" var="optx">
                                                <input type="checkbox" name="platformIds" id="belongProject" value="<s:property value="#optx.platformId" />" ><s:property value="#optx.name" /><br>
                                            </s:iterator>
                                        </s:if>
                                        <s:else>
                                            <s:hidden name="platformIds" value="%{#session.customer.platformIds}"/>
                                        </s:else>
                                    </td>
                                </tr>
                                <tr><td>remark</td>
                                    <td>
                                        <textarea  name="remark" maxlength="20" value="<s:property value="#scx.remark"/>" ><s:property value="#scx.remark" /></textarea><%--NEW Collumn--%>
                                    </td>
                                </tr>
                                <tr><td></td><td><input type="submit" value='submit' /></td></tr>
                            </s:form>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>
