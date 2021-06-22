<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="ph.sinonet.vg.live.model.Constants" %>
<%
    if(session.getAttribute(Constants.SESSION_CUSTOMERID)==null){
        out.println("<script type='text/javascript'>alert('Please login first!');top.location.href='"+request.getContextPath()+ "/index.jsp"+"'</script>");
        return;
    }
%>
