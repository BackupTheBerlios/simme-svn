<%@ page import="at.einspiel.simme.server.management.*, java.util.*" %>

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
        out.println("\t<td>" + user.getStateAsString() + "</td>");

        out.println(" </tr>");
    }

    out.println("</table>");

%>