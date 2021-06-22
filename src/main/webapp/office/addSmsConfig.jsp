<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<%@page import="ph.sinonet.vg.live.model.enums.SmsConfigCode"%>
<%@page import="java.util.List"%>
<%@ page import="ph.sinonet.vg.live.dao.SmsDao" %>
<%@ page import="ph.sinonet.vg.live.dao.PlatformDao" %>
<%@ page import="ph.sinonet.vg.live.service.interfaces.PlatformService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Add Sms Config</title>
    <link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />


        <%
        List list=SmsConfigCode.getActiveSmsApi();
        if(list!=null && list.size()>0)
        {
        request.setAttribute("smsconfiglist",list);
        }

        PlatformService platformService = (PlatformService) ctx.getBean("platformService");
        request.setAttribute("platFormList", platformService.getPlatformList(null));
		 %>

</head>
<body onLoad="javascript:colorizeMenu('newaccessid');">


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
                                <tr><td><span style="color:red;">* </span>channel</td><td> <s:select name="channel" list="{'1','2'}" /> </td></tr>
                                <tr>
                                    <td colspan="2">
                                        Platform ID<br>
                                            <s:iterator value="%{#request.platFormList}" var="optx">
                                                <s:if test="%{#optx.platformId==#scx.platformIds}" >
                                                    <input type="checkbox" name="platformIds" id="belongProject" value="<s:property value="#optx.platformId" />" checked="checked" ><s:property value="#optx.platformId" /><br>
                                                </s:if>
                                                <s:else>
                                                    <input type="checkbox" name="platformIds" id="belongProject" value="<s:property value="#optx.platformId" />" ><s:property value="#optx.platformId" /><br>
                                                </s:else>
                                            </s:iterator>
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
