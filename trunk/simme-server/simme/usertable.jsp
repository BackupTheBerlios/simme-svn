<%@ page import="at.einspiel.simme.server.*, java.util.*" %>
<!--
   [Simme-Server]
          Java ServerPage: usertable.jsp
                    $Date: 2004/09/02 10:23:14 $
                $Revision: 1.3 $
-->
<%
    SessionManager manager = SessionManager.getInstance();

    out.println("<table border='0'>");
    out.println(" <tr>");
    out.println("\t<td>Nickname</td>");
    out.println("\t<td>Model</td>");
    out.println("\t<td>Update</td>");
    out.println("\t<td>State</td>");
    out.println(" </tr>");

    for (Iterator i = manager.userIterator(); i.hasNext();) {
        ManagedUser user = (ManagedUser) i.next();
        out.println(" <tr>");

        out.println("\t<td>" + user.getNick() + "</td>");
        out.println("\t<td>" + user.getClientmodel() + "</td>");
        out.println("\t<td>" + user.secondsSinceLastUpdate() + "</td>");
        out.println("\t<td>" + user.getState().toString() + "</td>");

        out.println(" </tr>");
    }

    out.println("</table>");

%>