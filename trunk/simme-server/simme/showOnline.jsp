<%@page contentType="text/html"%>
<%@page import="at.einspiel.simme.server.management.*"%>

<html>
<head><title>Online Users - Simme</title></head>

<body>
<h1>Users currently online</h1>

<%
    SessionManager sMgr = SessionManager.getInstance();
    if (sMgr.getNumberOfUsers() > 0) {
%>

<%@ include file="usertable.jsp" %>

<%
    } else {
       out.println("No user currently logged in");
    }
%>
</body>

</html>
