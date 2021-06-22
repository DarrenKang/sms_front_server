<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page session="true" isELIgnored="false" language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
    <title>500 System Error</title>
</head>
<fmt:setBundle basename="messages" var="message" scope="request"/>
<fmt:message key="imgpath" bundle="${requestScope.message}" var="image" scope="request"/>
<body>
<p align="center">Server-side Error</p>
</body>
</html>