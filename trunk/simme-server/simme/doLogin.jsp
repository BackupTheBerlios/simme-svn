<%@page contentType="text/xml; charset=iso-8859-1"%>
<!--
   [Simme-Server]
          Java ServerPage: doLogin.jsp
                    $Date: 2003/12/28 18:21:42 $
                $Revision: 1.4 $
-->

<%@page import="at.einspiel.simme.server.management.ManagedUser"%>

<%@taglib prefix="c" uri="/WEB-INF/tlds/c.tld"%>
<%@taglib prefix="x" uri="/WEB-INF/tlds/x.tld"%>

<c:choose>
    <c:when test="${(empty param.user) || (empty param.pwd)}">
        <%
            String msg = "Please enter username and password.";
            String title = "Error";
            out.print(SessionManager.makeMessage(title, msg));
        %>
    </c:when>
    <c:otherwise>
        <c:set var="nick" value="${param.user}" />
        <c:set var="pass" value="${param.pwd}"  />
        <c:set var="model" value="${param.model}" />
        <c:set var="version" value="${param.version}" />

        <jsp:useBean id="user" class="at.einspiel.simme.server.management.ManagedUser" scope="session">
            <jsp:setProperty name="user" property="nick" param="user" />
            <jsp:setProperty name="user" property="pwd" />
            <jsp:setProperty name="user" property="clientmodel" param="model" />
        </jsp:useBean>

        <%
            // retrieve the user from the session's context
            ManagedUser u = (ManagedUser) session.getAttribute("user");
            // login
            out.print(u.login((String) pageContext.getAttribute("version")).toString());
            // print user to console
            System.out.println(request.getParameter("user"));
        %>
    </c:otherwise>
</c:choose>