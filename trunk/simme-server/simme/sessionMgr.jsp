<%@page contentType="text/xml; charset=iso-8859-1"%>
<!--
   [Simme-Server]
          Java ServerPage: sessionMgr.jsp
                    $Date: 2003/12/28 18:21:43 $
                $Revision: 1.1 $
-->

<%@page import="at.einspiel.simme.server.management.ManagedUser"%>
<%@page import="at.einspiel.simme.server.management.SessionManager"%>

<%@taglib prefix="c" uri="/WEB-INF/tlds/c.tld"%>
<%@taglib prefix="x" uri="/WEB-INF/tlds/x.tld"%>

<%
    // retrieve the user from the session's context
    ManagedUser u = (ManagedUser) session.getAttribute("user");
    System.out.println(u.getNick());

    // TODO retrieve request parameters and answer user
%>
