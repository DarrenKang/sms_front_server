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
                document.deleteSmsConfig.action="<c:url value='/office/deleteSmsConfig.do'/>";
                document.deleteSmsConfig.name.value=idx;
                document.deleteSmsConfig.submit();
            }
        }

        function colorizeMenu(idx){
            document.getElementById(idx).style.backgroundColor = '#e4f2ff';
        }

        function submitForExcuted(btn,action,user){
            btn.disabled=true;
            window.location.href=action+"?name="+user;
        }
		function cancelDisable(operationName){
			window.location.href="<c:url value='/office/cancelDisableSmsConfig.do'/>?name="+operationName;
		}
		function notParticipateAutoSwitch(operationName){
			window.location.href="<c:url value='/office/notParticipateAutoSwitch.do'/>?name="+operationName;
		}
		function sort(sortField){
			var name=document.getElementById("name").value;
			var type=document.getElementById("type").value;
			var flag=document.getElementById("flag").value;
			if(document.getElementById("PlatformID")!=null){
				var PlatformID=document.getElementById("PlatformID").value;
			}
			if(document.getElementById("hiddenProject")!=null){
				var PlatformID=document.getElementById("hiddenProject").value;
			}
			var channel=document.getElementById("channel").value;
			var href1="<c:url value='/office/querySmsConfigByProject.do'/>?sortField="+sortField;
			if(name!=null &&name!=""){
				 href1=href1+"&name="+name;
			}
			if(type!=null && type!=""){
				href1=href1+"&type="+type;
			}
			if(flag!=null && flag!=""){
				href1=href1.concat("&flag="+flag);
			}
			if(PlatformID!=null && PlatformID!=""){
				href1=href1.concat("&projectname="+PlatformID);
			}
			if(channel!=null && channel!=""){
				href1=href1.concat("&channel="+channel);
			}
			window.location.href=href1;
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
<form action="<c:url value='/office/querySmsConfigByProject.do'/>" method="post">
    name
    <s:textfield name="name" id="name"></s:textfield>
   <!-- <input type="text" name="name"/> --> 
    type
    <s:select list="%{#request.smsconfiglist}" listValue="text" listKey="code" emptyOption="true" name="type" id="type"></s:select>
    flag
    <s:select list="#{'0': 'enable','1':'disable'}" name="flag" emptyOption="true" id="flag"></s:select>

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
                        	<td	align="center"  style="display:none;">name</td>
                        	<td	align="center"  style="display:none;">type</td>
                        	<td	align="center"  style="display:none;">flag</td>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="javascript:sort('name');" >
                                name
                            </td>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="javascript:sort('type');">
                                type
                            </td>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="javascript:sort('flag');">
                                flag
                            </td>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="javascript:sort('priority');">
                                priority
                            </td>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="javascript:sort('totalCounts');">
                                totalCounts
                            </td>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="javascript:sort('areadyCounts');">
                                areadyCounts
                            </td>
                            
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="javascript:sort('remark');">
                                REMARKS
                            </td>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="javascript:sort('server');">
                                sms server
                            </td>
                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="javascript:sort('port');">
                                sms port
                            </td>

                            <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="javascript:sort('user');">
                                user id
                            </td>
							
							
							<!-- 
                            <s:if test="#session.customer.platformIds=='999'">
                                <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >
                                    sms password
                                </td>
                            </s:if>
							 
							 --> 
							 
							 
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
                             <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >
                                auto switch 
                            </td>
                        </tr>
                        <s:iterator value="%{#request.smsConfigList}" var="scx" status="cnt">
                            <form name="updateForm<s:property value="#cnt.count"/>" action="<%=request.getContextPath() %>/office/updateSmsConfig.do" method="post" >
                               <!--  <s:hidden name="name" value="%{#scx.name}"/>
                                <s:hidden name="type" value="%{#scx.type}"  />
                                <s:hidden name="flag" value="%{#scx.flag}" /> -->
                                 <s:if test="#scx.flag1==1 && #scx.totalCounts==#scx.areadyCounts">
                                	<tr bgcolor="#FF0000">
                                </s:if>
                                <s:if test="#scx.flag1==1 && #scx.totalCounts!=#scx.areadyCounts">
                                	<tr bgcolor="#CCCCCC">
                                </s:if>
                                <s:if test="#scx.flag1!=1">
                                	<tr bgcolor="#e4f2ff">
                                </s:if>
                                	<td	align="center"  style="display:none;">
                                		<input type="text" name="name" value="<s:property value="%{#scx.name}" />" />
                                	</td>
                                	<td	align="center"  style="display:none;">
                                		<input type="text" name="type" value="<s:property value="%{#scx.type}" />" />
                                	</td>
                                	<td	align="center"  style="display:none;">
                                		<input type="text" name="flag" value="<s:property value="%{#scx.flag}" />" />
                                	</td>
                                    <td  align="center"  style="font-size:13px;">
                                        <s:property value="#scx.name"/>
                                    </td>
                                    <td  align="center"  style="font-size:13px;">
                                        <s:property value="#scx.type"/>
                                    </td>
                                    <td  align="center"  style="font-size:13px;">
                                        <s:if test="#scx.flag == 0">
                                            Enable
                                        </s:if>
                                        <s:else>
                                            Disable
                                        </s:else>
                                    </td>
                                     <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="priority" maxlength="20" size="5" value="<s:property value="#scx.priority" />" />
                                    </td>
                                    <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="totalCounts" maxlength="20" size="5" value="<s:property value="#scx.totalCounts" />" />
                                    </td>
                                   
                                    <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="areadyCounts" maxlength="20" size="5" disabled=disabled value="<s:property value="#scx.areadyCounts" />" />
                                    </td>
                                    <td  align="center"  style="font-size:13px;">
                                        <textarea  name="remark" maxlength="20" value="<s:property value="#scx.remark"/>" ><s:property value="#scx.remark"/></textarea><%--NEW Collumn--%>
                                    </td>
                                    <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="server" maxlength="200" size="15" value="<s:property value="#scx.server" />" />
                                    </td>
                                    <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="port" maxlength="20" size="5" value="<s:property value="#scx.port" />" />
                                    </td>
									
                                    <td  align="center"  style="font-size:13px;">
                                        <input type="text" name="user" maxlength="50" size="20" value="<s:property value="#scx.user" />" />
                                    </td>
									
									
									<!-- 
                                    <s:if test="#session.customer.platformIds=='999'">
                                        <td  align="center"  style="font-size:13px;">
                                            <input type="text" name="password" maxlength="50" size="10" value="<s:property value="#scx.password" />" />
                                        </td>
                                    </s:if>
									
									 -->
									
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
                                    <s:if test="#session.customer.platformIds=='999'">
                                        <td  align="center"  style="font-size:13px;">
                                            <div style="position: relative;  text-align:left">
                                                <c:set var="pid" value="${fn:split(scx.platformIds, ',')}" scope="request" />
                                                <s:iterator value="%{#request.platFormList}" var="optx">
                                                    <s:if test="%{#optx.platformId in #request.pid}">
                                                        <input type="checkbox" name="platformIds" id="belongProject" value="<s:property value="#optx.platformId" />" checked="checked" ><s:property value="#optx.name" /><br>
                                                    </s:if>
                                                    <s:else>
                                                        <input type="checkbox" name="platformIds" id="belongProject" value="<s:property value="#optx.platformId" />" ><s:property value="#optx.name" /><br>
                                                    </s:else>
                                                </s:iterator>
                                            </div>
                                        </td>
                                    </s:if>
                                    <td align="center"  style="font-size:13px;">
                                        <input type="button" name="update" value="Update" onclick="document.forms['updateForm<s:property value="#cnt.count"/>'].submit();" /><br/>
                                        <input type="button" name="delete" value="Delete" onclick="javascript:deleteAC('<s:property value="#scx.name" />');" /><br/>
                                        <s:if test="#scx.flag == 1">
                                            <c:url var="action" value="/office/enableConfig.do" scope="request" />
                                            <input type="button" value='Enable'  onclick="submitForExcuted(this,'${action }','${scx.name}');"/><br/>
                                        </s:if>
                                        <s:else>
                                            <c:url var="action" value="/office/disableConfig.do" scope="request" />
                                            <input type="button" value='Disable'  onclick="submitForExcuted(this,'${action }','${scx.name}');"/><br/>
                                        </s:else>
                                    </td>
                                    <td align="center" style="font-size:13px;">
                                    	<input type="button" name="CancelDisable" value="参与自动切换" onclick="javascript:cancelDisable('<s:property value="#scx.name"/>');"></input><br/>
                                    	<input type="button" name="fit" value="不参与自动切换" onclick="javascript:notParticipateAutoSwitch('<s:property value="#scx.name"/>');"></input><br/>
                                    </td>
                                </tr>
                            </form>
                        </s:iterator>
                    </table>

                    <div style="margin-left:15px;width: 100%;">
                        <br />
                        <h1>New Config</h1>
                        <table>
                            <s:form namespace="/office" action="addSmsConfig" theme="css_xhtml">
                                <tr><td><span style="color:red;">* </span>sms name</td><td><s:textfield name="name" maxlength="20"/></td></tr>
                                <tr>
                                    <td><span style="color:red;">* </span>sms type</td>
                                    <td>
                                        <select name="type" >
                                            <s:iterator value="%{#request.smsconfiglist}" var="scc">
                                                <option value="<s:property value="#scc.code" />" ><s:property value="#scc.text" /></option>
                                            </s:iterator>
                                        </select>
                                    </td>
                                </tr>
                                <tr><td><span style="color:red;">* </span>sms server</td><td><s:textfield name="server" maxlength="200"/></td></tr>
                                <tr><td>sms port</td><td><s:textfield name="port" maxlength="20"/></td></tr>
                                <tr><td><span style="color:red;">* </span>sms user id</td><td><s:textfield name="user" maxlength="50"/></td></tr>
                                <tr><td><span style="color:red;">* </span>sms password</td><td><s:textfield name="password" maxlength="50"/></td></tr>
                                <tr><td><span style="color:red;">* </span>sms priority</td><td><s:textfield name="priority" maxlength="50"/></td></tr>
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
