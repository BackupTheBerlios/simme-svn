<%@page contentType="text/html"%>
<!--
   [Simme-Server]
          Java ServerPage: showOnline.jsp
                    $Date: 2003/12/28 18:21:43 $
                $Revision: 1.3 $
-->

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
