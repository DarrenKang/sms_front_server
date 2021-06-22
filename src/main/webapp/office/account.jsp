<%@ page import="ph.sinonet.vg.live.service.interfaces.PlatformService" %>
<%@include file="/office/include.jsp" %>
<%@include file="/office/checkLogin.jsp" %>
<!DOCTYPE html>
<html>
<body>
<meta charset="utf-8">
<title>SMS API</title>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+path+"/";

    PlatformService platformService = (PlatformService) ctx.getBean("platformService");
    request.setAttribute("platFormList", platformService.getPlatformList(null));
%>
<body>

<a href="<%=path%>/office/menu.jsp">Menu</a>
<br>
<br>
<br>
<div style="margin-left:15px;width: 100%;">
    <br />
<s:if test="#session.customer.platformIds=='999'">
    <h1>Create Account</h1>
    <table>
        <s:form namespace="/office" action="addAccount" theme="css_xhtml">
            <tr><td><span style="color:red;">* </span>username</td><td><s:textfield name="smsUserId" maxlength="12" required="true"/></td></tr>
            <tr><td><span style="color:red;">* </span>password</td><td><s:password name="password1" maxlength="12" required="true"/></td></tr>
            <tr><td><span style="color:red;">* </span>re-password</td><td><s:password name="password2" maxlength="12" required="true"/></td></tr>
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
            <tr><td></td><td><input type="submit" value='submit' /></td></tr>
        </s:form>
    </table>
</s:if>
    <h1>Change Password</h1>
    <table>
        <s:form namespace="/office" action="modifyPassword" theme="css_xhtml">
            <tr><td><span style="color:red;">* </span>old password</td><td><s:password name="oldpassword" maxlength="12" required="true"/></td></tr>
            <tr><td><span style="color:red;">* </span>new password</td><td><s:password name="password3" maxlength="12" required="true"/></td></tr>
            <tr><td><span style="color:red;">* </span>re-password</td><td><s:password name="password4" maxlength="12" required="true"/></td></tr>
            <tr><td></td><td><input type="submit" value='submit' /></td></tr>
        </s:form>
    </table>
</div>

<c:import url="/office/script.jsp" />
</body>
</html>
